package com.stylefeng.guns.wxpay;

import com.stylefeng.guns.config.properties.WxPayV3Properties;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAPublicKeyConfig;
import com.wechat.pay.java.core.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.Instant;

/**
 * 新版商家转账到零钱（单笔）
 * API: POST /v3/fund-app/mch-transfer/transfer-bills
 */
@Service
public class WxPayV3TransferService {

    private static final Logger log = LoggerFactory.getLogger(WxPayV3TransferService.class);
    private static final String TRANSFER_URL = "https://api.mch.weixin.qq.com/v3/fund-app/mch-transfer/transfer-bills";

    @Resource
    private WxPayV3Properties v3Properties;

    private HttpClient httpClient;

    @PostConstruct
    public void init() {
        String apiV3Key = v3Properties.getApiV3Key();
        if (apiV3Key == null || apiV3Key.contains("待填") || apiV3Key.length() != 32) {
            log.warn("V3商家转账配置不完整(apiV3Key未配置)，跳过初始化");
            return;
        }
        try {
            Config config = new RSAPublicKeyConfig.Builder()
                    .merchantId(v3Properties.getMchId())
                    .privateKeyFromPath(v3Properties.getPrivateKeyPath())
                    .publicKeyFromPath(v3Properties.getPublicKeyPath())
                    .publicKeyId(v3Properties.getPublicKeyId())
                    .merchantSerialNumber(v3Properties.getCertSerialNo())
                    .apiV3Key(apiV3Key)
                    .build();
            httpClient = new DefaultHttpClientBuilder()
                    .credential(config.createCredential())
                    .validator(config.createValidator())
                    .build();
            log.info("V3新版商家转账初始化成功");
        } catch (Throwable t) {
            log.error("V3商家转账初始化失败", t);
        }
    }

    /**
     * 新版商家转账到用户零钱（单笔）
     *
     * @param openid    用户小程序openid
     * @param accid     事故ID
     * @param amountFen 金额，单位：分
     * @return TransferResult 包含 success、packageInfo、outBillNo
     */
    public TransferResult transferToUser(String openid, Integer accid, long amountFen) {
        if (httpClient == null) {
            log.warn("V3商家转账未初始化，无法执行转账 accid={}", accid);
            return TransferResult.fail();
        }
        try {
            long now = Instant.now().toEpochMilli();
            String outBillNo = "accid" + accid + "t" + now;

            String jsonBody = "{" +
                    "\"appid\":\"" + v3Properties.getAppId() + "\"," +
                    "\"out_bill_no\":\"" + outBillNo + "\"," +
                    "\"transfer_scene_id\":\"" + v3Properties.getTransferSceneId() + "\"," +
                    "\"openid\":\"" + openid + "\"," +
                    "\"transfer_amount\":" + amountFen + "," +
                    "\"transfer_remark\":\"事故上报红包奖励\"," +
                    "\"transfer_scene_report_infos\":[" +
                    "{\"info_type\":\"活动名称\",\"info_content\":\"事故上报奖励\"}," +
                    "{\"info_type\":\"奖励说明\",\"info_content\":\"事故上报红包奖励\"}" +
                    "]" +
                    "}";

            HttpHeaders headers = new HttpHeaders();
            headers.addHeader("Accept", "application/json");
            headers.addHeader("Content-Type", "application/json");

            JsonRequestBody requestBody = new JsonRequestBody.Builder()
                    .body(jsonBody)
                    .build();

            HttpRequest httpRequest = new HttpRequest.Builder()
                    .httpMethod(HttpMethod.POST)
                    .url(TRANSFER_URL)
                    .headers(headers)
                    .body(requestBody)
                    .build();

            log.info("V3商家转账请求 accid={} outBillNo={}", accid, outBillNo);
            HttpResponse<JsonResponseBody> response = httpClient.execute(httpRequest, JsonResponseBody.class);

            // 从 response.getBody() 获取原始响应体
            // 注意：response.getServiceResponse() 是 Gson 反序列化的结果，
            // 由于 JsonResponseBody 只有 body 字段，而微信响应中无此字段，
            // getServiceResponse().getBody() 会返回 null，不能使用
            String bodyStr = "";
            com.wechat.pay.java.core.http.ResponseBody rawBody = response.getBody();
            if (rawBody instanceof JsonResponseBody) {
                String raw = ((JsonResponseBody) rawBody).getBody();
                if (raw != null && !raw.isEmpty()) {
                    bodyStr = raw;
                }
            }
            log.info("V3商家转账响应 accid={} outBillNo={} body={}", accid, outBillNo, bodyStr);

            if (bodyStr.isEmpty() || "{}".equals(bodyStr.trim())) {
                log.error("V3商家转账响应体为空 accid={}", accid);
                return TransferResult.fail();
            }

            // 解析 package_info
            String packageInfo = null;
            if (bodyStr.contains("\"package_info\"")) {
                int idx = bodyStr.indexOf("\"package_info\"");
                int colon = bodyStr.indexOf(":", idx);
                int quote1 = bodyStr.indexOf("\"", colon + 1);
                int quote2 = bodyStr.indexOf("\"", quote1 + 1);
                if (quote1 >= 0 && quote2 > quote1) {
                    packageInfo = bodyStr.substring(quote1 + 1, quote2);
                }
            }

            if (packageInfo == null || packageInfo.isEmpty()) {
                log.error("V3商家转账响应缺少package_info accid={} body={}", accid, bodyStr);
                return TransferResult.fail();
            }

            log.info("V3商家转账成功 accid={} outBillNo={} packageInfo={}", accid, outBillNo, packageInfo);
            return TransferResult.ok(packageInfo, outBillNo);
        } catch (Exception e) {
            log.error("V3商家转账失败 accid={} openid={} error={}", accid, openid, e.getMessage(), e);
            return TransferResult.fail();
        }
    }
}

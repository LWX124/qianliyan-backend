package com.stylefeng.guns.wxpay;

import com.alibaba.fastjson.JSONObject;
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
     * @return true=成功，false=失败
     */
    public boolean transferToUser(String openid, Integer accid, long amountFen) {
        if (httpClient == null) {
            log.warn("V3商家转账未初始化，无法执行转账 accid={}", accid);
            return false;
        }
        try {
            long now = Instant.now().toEpochMilli();
            String outBillNo = "accid" + accid + "t" + now;

            JSONObject body = new JSONObject();
            body.put("appid", v3Properties.getAppId());
            body.put("out_bill_no", outBillNo);
            body.put("transfer_scene_id", v3Properties.getTransferSceneId());
            body.put("openid", openid);
            body.put("transfer_amount", amountFen);
            body.put("transfer_remark", "事故上报红包奖励");

            HttpHeaders headers = new HttpHeaders();
            headers.addHeader("Accept", "application/json");
            headers.addHeader("Content-Type", "application/json");

            JsonRequestBody requestBody = new JsonRequestBody.Builder()
                    .body(body.toJSONString())
                    .build();

            HttpRequest httpRequest = new HttpRequest.Builder()
                    .httpMethod(HttpMethod.POST)
                    .url(TRANSFER_URL)
                    .headers(headers)
                    .body(requestBody)
                    .build();

            HttpResponse<JsonResponseBody> response = httpClient.execute(httpRequest, JsonResponseBody.class);
            log.info("V3商家转账成功 accid={} outBillNo={} response={}",
                    accid, outBillNo, response.getServiceResponse());
            return true;
        } catch (Exception e) {
            log.error("V3商家转账失败 accid={} openid={} error={}", accid, openid, e.getMessage(), e);
            return false;
        }
    }
}

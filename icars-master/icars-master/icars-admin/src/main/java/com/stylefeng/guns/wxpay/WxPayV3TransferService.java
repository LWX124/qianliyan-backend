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
import java.math.BigDecimal;
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
        System.out.println("========== WxPayV3TransferService.init() 开始 ==========");
        String apiV3Key = v3Properties.getApiV3Key();
        System.out.println("apiV3Key: " + (apiV3Key != null ? apiV3Key.length() + "位" : "null"));

        if (apiV3Key == null || apiV3Key.contains("待填") || apiV3Key.length() != 32) {
            System.out.println("V3商家转账配置不完整，跳过初始化");
            log.error("V3商家转账配置不完整(apiV3Key未配置或长度不对, len={}), 跳过初始化", apiV3Key == null ? "null" : String.valueOf(apiV3Key.length()));
            return;
        }

        try {
            System.out.println("开始构建RSAPublicKeyConfig...");
            Config config = new RSAPublicKeyConfig.Builder()
                    .merchantId(v3Properties.getMchId())
                    .privateKeyFromPath(v3Properties.getPrivateKeyPath())
                    .publicKeyFromPath(v3Properties.getPublicKeyPath())
                    .publicKeyId(v3Properties.getPublicKeyId())
                    .merchantSerialNumber(v3Properties.getCertSerialNo())
                    .build();
            System.out.println("RSAPublicKeyConfig构建成功");

            System.out.println("开始构建httpClient...");
            httpClient = new DefaultHttpClientBuilder()
                    .credential(config.createCredential())
                    .validator(config.createValidator())
                    .build();
            System.out.println("httpClient构建成功");

            log.error("V3新版商家转账初始化成功, mchId={}", v3Properties.getMchId());
            System.out.println("========== WxPayV3TransferService.init() 成功 ==========");
        } catch (Throwable t) {
            System.out.println("初始化失败: " + t.getClass().getName() + ": " + t.getMessage());
            t.printStackTrace();
            log.error("V3商家转账初始化失败", t);
        }
    }

    /**
     * 获取 apiV3Key，用于回调通知解密
     */
    public String getApiV3Key() {
        return v3Properties.getApiV3Key();
    }

    /**
     * 新版商家转账到用户零钱（单笔）
     *
     * @param openid    用户小程序openid
     * @param accid     事故ID
     * @param amountFen 金额，单位：分
     * @return TransferResult 包含 success、packageInfo、outBillNo、failCode、failMessage
     */
    public TransferResult transferToUser(String openid, Integer accid, long amountFen) {
        if (httpClient == null) {
            log.warn("V3商家转账未初始化，无法执行转账 accid={}", accid);
            return TransferResult.fail("NOT_INITIALIZED", "V3商家转账未初始化");
        }
        try {
            long now = Instant.now().toEpochMilli();
            String outBillNo = "accid" + accid + "t" + now;

            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("\"appid\":\"").append(v3Properties.getAppId()).append("\",");
            sb.append("\"out_bill_no\":\"").append(outBillNo).append("\",");
            sb.append("\"transfer_scene_id\":\"").append(v3Properties.getTransferSceneId()).append("\",");
            sb.append("\"openid\":\"").append(openid).append("\",");
            sb.append("\"transfer_amount\":").append(amountFen).append(",");
            sb.append("\"transfer_remark\":\"事故上报红包奖励\",");
            if (v3Properties.getNotifyUrl() != null && !v3Properties.getNotifyUrl().isEmpty()) {
                sb.append("\"notify_url\":\"").append(v3Properties.getNotifyUrl()).append("\",");
            }
            sb.append("\"transfer_scene_report_infos\":[");
            sb.append("{\"info_type\":\"活动名称\",\"info_content\":\"事故上报奖励\"},");
            sb.append("{\"info_type\":\"奖励说明\",\"info_content\":\"事故上报红包奖励\"}");
            sb.append("]");
            sb.append("}");
            String jsonBody = sb.toString();

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
                return TransferResult.fail("EMPTY_RESPONSE", "响应体为空");
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
                return TransferResult.fail("NO_PACKAGE_INFO", "响应缺少package_info");
            }

            log.info("V3商家转账成功 accid={} outBillNo={} packageInfo={}", accid, outBillNo, packageInfo);
            return TransferResult.ok(packageInfo, outBillNo);
        } catch (com.wechat.pay.java.core.exception.ServiceException se) {
            String errorBody = se.getMessage();
            String failCode = "UNKNOWN";
            String failMessage = errorBody;
            // 解析错误码: httpResponseBody[{"code":"NOTENOUGH","message":"..."}]
            if (errorBody != null && errorBody.contains("\"code\"")) {
                try {
                    int codeIdx = errorBody.indexOf("\"code\"");
                    int colon = errorBody.indexOf(":", codeIdx);
                    int q1 = errorBody.indexOf("\"", colon + 1);
                    int q2 = errorBody.indexOf("\"", q1 + 1);
                    if (q1 >= 0 && q2 > q1) {
                        failCode = errorBody.substring(q1 + 1, q2);
                    }
                    int msgIdx = errorBody.indexOf("\"message\"");
                    if (msgIdx >= 0) {
                        int mc = errorBody.indexOf(":", msgIdx);
                        int mq1 = errorBody.indexOf("\"", mc + 1);
                        int mq2 = errorBody.indexOf("\"", mq1 + 1);
                        if (mq1 >= 0 && mq2 > mq1) {
                            failMessage = errorBody.substring(mq1 + 1, mq2);
                        }
                    }
                } catch (Exception parseEx) {
                    log.warn("解析V3错误码失败", parseEx);
                }
            }
            log.error("V3商家转账失败 accid={} openid={} failCode={} failMessage={}", accid, openid, failCode, failMessage, se);
            return TransferResult.fail(failCode, failMessage);
        } catch (Exception e) {
            log.error("V3商家转账失败 accid={} openid={} error={}", accid, openid, e.getMessage(), e);
            return TransferResult.fail("SYSTEM_ERROR", e.getMessage());
        }
    }

    /**
     * 下载资金账单，解析日终余额
     * @param billDate 账单日期，格式 yyyy-MM-dd
     * @return 日终余额（分），失败返回 -1
     */
    public long downloadFundFlowBill(String billDate) {
        System.out.println("========== downloadFundFlowBill 开始 billDate=" + billDate + " ==========");
        if (httpClient == null) {
            System.out.println("httpClient为null，无法下载资金账单");
            log.error("V3商家转账未初始化，无法下载资金账单");
            return -1;
        }
        try {
            String url = "https://api.mch.weixin.qq.com/v3/bill/fundflowbill?bill_date=" + billDate + "&account_type=OPERATION";
            System.out.println("请求URL: " + url);

            HttpHeaders headers = new HttpHeaders();
            headers.addHeader("Accept", "application/json");

            HttpRequest request = new HttpRequest.Builder()
                .httpMethod(HttpMethod.GET)
                .url(url)
                .headers(headers)
                .build();

            System.out.println("开始执行HTTP请求...");
            log.info("下载资金账单请求 billDate={} url={}", billDate, url);
            HttpResponse<JsonResponseBody> response = null;
            try {
                response = httpClient.execute(request, JsonResponseBody.class);
                log.info("HTTP请求执行成功 billDate={}", billDate);
            } catch (com.wechat.pay.java.core.exception.ServiceException se) {
                log.error("HTTP请求失败(ServiceException) billDate={} httpStatusCode={} errorCode={} errorMessage={} responseBody={}",
                    billDate, se.getHttpStatusCode(), se.getErrorCode(), se.getErrorMessage(), se.getResponseBody(), se);
                throw se;
            } catch (Exception ex) {
                log.error("HTTP请求执行失败 billDate={} type={} message={}", billDate, ex.getClass().getName(), ex.getMessage(), ex);
                throw ex;
            }

            String bodyStr = "";
            com.wechat.pay.java.core.http.ResponseBody rawBody = response.getBody();
            if (rawBody instanceof JsonResponseBody) {
                String raw = ((JsonResponseBody) rawBody).getBody();
                if (raw != null && !raw.isEmpty()) {
                    bodyStr = raw;
                }
            }
            log.info("资金账单响应 body={}", bodyStr);

            // 解析 download_url
            String downloadUrl = null;
            if (bodyStr.contains("\"download_url\"")) {
                int idx = bodyStr.indexOf("\"download_url\"");
                int colon = bodyStr.indexOf(":", idx);
                int q1 = bodyStr.indexOf("\"", colon + 1);
                int q2 = bodyStr.indexOf("\"", q1 + 1);
                if (q1 >= 0 && q2 > q1) {
                    downloadUrl = bodyStr.substring(q1 + 1, q2);
                }
            }

            if (downloadUrl == null || downloadUrl.isEmpty()) {
                log.error("资金账单响应缺少 download_url billDate={}", billDate);
                return -1;
            }

            // 下载 CSV（gzip）
            HttpRequest downloadRequest = new HttpRequest.Builder()
                .httpMethod(HttpMethod.GET)
                .url(downloadUrl)
                .headers(headers)
                .build();

            HttpResponse<JsonResponseBody> csvResponse = httpClient.execute(downloadRequest, JsonResponseBody.class);
            String csvBody = "";
            com.wechat.pay.java.core.http.ResponseBody csvRawBody = csvResponse.getBody();
            if (csvRawBody instanceof JsonResponseBody) {
                String raw = ((JsonResponseBody) csvRawBody).getBody();
                if (raw != null && !raw.isEmpty()) {
                    csvBody = raw;
                }
            }

            // 解析 CSV 获取最后一行的"账户余额"
            // 微信资金账单 CSV 格式：每行用逗号分隔，最后一列是"账户余额"
            String[] lines = csvBody.split("\n");
            String lastDataLine = null;
            for (int i = lines.length - 1; i >= 0; i--) {
                String line = lines[i].trim();
                if (line.startsWith("`")) {  // 微信 CSV 数据行以 ` 开头
                    lastDataLine = line;
                    break;
                }
            }

            if (lastDataLine == null) {
                log.error("资金账单 CSV 无法找到数据行 billDate={}", billDate);
                return -1;
            }

            // CSV 字段用 `,` 分隔，最后一个字段是账户余额（格式 `123.45`）
            String[] fields = lastDataLine.split(",");
            String balanceField = fields[fields.length - 1].replace("`", "").trim();
            BigDecimal balanceYuan = new BigDecimal(balanceField);
            long balanceFen = balanceYuan.multiply(new BigDecimal("100")).longValue();

            log.info("资金账单解析成功 billDate={} balance={} 分", billDate, balanceFen);
            return balanceFen;
        } catch (com.wechat.pay.java.core.exception.ServiceException se) {
            log.error("下载资金账单失败 ServiceException billDate={} httpStatusCode={} errorCode={} errorMessage={}",
                billDate, se.getHttpStatusCode(), se.getErrorCode(), se.getErrorMessage(), se);
            return -1;
        } catch (Exception e) {
            log.error("下载资金账单失败 Exception billDate={} type={} message={}",
                billDate, e.getClass().getName(), e.getMessage(), e);
            return -1;
        }
    }
}

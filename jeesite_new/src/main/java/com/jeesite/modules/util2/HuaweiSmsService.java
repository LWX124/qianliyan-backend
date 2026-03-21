package com.jeesite.modules.util2;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Base64;

@Service
public class HuaweiSmsService {

    private final static Logger logger = LoggerFactory.getLogger(HuaweiSmsService.class);

    //无需修改,用于格式化鉴权头域,给"X-WSSE"参数赋值
    private static final String WSSE_HEADER_FORMAT = "UsernameToken Username=\"%s\",PasswordDigest=\"%s\",Nonce=\"%s\",Created=\"%s\"";
    //无需修改,用于格式化鉴权头域,给"Authorization"参数赋值
    private static final String AUTH_HEADER_VALUE = "WSSE realm=\"SDP\",profile=\"UsernameToken\",type=\"Appkey\"";

    private static String url = "https://api.rtc.huaweicloud.com:10443/sms/batchSendSms/v1"; //APP接入地址+接口访问URI
    //必填,请参考"开发准备"获取如下数据,替换为实际值
    private static String appKey = "5PB7cH7nydS90BqKqfH1TqEHHhE5"; //APP_Ke
    private static String appSecret = "Ee86yvaul53q5jvV21J22Jg3la2E"; //APP_Secret
    private static String sender = "8819032763192"; //国内短信签名通道号或国际/港澳台短信通道号
    private static String templateId = "d6000df44cf4463e89f7bc6673b561e5"; //模板ID


    public void sendSmsByTemplate(String flag, String phone, String smsCode) {

        //条件必填,国内短信关注,当templateId指定的模板类型为通用模板时生效且必填,必须是已审核通过的,与模板类型一致的签名名称
        //国际/港澳台短信不用关注该参数
        String signature = null; //签名名称
        switch (flag) {
            case "1":
                //注册
//                templateId = "d51cba5eb2054c30b6bbd5b8913f03c5";
                templateId = "77d0d8e293904549ab9afecbb90d4f94";
                sender = "8819032763192";
                break;
            case "2":
                //修改密码
                templateId = "879f7bc965d94f1f8eb80a2cda09da22";
                sender = "8819032763192";
                break;
            case "3":
                //通知短信
                templateId = "0386623fb6a1458fadc55d65040a10ef";
                sender = "8819032763062";
                break;
            case "4":
                //通知短信
                templateId = "21bbe32cf97c4dcc9b09a22f1a4c7171";
                sender = "8819032763192";
                break;
            case "5":
                //通知短信
                templateId = "82e19d072f324ec2af9947a332f85632";
                sender = "8819032763192";
                break;
            case "6":
                //忘记密码
                templateId = "7ac3b5cbc10f49c699c71962f63aefd6";
                sender = "8819032763192";
                break;
            case "7":
                //通知
                templateId = "92711b244ff74931a57dc62fa2d33e8c";
                sender = "8819032763062";
                break;
            case "8":
                templateId = "433362fde035472e9960bf66dfb0efbf";
                sender = "8819032763062";
                break;
            case "9":
                templateId = "66eed63ce88744fe89d5436ed3b76238";
                sender = "8819032763062";
                break;
            case "10":
                templateId = "7fd21d586309453da7ebdc541c9e9b44";
                sender = "8819032763062";
                break;
            case "11":
                templateId = "173dfa5f82db44beb7a007878bc68531";
                sender = "8819032763062";
                break;
        }


        //必填,全局号码格式(包含国家码),示例:+8615123456789,多个号码之间用英文逗号分隔
        String receiver = "+86" + phone; //短信接收人号码

        //选填,短信状态报告接收地址,推荐使用域名,为空或者不填表示不接收状态报告
        String statusCallBack = "";

        /**
         * 选填,使用无变量模板时请赋空值 String templateParas = "";
         * 单变量模板示例:模板内容为"您的验证码是${NUM_6}"时,templateParas可填写为"[\"369751\"]"
         * 双变量模板示例:模板内容为"您有${NUM_2}件快递请到${TXT_32}领取"时,templateParas可填写为"[\"3\",\"人民公园正门\"]"
         * 查看更多模板变量规则:常见问题>业务规则>短信模板内容审核标准
         */
        String templateParas = "[\"" + smsCode + "\"]"; //模板变量

        if (flag.equals("7")) {
            templateParas = "";
        }

        //请求Body,不携带签名名称时,signature请填null
        String body = buildRequestBody(sender, receiver, templateId, templateParas, statusCallBack, signature);
        if (null == body || body.isEmpty()) {
            System.out.println("body is null.");
            return;
        }

        //请求Headers中的X-WSSE参数值
        String wsseHeader = buildWsseHeader(appKey, appSecret);
        if (null == wsseHeader || wsseHeader.isEmpty()) {
            System.out.println("wsse header is null.");
            return;
        }

        //如果JDK版本低于1.8,可使用如下代码
        //为防止因HTTPS证书认证失败造成API调用失败,需要先忽略证书信任问题
        //CloseableHttpClient client = HttpClients.custom()
        //        .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
        //            @Override
        //            public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        //                return true;
        //            }
        //        }).build()).setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();

        //如果JDK版本是1.8,可使用如下代码
        //为防止因HTTPS证书认证失败造成API调用失败,需要先忽略证书信任问题
        CloseableHttpClient client = null;
        try {
            client = HttpClients.custom()
                    .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null,
                            (x509CertChain, authType) -> true).build())
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        HttpResponse response = null;
        try {
            response = client.execute(RequestBuilder.create("POST")//请求方法POST
                    .setUri(url)
                    .addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                    .addHeader(HttpHeaders.AUTHORIZATION, AUTH_HEADER_VALUE)
                    .addHeader("X-WSSE", wsseHeader)
                    .setEntity(new StringEntity(body)).build());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(response.toString()); //打印响应头域信息
        try {
            logger.info("### 短信调用结果##  {}", response.getEntity());
            System.out.println(EntityUtils.toString(response.getEntity())); //打印响应消息实体
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 构造请求Body体
     *
     * @param sender
     * @param receiver
     * @param templateId
     * @param templateParas
     * @param statusCallbackUrl
     * @param signature         | 签名名称,使用国内短信通用模板时填写
     * @return
     */
    static String buildRequestBody(String sender, String receiver, String templateId, String templateParas,
                                   String statusCallbackUrl, String signature) {
        if (null == sender || null == receiver || null == templateId || sender.isEmpty() || receiver.isEmpty()
                || templateId.isEmpty()) {
            System.out.println("buildRequestBody(): sender, receiver or templateId is null.");
            return null;
        }
        List<NameValuePair> keyValues = new ArrayList<NameValuePair>();

        keyValues.add(new BasicNameValuePair("from", sender));
        keyValues.add(new BasicNameValuePair("to", receiver));
        keyValues.add(new BasicNameValuePair("templateId", templateId));
        if (null != templateParas && !templateParas.isEmpty()) {
            keyValues.add(new BasicNameValuePair("templateParas", templateParas));
        }
        if (null != statusCallbackUrl && !statusCallbackUrl.isEmpty()) {
            keyValues.add(new BasicNameValuePair("statusCallback", statusCallbackUrl));
        }
        if (null != signature && !signature.isEmpty()) {
            keyValues.add(new BasicNameValuePair("signature", signature));
        }

        return URLEncodedUtils.format(keyValues, Charset.forName("UTF-8"));
    }

    /**
     * 构造X-WSSE参数值
     *
     * @param appKey
     * @param appSecret
     * @return
     */
    static String buildWsseHeader(String appKey, String appSecret) {
        if (null == appKey || null == appSecret || appKey.isEmpty() || appSecret.isEmpty()) {
            System.out.println("buildWsseHeader(): appKey or appSecret is null.");
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String time = sdf.format(new Date()); //Created
        String nonce = UUID.randomUUID().toString().replace("-", ""); //Nonce

        byte[] passwordDigest = DigestUtils.sha256(nonce + time + appSecret);
        String hexDigest = Hex.encodeHexString(passwordDigest);

        //如果JDK版本是1.8,请加载原生Base64类,并使用如下代码
        String passwordDigestBase64Str = Base64.getEncoder().encodeToString(hexDigest.getBytes()); //PasswordDigest
        //如果JDK版本低于1.8,请加载三方库提供Base64类,并使用如下代码
        //String passwordDigestBase64Str = Base64.encodeBase64String(hexDigest.getBytes(Charset.forName("utf-8"))); //PasswordDigest
        //若passwordDigestBase64Str中包含换行符,请执行如下代码进行修正
        //passwordDigestBase64Str = passwordDigestBase64Str.replaceAll("[\\s*\t\n\r]", "");

        return String.format(WSSE_HEADER_FORMAT, appKey, passwordDigestBase64Str, nonce, time);
    }
}

//package com.stylefeng.guns.core.util;
//
//import com.stylefeng.guns.config.properties.WxPayProperties;
//import com.stylefeng.guns.modular.system.constant.WxPay.SendRedPack;
//import org.apache.commons.codec.digest.DigestUtils;
//import org.apache.http.HttpEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.ssl.SSLContexts;
//import org.apache.http.util.EntityUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.net.ssl.SSLContext;
//import java.io.*;
//import java.security.KeyStore;
//import java.text.DecimalFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Map;
//import java.util.UUID;
//import java.util.concurrent.atomic.AtomicInteger;
//
//@Component
//public class WxPayUtil {
//
//    @Autowired
//    WxPayProperties wxPayProperties;
//
//    private static final AtomicInteger atomicInteger = new AtomicInteger(0);
//
//    private static final String STR_FORMAT = "00000";
//
//    /**
//     * 发送现金红包 4      * @return String
//     * @throws
//     */
//    public Map<String, String> sendRedPack(String openId) throws Exception{
//        Date date = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//        String str = sdf.format(date);
//        SendRedPack sendRedPack = new SendRedPack();
//        sendRedPack.setNonce_str(UUID.randomUUID().toString());
//        sendRedPack.setMch_id(wxPayProperties.getMch_id());
//        sendRedPack.setMch_billno(wxPayProperties.getMch_id()+System.currentTimeMillis()+getRandomStr(atomicInteger.addAndGet(1)));
//        sendRedPack.setWxappid(wxPayProperties.getWxappid());
//        sendRedPack.setSend_name(wxPayProperties.getSend_name());
//        sendRedPack.setTotal_num(1);
//        sendRedPack.setAct_name("业务信息红包");
//        sendRedPack.setWishing("感谢您的支持");
//        sendRedPack.setRemark("");
//        sendRedPack.setClient_ip(wxPayProperties.getClient_ip());
//        sendRedPack.setRe_openid(openId);//红包发放用户openid
//        sendRedPack.setTotal_amount(5);//红包金额
//        String sign = createSendRedPackOrderSign(sendRedPack);
//        sendRedPack.setSign(sign);
//
//        XmlUtil xmlUtil= new XmlUtil();
//        xmlUtil.xstream().alias("xml", sendRedPack.getClass());
//        String xml = xmlUtil.xstream().toXML(sendRedPack);
//        String response = ssl(wxPayProperties.getApiUrl(), xml);
//        Map<String, String> map = xmlUtil.parseXml(response);
//        return map;
//    }
//
//
//    /**
//     * 生成签名
//     * */
//    public String createSendRedPackOrderSign(SendRedPack redPack){
//        StringBuffer sign = new StringBuffer();
//        sign.append("act_name=").append(redPack.getAct_name());
//        sign.append("&client_ip=").append(redPack.getClient_ip());
//        sign.append("&mch_billno=").append(redPack.getMch_billno());
//        sign.append("&mch_id=").append(redPack.getMch_id());
//        sign.append("&nonce_str=").append(redPack.getNonce_str());
//        sign.append("&re_openid=").append(redPack.getRe_openid());
//        sign.append("&remark=").append(redPack.getRemark());
//        sign.append("&send_name=").append(redPack.getSend_name());
//        sign.append("&total_amount=").append(redPack.getTotal_amount());
//        sign.append("&total_num=").append(redPack.getTotal_num());
//        sign.append("&wishing=").append(redPack.getWishing());
//        sign.append("&wxappid=").append(redPack.getWxappid());
//        sign.append("&key=").append(wxPayProperties.getApiKey());
//        return DigestUtils.md5Hex(sign.toString()).toUpperCase();
//    }
//
//    /**
//     * 发送请求
//     * */
//    public String ssl(String url,String data){
//        StringBuffer message = new StringBuffer();
//        try {
//            String mchId = wxPayProperties.getMch_id();
//            KeyStore keyStore  = KeyStore.getInstance("PKCS12");
//            String certFilePath = "D:/certs/apiclient_cert.p12";
//            // linux下
//            if ("/".equals(File.separator)) {
//                certFilePath = "//usr//local//certs//apiclient_cert.p12";
//            }
//            FileInputStream instream = new FileInputStream(new File(certFilePath));
//            keyStore.load(instream, mchId.toCharArray());
//            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mchId.toCharArray()).build();
//            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
//            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
//            HttpPost httpost = new HttpPost(url);
//            httpost.addHeader("Connection", "keep-alive");
//            httpost.addHeader("Accept", "*/*");
//            httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//            httpost.addHeader("Host", "api.mch.weixin.qq.com");
//            httpost.addHeader("X-Requested-With", "XMLHttpRequest");
//            httpost.addHeader("Cache-Control", "max-age=0");
//            httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
//            httpost.setEntity(new StringEntity(data, "UTF-8"));
//            System.out.println("executing request" + httpost.getRequestLine());
//            CloseableHttpResponse response = httpclient.execute(httpost);
//            try {
//                HttpEntity entity = response.getEntity();
//                System.out.println("----------------------------------------");
//                System.out.println(response.getStatusLine());
//                if (entity != null) {
//                    System.out.println("Response content length: " + entity.getContentLength());
//                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(),"UTF-8"));
//                    String text;
//                    while ((text = bufferedReader.readLine()) != null) {
//                        message.append(text);
//                    }
//                }
//                EntityUtils.consume(entity);
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                response.close();
//            }
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
//        return message.toString();
//    }
//
//    public static String getRandomStr(int num){
//        DecimalFormat df = new DecimalFormat(STR_FORMAT);
//        return df.format(num);
//    }
//}

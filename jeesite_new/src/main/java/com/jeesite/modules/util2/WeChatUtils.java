package com.jeesite.modules.util2;

import com.jeesite.modules.pojo2.RSAPublicKeyPojo;
import com.jeesite.modules.pojo2.WxPayFindResultPojo;
import com.jeesite.modules.pojo2.WxPayToBankPojo;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class WeChatUtils {

    public static String getOrderNo() {
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String str = simpleDateFormat.format(date);
        Random random = new Random();
        int rannum= (int)(random.nextDouble()*(99999-10000 + 1))+ 10000;// 获取5位随机数
        System.out.println(rannum);
        return str + rannum;// 当前时间 + 系统5随机生成位数
    }

    /**
     * 扩展xstream,使其支持name带有"_"的节点
     */
    public static XStream xStream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));

    /**
     * 请求参数转换成xml
     *
     * @param data
     * @return xml字符串
     */
    public static String sendDataToXml(WxPayFindResultPojo data) {
        xStream.autodetectAnnotations(true);
        xStream.alias("xml", WxPayFindResultPojo.class);
        return xStream.toXML(data);
    }

    /**
     * 请求参数转换成xml
     *
     * @param data
     * @return xml字符串
     */
    public static String sendDataToXml(WxPayToBankPojo data) {
        xStream.autodetectAnnotations(true);
        xStream.alias("xml", WxPayToBankPojo.class);
        return xStream.toXML(data);
    }

    /**
     * 请求参数转换成xml
     *
     * @param data
     * @return xml字符串
     */
    public static String sendDataToXml(RSAPublicKeyPojo data) {
        xStream.autodetectAnnotations(true);
        xStream.alias("xml", RSAPublicKeyPojo.class);
        return xStream.toXML(data);
    }

    /**
     * 获取付款单号
     *
     * @return
     */
    public static String getDivedeOrderNo() {
        return "BANK" + getOrderNo();
    }

    /**
     * 获取指定长度的随机字符串
     *
     * @param length
     * @return 随机字符串
     */
    public static String getRandomStr(int length) {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }


    /**
     * 使用 HMACSHA256 加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String HMACSHA256(String data, String key) throws Exception {

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");

        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");

        sha256_HMAC.init(secret_key);

        byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));

        StringBuilder sb = new StringBuilder();

        for (byte item : array) {

            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));

        }

        return sb.toString().toUpperCase();

    }

}

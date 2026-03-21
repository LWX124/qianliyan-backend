package com.jeesite.modules.app.service;

import com.jeesite.modules.util2.Base64;
import com.jeesite.modules.util2.RSAUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.PublicKey;

/**
 * 企业付款到银行卡service
 */
@Service
@Transactional(readOnly = true)
public class WxPayBankService {

    private static Logger logger = LoggerFactory.getLogger(WxPayBankService.class);

    @Value("${wx.mchId}")
    private String mchId;

    @Value("${publicKey}")
    private String publicKey;

    @Value("${apiclientCert}")
    private String apiclientCert;

    public static void main(String[] args) throws Exception {
        //需要被加密的字符串
        String encBankAcctName = "小郑"; //加密的银行账户名
        //定义自己公钥的路径
        String keyfile = "C:/book/pksc8_public.pem"; //
        //RSA工具类提供了，根据加载PKCS8密钥文件的方法
        PublicKey pub= RSAUtil.getPubKey(keyfile,"RSA");
        //rsa是微信付款到银行卡要求我们填充的字符串
        String rsa ="RSA/ECB/OAEPWITHSHA-1ANDMGF1PADDING";
        // 进行加密
        byte[] estr=RSAUtil.encrypt(encBankAcctName.getBytes(),pub,2048, 11,rsa);   //对银行账号进行base加密
        encBankAcctName = Base64.encode(estr);//并转为base64格式
        //测试输出
        System.out.println(encBankAcctName);
    }
//    //生成获取rsa公钥的签名
//    public static String createGetPublicKeySign(RSAPublicKeyBean bean) {
//        StringBuffer sign = new StringBuffer();
//        sign.append("mch_id=").append(bean.getMch_id());
//        sign.append("&nonce_str=").append(bean.getNonce_str());
//        sign.append("&sign_type=").append(bean.getSign_type());
//        sign.append("&key=").append("Ashes");
//        System.out.println("获取公钥签名参数：" + sign.toString());
//        String signStr = Md5Util.getInstance().getLongToken(sign.toString()).toUpperCase();
//        System.out.println("获取公钥签名：" + signStr);
//        return signStr;
//    }
//
//
//    public void getPublicKey() throws Exception {
//        //1.0 拼凑所需要传递的参数 map集合
//        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
//        String nonce_str = RandomStringUtils.random(28);
//        parameters.put("mch_id", mchId);
//        parameters.put("nonce_str", nonce_str);
//        parameters.put("sign_type", "MD5");
//        //2.0 根据要传递的参数生成自己的签名~~注意creatSign是自己封装的一个类。大家可以在下面自主下载
//        String sign = SignUtils.creatSign(parameters);
//        System.out.println(sign);
//        //3.0 把签名放到map集合中【因为签名也要传递过去】
//        parameters.put("sign", sign);
//        //4.0将当前的map结合转化成xml格式
//        String reuqestXml = WXPayUtil.getRequestXml(parameters);
//        //5.0 发送请求到微信请求公钥Api。发送请求是一个方法来的~~注意需要带着证书哦
//        String xml1 = HttpClientCustomSSL.httpClientResultGetPublicKey(reuqestXml);
//        //6.0 解析返回的xml数据===》map集合
//        String publicKey = XMLUtils.Progress_resultParseXml(xml1);
//        System.out.println(publicKey);
//    }

}

package com.stylefeng.guns.modular.system.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @program: weg
 * @description: token验证工具
 * @author: lvyq
 * @create: 2023-03-07 14:55
 **/
public class TokenCheckUtils {


      public static boolean checkToken(HttpServletResponse response, HttpServletRequest request, String token) throws NoSuchAlgorithmException, IOException, IOException {
        String method = request.getMethod();
        String signature = request.getParameter("signature");
        String echostr = request.getParameter("echostr");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String[] str = new String[]{token, timestamp, nonce};
        Arrays.sort(str);
        String bigStr = str[0] + str[1] + str[2];
        String digest = sha1(bigStr);
        if (digest.equals(signature)) {
            if ("GET".equals(method)) {
                response.getWriter().print(echostr);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * @description: sha1
     * @author: lvyq
     * @date: 2022/7/8 15:18
     * @version 1.0
     */

    public static String sha1(String data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        //把字符串转为字节数组
        byte[] b = data.getBytes();
        //使用指定的字节来更新我们的摘要
        md.update(b);
        //获取密文  （完成摘要计算）
        byte[] b2 = md.digest();
        //获取计算的长度
        int len = b2.length;
        //16进制字符串
        String str = "0123456789abcdef";
        //把字符串转为字符串数组
        char[] ch = str.toCharArray();
        //创建一个40位长度的字节数组
        char[] chs = new char[len*2];
        //循环20次
        for(int i=0,k=0;i<len;i++) {
            //获取摘要计算后的字节数组中的每个字节
            byte b3 = b2[i];
            // >>>:无符号右移
            // &:按位与
            //0xf:0-15的数字
            chs[k++] = ch[b3 >>> 4 & 0xf];
            chs[k++] = ch[b3 & 0xf];
        }
        //字符数组转为字符串
        return new String(chs);
    }
}
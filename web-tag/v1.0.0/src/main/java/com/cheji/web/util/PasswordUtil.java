package com.cheji.web.util;

import java.security.MessageDigest;
import java.util.Random;
import java.util.UUID;

public class PasswordUtil {

    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5",
            "6", "!", "#", "@", "a", "b", "c", "d", "*", "f", "g", "F"};

    private static final String algorithm = "sha-256";

    /**
     * 获得指定位数的随机数
     * @param iLength
     * @return
     */
    public static String getRandomCode(int iLength){
        try {
            String[] m_srcStr = new String[] { "012345678998765432", "123456789009876543" };

            int len = 6;
            if (iLength >= 3)
                len = iLength;
            Random m_rnd = new Random();
            byte[] m_b = new byte[len];
            m_rnd.nextBytes(m_b);
            String m_pwdStr = "";
            for (int i = 0; i < len; i++) {
                int startIdx = Math.abs((int) m_b[i] % 18);
                m_pwdStr += m_srcStr[i % 2].substring(startIdx, startIdx + 1);
            }

            return m_pwdStr;
        } catch (Exception ex) {
//	    	LogUtil.warn("生成随机密码时发生异常："+ex);

            throw new RuntimeException(ex);
        }
    }

    public static String encode(String rawPass, String salt) {
        String result = null;
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            // 加密后的字符串
            result = byteArrayToHexString(md.digest(mergePasswordAndSalt(
                    rawPass, salt).getBytes("utf-8")));
        } catch (Exception ex) {
        }
        return result;
    }

    /**
     * 验证两个密码是否相同
     *
     * @param encPass 数据库中的密码
     * @param rawPass 用户输入的密码
     * @param salt    盐
     * @return true 表示验证通过  false 表示验证不通过
     */
    public static boolean isPasswordValid(String encPass, String rawPass, String salt) {
        String pass1 = "" + encPass;
        String pass2 = encode(rawPass, salt);

        return pass1.equals(pass2);
    }

    private static String mergePasswordAndSalt(String password, String salt) {
        if (password == null) {
            password = "";
        }

        if ((salt == null) || "".equals(salt)) {
            return password;
        } else {
            return password + "{" + salt.toString() + "}";
        }
    }

    /**
     * 转换字节数组为16进制字串
     *
     * @param b 字节数组
     * @return 16进制字串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / hexDigits.length;
        int d2 = n % hexDigits.length;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * 生成盐
     * @return 盐
     */
    public static String generateSalt(){
        return UUID.randomUUID().toString();
    }

//    public static void main(String[] args) {
//        String salt = UUID.randomUUID().toString();
////        PasswordUtil encoderMd5 = new PasswordUtil("80cfdfb4-1237-4d75-ae18-1cce0c0861e5", "sha-256");
//        String encodedPassword = PasswordUtil.encode("12121212", salt);
//        System.out.println("加密后密码：" + encodedPassword + "\n密码长度：" + encodedPassword.length());
//        System.out.println("salt:" + salt);
//
//        System.out.println(PasswordUtil.isPasswordValid("060#1b*3#b043@4#c2aF4635@g45@gc*121b65343@4d21d2616a6*d@60c@a1c5", "121212", "3882b211-e94c-4474-955f-25f194218168"));
//
//    }


}

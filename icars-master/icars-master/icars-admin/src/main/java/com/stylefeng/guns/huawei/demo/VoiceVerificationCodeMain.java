package com.stylefeng.guns.huawei.demo;


import com.stylefeng.guns.huawei.utils.Constant;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class VoiceVerificationCodeMain {
    // 接口通用返回值
    private static String status = "";
    private static String resultcode = "";
    private static String resultdesc = "";
    // 鉴权接口返回值
    private static String access_token = "";
    private static String refresh_token = "";
    private static int expires_in = 0;
    // 语音验证码接口返回值
    private static String sessionId = "";

    // 计数器
    private static Timer timer = new Timer();
    private static int expires_count = 0;

    // 业务平台的账号和密码,实际开发时请替换取值
    public static String username = "CaaS_Test_01";
    public static String password = "CaaS2.0?";

    // 鉴权类实体
    public static Auth voiceVerificationCodeAuth = new Auth(Constant.CALLNOTIFYVERIFY_APPID,
            Constant.CALLNOTIFYVERIFY_SECRET);
    // 语音验证码业务类实体
    public static VoiceVerificationCode voiceVerificationCodeAPI = new VoiceVerificationCode();

    // 退出标识
    private static Boolean quit = false;
    // 调用接口成功标示
    private static final String success = "200";

    public static void main(String args[]) throws Exception {

        // 首先调用大客户SP简单认证API进行登录鉴权
        VoiceVerificationCodeMain.login();

        // 创建一个计时器对access_token的有效期（expires_in）进行倒计时,单位为秒.
        expires_count = expires_in;
        timer.schedule(new TimerTask() {
            public void run() {
                --expires_count;
            }
        }, 1000);

        /*
         * 当有效期还剩下1/4时,建议调用刷新授权API刷新token. 循环刷新token并等待调用点击呼叫接口直至退出
         */
        while (!quit) {
            if (expires_count < (expires_in / 4)) {
                // 调用刷新授权API刷新token
                VoiceVerificationCodeMain.refresh();
                // 刷新倒计时
                expires_count = expires_in;
            }

            // TODO 程序前端要求发起语音验证码呼叫,调用doVoiceVerificationCode方法.
            // 以下代码仅供调试使用,实际开发时请删除
            VoiceVerificationCodeMain.doVoiceVerificationCode("+8619983267209", 2, "test.wav", "1234");
            if (status.indexOf(success) != -1) {
                System.out.println(status);
                System.out.println(resultcode + " " + resultdesc);
                System.out.println("The session id is: " + sessionId);
            }

            // TODO 需要接收状态和话单时,请参考"呼叫状态和话单通知API"接口实现状态通知和话单的接收和解析

            // 需要退出循环时,调用quit方法.
            // 以下代码仅供调试使用,实际开发时请删除
//            VoiceVerificationCodeMain.quit();
        }

    }

    // 登录鉴权
    private static void login() throws Exception {

        Boolean retry = false;
        // 调用登录鉴权接口,直至成功
        do {
            status = voiceVerificationCodeAuth.fastlogin(username, password);
            if (status.indexOf(success) != -1) {
                retry = false;
                // 调用成功,记录token和有效期
                access_token = voiceVerificationCodeAuth.getResponsePara("access_token");
                refresh_token = voiceVerificationCodeAuth.getResponsePara("refresh_token");
                expires_in = Integer.parseInt(voiceVerificationCodeAuth.getResponsePara("expires_in"));
            } else {
                retry = true;
                // 调用失败,获取错误码和错误描述
                resultcode = voiceVerificationCodeAuth.getResponsePara("resultcode");
                resultdesc = voiceVerificationCodeAuth.getResponsePara("resultdesc");
                // 处理错误
                VoiceVerificationCodeMain.processError();
                // 每次重试之前等待70秒,避免接口返回登录太频繁的错误
                TimeUnit.SECONDS.sleep(70);
            }
        } while (retry);
    }

    // 刷新token
    private static void refresh() throws Exception {

        Boolean retry = false;
        // 调用刷新token接口,直至成功
        do {
            status = voiceVerificationCodeAuth.refresh(refresh_token);
            if (status.indexOf(success) != -1) {
                retry = false;
                // 调用成功,刷新token和有效期
                access_token = voiceVerificationCodeAuth.getResponsePara("access_token");
                refresh_token = voiceVerificationCodeAuth.getResponsePara("refresh_token");
                expires_in = Integer.parseInt(voiceVerificationCodeAuth.getResponsePara("expires_in"));
            } else {
                retry = true;
                // 调用失败,获取错误码和错误描述
                resultcode = voiceVerificationCodeAuth.getResponsePara("resultcode");
                resultdesc = voiceVerificationCodeAuth.getResponsePara("resultdesc");
                // 处理错误
                VoiceVerificationCodeMain.processError();
            }
        } while (retry);
    }

    // 前端需要退出等待调用循环时,调用此方法
    public static void quit() {
        quit = true;
        timer.cancel();
    }

    /*
     * 前端需要发起语音验证码呼叫时,调用此方法 该示例只仅体现必选参数,可选参数根据接口文档和实际情况配置.
     */
    public static void doVoiceVerificationCode(String calleeNbr, int languageType, String preTone, String verifyCode)
            throws Exception {

        Boolean retry = false;
        // 调用语音验证码接口,直至成功
        do {
            status = voiceVerificationCodeAPI.voiceVerificationCodeAPI(access_token, calleeNbr, languageType, preTone,
                    verifyCode);
            if (status.indexOf(success) != -1) {
                retry = false;
                // 调用成功,记录返回的信息.
                resultcode = voiceVerificationCodeAPI.getResponsePara("resultcode");
                resultdesc = voiceVerificationCodeAPI.getResponsePara("resultdesc");
                sessionId = voiceVerificationCodeAPI.getResponsePara("sessionId");
            } else {
                retry = true;
                // 调用失败,获取错误码和错误描述.
                resultcode = voiceVerificationCodeAPI.getResponsePara("resultcode");
                resultdesc = voiceVerificationCodeAPI.getResponsePara("resultdesc");
                // 处理错误
                VoiceVerificationCodeMain.processError();
            }
        } while (retry);
    }

    // 当API的返回值不是200时,处理错误.
    private static void processError() throws InterruptedException {

        // TODO 根据错误码和错误码描述处理问题
        // 以下代码仅供调试使用,实际开发时请删除
        System.out.println(status);
        System.out.println(resultcode + " " + resultdesc);
        System.exit(-1);
    }
}
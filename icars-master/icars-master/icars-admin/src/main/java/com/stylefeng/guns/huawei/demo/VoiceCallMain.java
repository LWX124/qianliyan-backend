package com.stylefeng.guns.huawei.demo;


import com.stylefeng.guns.huawei.utils.Constant;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class VoiceCallMain {
    // 接口通用返回值
    private static String status = "";
    private static String resultcode = "";
    private static String resultdesc = "";
    // 鉴权接口返回值
    private static String access_token = "";
    private static String refresh_token = "";
    private static int expires_in = 0;
    // 语音回呼接口返回值
    private static String sessionId = "";

    // 计数器
    private static Timer timer = new Timer();
    private static int expires_count = 0;

    public static String username = "CaaS_Test_01";
    public static String password = "CaaS2.0?";

    // 鉴权类实体
    public static Auth voiceCallAuth = new Auth(Constant.CLICK2CALL_APPID, Constant.CLICK2CALL_SECRET);
    // 语音回呼业务类实体
    public static VoiceCall voiceCallAPI = new VoiceCall();
    // 获取录音文件下载地址实体
    public static GetRecordLink recordLinkAPI = new GetRecordLink();

    // 退出标识
    private static Boolean quit = false;
    // 调用接口成功标识
    private static final String success = "200";

    public static void main(String args[]) throws Exception {

        // 首先调用大客户SP简单认证API进行登录鉴权
        VoiceCallMain.login();

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
                VoiceCallMain.refresh();
                // 刷新倒计时
                expires_count = expires_in;
            }

            // TODO 程序前端要求发起语音回呼,调用doVoiceCall方法.
            // 以下代码仅供调试使用,实际开发时请删除
            VoiceCallMain.doVoiceCall("+8613800000001", "+8613800000002");
            if (status.indexOf(success) != -1) {
                System.out.println(status);
                System.out.println(resultcode + " " + resultdesc);
                System.out.println("The session id is: " + sessionId);
            }

            // TODO 需要接收状态和话单时,请参考"呼叫状态和话单通知API"接口实现状态通知和话单的接收和解析.
            // HostingVoiceEventDemoImpl

            // TODO 需要下载录音文件时,请参照"获取录音文件下载地址API"接口获取录音文件下载地址.
            String code = recordLinkAPI.getRecordLinkAPI(access_token, "1200_366_0_20161228102743.wav", "ostor.huawei.com");
            if (code.indexOf("301") != -1) {
                System.out.println("The record file download link is: " + recordLinkAPI.getLocation());
            } else {
                System.out.println("code: " + code);
                System.out.println("Failed: " + recordLinkAPI.getResponsebody().toString());
            }

            // 需要退出循环时,调用quit方法.
            // 以下代码仅供调试使用,实际开发时请删除
            VoiceCallMain.quit();
        }
    }

    // 登录鉴权
    private static void login() throws Exception {

        Boolean retry = false;
        // 调用登录鉴权接口,直至成功
        do {
            status = voiceCallAuth.fastlogin(username, password);
            if (status.indexOf(success) != -1) {
                retry = false;
                // 调用成功,记录token和有效期
                access_token = voiceCallAuth.getResponsePara("access_token");
                refresh_token = voiceCallAuth.getResponsePara("refresh_token");
                expires_in = Integer.parseInt(voiceCallAuth.getResponsePara("expires_in"));
            } else {
                retry = true;
                // 调用失败,获取错误码和错误描述
                resultcode = voiceCallAuth.getResponsePara("resultcode");
                resultdesc = voiceCallAuth.getResponsePara("resultdesc");
                // 处理错误
                VoiceCallMain.processError();
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
            status = voiceCallAuth.refresh(refresh_token);
            if (status.indexOf(success) != -1) {
                retry = false;
                // 调用成功,刷新token和有效期
                access_token = voiceCallAuth.getResponsePara("access_token");
                refresh_token = voiceCallAuth.getResponsePara("refresh_token");
                expires_in = Integer.parseInt(voiceCallAuth.getResponsePara("expires_in"));
            } else {
                retry = true;
                // 调用失败,获取错误码和错误描述
                resultcode = voiceCallAuth.getResponsePara("resultcode");
                resultdesc = voiceCallAuth.getResponsePara("resultdesc");
                // 处理错误
                VoiceCallMain.processError();
            }
        } while (retry);
    }

    // 前端需要退出等待调用循环时,调用此方法
    public static void quit() {
        quit = true;
        timer.cancel();
    }

    /*
     * 前端需要发起语音回呼时,调用此方法 该示例只仅体现必选参数,可选参数根据接口文档和实际情况配置.
     */
    public static void doVoiceCall(String callerNbr, String calleeNbr) throws Exception {

        Boolean retry = false;
        // 调用语音回呼接口,直至成功
        do {
            status = voiceCallAPI.voiceCallAPI(access_token, callerNbr, calleeNbr);
            if (status.indexOf(success) != -1) {
                retry = false;
                // 调用成功,记录返回的信息.
                resultcode = voiceCallAPI.getResponsePara("resultcode");
                resultdesc = voiceCallAPI.getResponsePara("resultdesc");
                sessionId = voiceCallAPI.getResponsePara("sessionId");
            } else {
                retry = true;
                // 调用失败,获取错误码和错误描述.
                resultcode = voiceCallAPI.getResponsePara("resultcode");
                resultdesc = voiceCallAPI.getResponsePara("resultdesc");
                // 处理错误
                VoiceCallMain.processError();
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
package com.stylefeng.guns.huawei.demo;


import com.stylefeng.guns.config.properties.SysProperties;
import com.stylefeng.guns.huawei.utils.Constant;
import com.stylefeng.guns.modular.system.constant.SysActive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class CallNotifyMain implements ApplicationRunner {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    // 接口返回值
    private String status = "";
    private String resultcode = "";
    private String resultdesc = "";
    // 鉴权接口
    private String access_token = "";
    private String refresh_token = "";
    private int expires_in = 0;
    // 点击呼叫接口
    private String sessionId = "";

    // 有效期计数器
    private Timer timer = new Timer();
    private int expires_count = 0;

    // 业务平台的账号和密码,实际开发时请替换取值
    public String username = "CheJi";
    public String password = "CheJi@12";
    // 鉴权类实体
    public Auth callNotifyAuth = new Auth(Constant.CALLNOTIFYVERIFY_APPID, Constant.CALLNOTIFYVERIFY_SECRET);
    // 语音通知业务类实体
    public CallNotify callNotifyAPI = new CallNotify();

    // 退出标识
    private Boolean quit = false;
    // 调用接口成功标示
    private final String success = "200";

    @Resource
    private SysProperties sysProperties;

    public void sendVoice(String param, String phone) throws Exception {

        /*
         * 当有效期还剩下1/4时,建议调用刷新授权API刷新token. 循环刷新token并等待调用点击呼叫接口直至退出
         */
//        while (!quit) {
        if (expires_count < (expires_in / 4)) {
            // 调用刷新授权API刷新token
            if (sysProperties.getActive().equals(SysActive.PRO)) {
                refresh();
                //刷新倒计时
                expires_count = expires_in;
            }

        }

        // TODO 程序前端要求发起语音通知呼叫,首先使用getplayInfo构造构造playInfoList参数,然后调用doCallNotify方法.
        // 以下代码仅供调试使用,实际开发时请删除
        // 构造playInfoList参数
        List<Map<String, Object>> playInfoList = new ArrayList<Map<String, Object>>();
        // 使用音频文件作为第一段放音内容
//            playInfoList.add(callNotifyAPI.getplayInfo("test.wav"));
        // 使用v2.0版本接口的TTS模板作为第二段放音内容
        String templateId = "fb5e1e999b964f85819911c82a334dd5";
        List<String> templateParas = new ArrayList<String>();
        templateParas.add(param);

        playInfoList.add(callNotifyAPI.getplayInfo(templateId, templateParas));

        // 调用doCallNotify方法
//            CallNotifyMain.doCallNotify("+8678880005530", "+8618180765139", playInfoList);
        doCallNotify("+8678880005530", "+86" + phone, playInfoList);
        if (status.indexOf(success) != -1) {
            System.out.println(status + "8619983267209");
            System.out.println(resultcode + " " + resultdesc);
            System.out.println("The session id is: " + sessionId);
        }

        // TODO 需要接收状态和话单时,请参考"呼叫状态和话单通知API"接口实现状态通知和话单的接收和解析

        // 需要退出循环时,调用quit方法.
        // 以下代码仅供调试使用,实际开发时请删除
//            CallNotifyMain.quit();
//        }
    }

    // 登录鉴权
    private void login() throws Exception {

        Boolean retry = false;
        // 调用登录鉴权接口,直至成功
        do {
            status = callNotifyAuth.fastlogin(username, password);
            if (status.indexOf(success) != -1) {
                retry = false;
                // 调用成功,记录token和有效期
                access_token = callNotifyAuth.getResponsePara("access_token");
                refresh_token = callNotifyAuth.getResponsePara("refresh_token");
                expires_in = Integer.parseInt(callNotifyAuth.getResponsePara("expires_in"));
            } else {
                retry = true;
                // 调用失败,获取错误码和错误描述
                resultcode = callNotifyAuth.getResponsePara("resultcode");
                resultdesc = callNotifyAuth.getResponsePara("resultdesc");
                // 处理错误
                processError();
                // 每次重试之前等待70秒,避免接口返回登录太频繁的错误
                TimeUnit.SECONDS.sleep(70);
            }
        } while (retry);
    }

    // 刷新token
    private void refresh() throws Exception {

        Boolean retry = false;
        // 调用刷新token接口,直至成功

        status = callNotifyAuth.refresh(refresh_token);
        if (status.indexOf(success) != -1) {
            retry = false;
            // 调用成功,刷新token和有效期
            access_token = callNotifyAuth.getResponsePara("access_token");
            refresh_token = callNotifyAuth.getResponsePara("refresh_token");
            expires_in = Integer.parseInt(callNotifyAuth.getResponsePara("expires_in"));
        } else {
            retry = true;
            // 调用失败,获取错误码和错误描述
            resultcode = callNotifyAuth.getResponsePara("resultcode");
            resultdesc = callNotifyAuth.getResponsePara("resultdesc");
            // 处理错误
            processError();
        }

    }

    // 前端需要退出等待调用循环时,调用此方法
    public void quit() {
        quit = true;
        timer.cancel();
    }

    /*
     * 前端需要发起语音通知呼叫时,调用此方法 该示例只仅体现必选参数,可选参数根据接口文档和实际情况配置.
     */
    public void doCallNotify(String bindNbr, String calleeNbr, List<Map<String, Object>> playInfoList)
            throws Exception {

        Boolean retry = false;
        // 调用语音通知接口,直至成功
//        do {

        status = callNotifyAPI.callNotifyAPI(access_token, bindNbr, calleeNbr, playInfoList);

        if (status.indexOf(success) != -1) {
            retry = false;
            // 调用成功,记录返回的信息.
            resultcode = callNotifyAPI.getResponsePara("resultcode");
            resultdesc = callNotifyAPI.getResponsePara("resultdesc");
            sessionId = callNotifyAPI.getResponsePara("sessionId");
        } else {
            retry = true;
            // 调用失败,获取错误码和错误描述.
            resultcode = callNotifyAPI.getResponsePara("resultcode");
            resultdesc = callNotifyAPI.getResponsePara("resultdesc");
            // 处理错误
            processError();
        }
//        } while (retry);
    }

    // 当API的返回值不是200时,处理错误.
    private void processError() throws InterruptedException {

        // TODO 根据错误码和错误码描述处理问题
        // 以下代码仅供调试使用,实际开发时请删除
        System.out.println(status);
        System.out.println(resultcode + " " + resultdesc);
//        System.exit(-1);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 首先调用大客户SP简单认证API进行登录鉴权
        try {
            if (sysProperties.getActive().equals(SysActive.PRO)) {
                System.out.println("开始登陆");

                //生产环境才获取华为token
                login();

                // 创建一个计时器对access_token的有效期（expires_in）进行倒计时,单位为秒.
                expires_count = expires_in;


                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    public void run() {
                        --expires_count;
                    }
                };
                timer.schedule(task, 1000L, 1000L);


                System.out.println("登陆成功expires_count" + expires_count + "登陆成功expires_in" + expires_in);
            } else {
                System.out.println("####" + sysProperties.getActive() + "###不需要登录华为云短信语音服务####");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
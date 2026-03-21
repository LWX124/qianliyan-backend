package com.jeesite.modules.app.service;
import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

@Component
public class ihuyiService {

    private static String Url = "http://api.vm.ihuyi.com/webservice/voice.php?method=Submit";

    public void ihuyiCall(String phone){

        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(Url);

        //client.getParams().setContentCharset("GBK");
        client.getParams().setContentCharset("UTF-8");
        method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=UTF-8");

        NameValuePair[] data = {//提交短信
                new NameValuePair("account", "VM80468860"),//用户名是登录用户中心->语音通知->帐户参数设置->APIID
                new NameValuePair("password", "a058b9fd6ba691c65727719c4639bb19"),//查看密码请登录用户中心->语音通知->帐户参数设置->APIKEY
                new NameValuePair("mobile", phone),//手机号码
                new NameValuePair("content", "您有新的消息，请注意查收"),
        };

        method.setRequestBody(data);

        try {
            client.executeMethod(method);

            String SubmitResult = method.getResponseBodyAsString();

            //System.out.println(SubmitResult);

            Document doc = DocumentHelper.parseText(SubmitResult);
            Element root = doc.getRootElement();

            String code = root.elementText("code");
            String msg = root.elementText("msg");
            String voiceid = root.elementText("voiceid");

//            System.out.println(code);
//            System.out.println(msg);
//            System.out.println(voiceid);

            if("2".equals(code)){
                System.out.println("短信提交成功");
            }

        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }



}

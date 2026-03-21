package com.stylefeng.guns.wxpay;


import org.springframework.util.ResourceUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
public class MyConfig extends WXPayConfig {

    private String appid;

    private String mchid;

    private String key;

    private String certPath;

    private byte[] certData;

    public MyConfig(String appid, String mchid, String key, String certPath) throws Exception {
        this.appid = appid;
        this.mchid = mchid;
        this.key = key;
        this.certPath = certPath;

//        File file = ResourceUtils.getFile(this.certPath);
        File file = new File(this.certPath);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    String getAppID() {
        return this.appid;
    }

    String getMchID() {
        return this.mchid;
    }

    String getKey() {
        return this.key;
    }

    InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    IWXPayDomain getWXPayDomain() {
        return new Domain();
    }
}

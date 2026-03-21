package com.stylefeng.guns.modular.system.vo;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class WxUserInfoVo implements Serializable {

    @NotNull(message = "encryptedData不能为空！")
    private String encryptedData;

    @NotNull(message = "iv不能为空！")
    private String iv;
    @NotNull(message = "sessionId不能为空！")
    private String sessionId;

    public String getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}

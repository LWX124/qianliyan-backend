package com.jeesite.modules.constant2;

/**
 * 常量
 */
public enum JgTokenEnum {
    C("efafe1bd46af9d68a2e67391", "ae0dffd22327afa9c9949bc2"),
    B("284ba1bcf05ba69d03be04e3", "ed8d323840605d1483b6f645");

    private String appKey ;
    private String masterSecret;

    JgTokenEnum(String appKey, String masterSecret) {
        this.appKey = appKey;
        this.masterSecret = masterSecret;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getMasterSecret() {
        return masterSecret;
    }

    public void setMasterSecret(String masterSecret) {
        this.masterSecret = masterSecret;
    }
}

package com.stylefeng.guns.modular.system.constant;

import com.stylefeng.guns.modular.system.model.BizWxUser;
import com.stylefeng.guns.modular.system.model.User;

public class WxSession {

    private String openId;

    private String wxSessionKey;

    private long expires;

    private long createSeconds;

    private String  unionId;

    private BizWxUser bizWxUser;

    private User user;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getWxSessionKey() {
        return wxSessionKey;
    }

    public void setWxSessionKey(String wxSessionKey) {
        this.wxSessionKey = wxSessionKey;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }

    public long getCreateSeconds() {
        return createSeconds;
    }

    public void setCreateSeconds(long createSeconds) {
        this.createSeconds = createSeconds;
    }

    public BizWxUser getBizWxUser() {
        return bizWxUser;
    }

    public void setBizWxUser(BizWxUser bizWxUser) {
        this.bizWxUser = bizWxUser;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public WxSession(String openId, String wxSessionKey, long expires, long createSeconds, BizWxUser bizWxUser ,User user,String unionId) {
        this.openId = openId;
        this.wxSessionKey = wxSessionKey;
        this.expires = expires;
        this.createSeconds = createSeconds;
        this.unionId = unionId;
        this.bizWxUser = bizWxUser;
        this.user = user;
    }

    @Override
    public String toString() {
        return "WxSession{" +
                "openId='" + openId + '\'' +
                ", wxSessionKey='" + wxSessionKey + '\'' +
                ", expires=" + expires +
                ", createSeconds=" + createSeconds +
                ", bizWxUser=" + bizWxUser +
                ", user=" + user +
                '}';
    }
}

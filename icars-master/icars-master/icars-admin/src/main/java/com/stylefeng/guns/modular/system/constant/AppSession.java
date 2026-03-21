package com.stylefeng.guns.modular.system.constant;

import com.stylefeng.guns.modular.system.model.User;

public class AppSession {



    private long expires;

    private long createSeconds;


    private User user;



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



    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }



    public AppSession(long expires, long createSeconds, User user) {
        this.expires = expires;
        this.createSeconds = createSeconds;
        this.user = user;
    }


    public AppSession() {
    }

}

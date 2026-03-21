package com.jeesite.modules.app.entity;

public class Https {
    private String token;

    private String alias_value;//别名值

    private String registration_id;


    public String getToken() {
        return token;
    }


    public void setToken(String token) {
        this.token = token;
    }


    public String getAlias_value() {
        return alias_value;
    }


    public void setAlias_value(String alias_value) {
        this.alias_value = alias_value;
    }


    public String getRegistration_id() {
        return registration_id;
    }


    public void setRegistration_id(String registration_id) {
        this.registration_id = registration_id;
    }
}

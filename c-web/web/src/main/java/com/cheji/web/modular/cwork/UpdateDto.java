package com.cheji.web.modular.cwork;

public class UpdateDto {
    private String url;
    private String describe;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    @Override
    public String toString() {
        return "UpdateDto{" +
                "url='" + url + '\'' +
                ", describe='" + describe + '\'' +
                '}';
    }
}

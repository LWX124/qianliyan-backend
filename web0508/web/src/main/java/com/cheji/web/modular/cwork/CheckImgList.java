package com.cheji.web.modular.cwork;

public class CheckImgList {
    private String url;
    private Integer type;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "CheckImgList{" +
                "url='" + url + '\'' +
                ", type=" + type +
                '}';
    }
}

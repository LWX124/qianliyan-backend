package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.annotations.TableName;

@TableName("app_version")
public class AppVersionEntity {
    private String id;
    private Integer versionNumMin;
    private Integer versionNumMax;
    private Integer type; //'1:c端   2：b端',
    private String versionName;
    private String info;
    private String href;

    @Override
    public String toString() {
        return "AppVersionEntity{" +
                "id='" + id + '\'' +
                ", versionNumMin=" + versionNumMin +
                ", versionNumMax=" + versionNumMax +
                ", type=" + type +
                ", versionName='" + versionName + '\'' +
                ", info='" + info + '\'' +
                ", href='" + href + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getVersionNumMin() {
        return versionNumMin;
    }

    public void setVersionNumMin(Integer versionNumMin) {
        this.versionNumMin = versionNumMin;
    }

    public Integer getVersionNumMax() {
        return versionNumMax;
    }

    public void setVersionNumMax(Integer versionNumMax) {
        this.versionNumMax = versionNumMax;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}

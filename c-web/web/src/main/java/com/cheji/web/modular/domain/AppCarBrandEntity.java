package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 * 汽车品牌
 * </p>
 *
 * @author Ashes
 * @since 2019-08-23
 */
@TableName("app_car_brand")
public class AppCarBrandEntity{

    private Integer id;
    private String initials;
    private String name;
    private String picUrl;
    private int status;

    @Override
    public String toString() {
        return "AppCarBrandEntity{" +
                "id=" + id +
                ", initials='" + initials + '\'' +
                ", name='" + name + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", status=" + status +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

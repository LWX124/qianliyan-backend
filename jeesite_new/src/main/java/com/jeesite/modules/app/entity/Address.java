package com.jeesite.modules.app.entity;

import io.swagger.annotations.ApiModelProperty;

public class Address {
    @ApiModelProperty("国家")
    private String country;

    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("区编码")
    private String adcode;

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("区")
    private String district;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }


    @Override
    public String toString() {
        return "Address{" +
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", adcode='" + adcode + '\'' +
                ", province='" + province + '\'' +
                ", district='" + district + '\'' +
                '}';
    }
}

package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

@TableName("app_wx_bank")
public class AppWxBankEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String bankName;
    private String bankCode;
    private String iconUrl;

    @Override
    public String toString() {
        return "AppWxBankEntity{" +
                "id=" + id +
                ", bankName='" + bankName + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                '}';
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
}

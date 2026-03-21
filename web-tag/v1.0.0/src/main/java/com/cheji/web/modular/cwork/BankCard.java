package com.cheji.web.modular.cwork;

//银行卡列表打工类
public class BankCard {
    private String id;      //银行卡id
    private String bankName;    //银行名称
    private String code;    //银行卡号
    private String tail;    //手机尾号


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTail() {
        return tail;
    }

    public void setTail(String tail) {
        this.tail = tail;
    }


    @Override
    public String toString() {
        return "BankCard{" +
                "id='" + id + '\'' +
                ", bankName='" + bankName + '\'' +
                ", code='" + code + '\'' +
                ", tail='" + tail + '\'' +
                '}';
    }
}

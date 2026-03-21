package com.cheji.web.modular.cwork;

//事故列表
public class AccidentList {
    public String id;
    public String address;      //地址
    public String thumbnailUrl; //缩略图
    public String payAmount;    //支付金额
    public String  createDate;

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "AccidentList{" +
                "id='" + id + '\'' +
                ", address='" + address + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", payAmount='" + payAmount + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}

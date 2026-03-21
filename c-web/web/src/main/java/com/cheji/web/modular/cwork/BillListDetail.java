package com.cheji.web.modular.cwork;

//账单列表
public class BillListDetail {
    private String id;      //账单id
    private String address; //事故地址
    private String createTime;  //事故时间
    private String money;       //支出或者收入
    private String  thumbnailUrl;   //缩略图

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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    @Override
    public String toString() {
        return "BillListDetail{" +
                "id='" + id + '\'' +
                ", address='" + address + '\'' +
                ", createTime='" + createTime + '\'' +
                ", money='" + money + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                '}';
    }
}

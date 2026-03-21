package com.jeesite.modules.constant2;

public enum PicTypeEnum {

    PartnerCommentImgType(1, "商户评论图"),
    OrderImgType(2, "订单资料图");

    private Integer type;
    private String info;

    PicTypeEnum(int type,String info){
        this.type = type;
        this.info = info;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}

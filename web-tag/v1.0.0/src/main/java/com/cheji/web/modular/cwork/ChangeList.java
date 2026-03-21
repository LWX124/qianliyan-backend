package com.cheji.web.modular.cwork;

//账单列表打工类
public class ChangeList {
    private String id;      //账单id
    private String partnerTradeNo;  //编号
    private String workName;    //操作名称
    private String amount;      //操作金额
    private String createTime;  //创建时间

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPartnerTradeNo() {
        return partnerTradeNo;
    }

    public void setPartnerTradeNo(String partnerTradeNo) {
        this.partnerTradeNo = partnerTradeNo;
    }

    @Override
    public String toString() {
        return "ChangeList{" +
                "id='" + id + '\'' +
                ", partnerTradeNo='" + partnerTradeNo + '\'' +
                ", workName='" + workName + '\'' +
                ", amount='" + amount + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}

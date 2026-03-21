package com.stylefeng.guns.alipay;

public class AlipayRequestEntity {
    /**
     * 生成支付订单号
     */
    private String out_biz_no;
    /**
     * 支付支付宝账户
     */
    private String payee_account;
    /**
     * 支付金额
     */
    private Integer amount;
    /**
     * 支付成功返回订单号
     */
    private String orderId;
    /**
     * 支付时间
     */
    private String payDate;

    public String getOut_biz_no() {
        return out_biz_no;
    }

    public void setOut_biz_no(String out_biz_no) {
        this.out_biz_no = out_biz_no;
    }

    public String getPayee_account() {
        return payee_account;
    }

    public void setPayee_account(String payee_account) {
        this.payee_account = payee_account;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    @Override
    public String toString() {
        return "AlipayRequestEntity{" +
                "out_biz_no='" + out_biz_no + '\'' +
                ", payee_account='" + payee_account + '\'' +
                ", amount=" + amount +
                ", orderId='" + orderId + '\'' +
                ", payDate='" + payDate + '\'' +
                '}';
    }
}

package com.stylefeng.guns.wxpay;

/**
 * V3商家转账结果
 */
public class TransferResult {

    private boolean success;
    private String packageInfo;
    private String outBillNo;

    public static TransferResult fail() {
        TransferResult r = new TransferResult();
        r.success = false;
        return r;
    }

    public static TransferResult ok(String packageInfo, String outBillNo) {
        TransferResult r = new TransferResult();
        r.success = true;
        r.packageInfo = packageInfo;
        r.outBillNo = outBillNo;
        return r;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getPackageInfo() {
        return packageInfo;
    }

    public String getOutBillNo() {
        return outBillNo;
    }
}

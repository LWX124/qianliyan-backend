/**
 * 初始化coupon详情对话框
 */
var BizWxpayBillInfoDlg = {
    bizWxpayBillInfoData : {}
};

/**
 * 清除数据
 */
BizWxpayBillInfoDlg.clearData = function() {
    this.bizWxpayBillInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BizWxpayBillInfoDlg.set = function(key, val) {
    this.bizWxpayBillInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BizWxpayBillInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
BizWxpayBillInfoDlg.close = function() {
    parent.layer.close(window.parent.BizWxpayBill.layerIndex);
}

/**
 * 收集数据
 */
BizWxpayBillInfoDlg.collectData = function() {
    this
    .set('id')
    .set('accid')
    .set('status')
    .set('payTime')
    .set('createTime');
}

/**
 * 提交添加
 */
BizWxpayBillInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/bizWxpayBill/add", function(data){
        Feng.success("添加成功!");
        window.parent.BizWxpayBill.table.refresh();
        BizWxpayBillInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bizWxpayBillInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
BizWxpayBillInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/bizWxpayBill/update", function(data){
        Feng.success("修改成功!");
        window.parent.BizWxpayBill.table.refresh();
        BizWxpayBillInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bizWxpayBillInfoData);
    ajax.start();
}

$(function() {

});

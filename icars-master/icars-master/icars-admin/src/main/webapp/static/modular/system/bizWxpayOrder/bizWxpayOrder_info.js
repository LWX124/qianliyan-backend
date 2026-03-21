/**
 * 初始化详情对话框
 */
var BizWxpayOrderInfoDlg = {
    bizWxpayOrderInfoData : {}
};

/**
 * 清除数据
 */
BizWxpayOrderInfoDlg.clearData = function() {
    this.bizWxpayOrderInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BizWxpayOrderInfoDlg.set = function(key, val) {
    this.bizWxpayOrderInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BizWxpayOrderInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
BizWxpayOrderInfoDlg.close = function() {
    parent.layer.close(window.parent.BizWxpayOrder.layerIndex);
}

/**
 * 收集数据
 */
BizWxpayOrderInfoDlg.collectData = function() {
    this
    .set('outTradeNo')
    .set('amount')
    .set('status')
    .set('openid')
    .set('prepayId')
    .set('notifyTime')
    .set('createTime');
}

/**
 * 提交添加
 */
BizWxpayOrderInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/bizWxpayOrder/add", function(data){
        Feng.success("添加成功!");
        window.parent.BizWxpayOrder.table.refresh();
        BizWxpayOrderInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bizWxpayOrderInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
BizWxpayOrderInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/bizWxpayOrder/update", function(data){
        Feng.success("修改成功!");
        window.parent.BizWxpayOrder.table.refresh();
        BizWxpayOrderInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bizWxpayOrderInfoData);
    ajax.start();
}

$(function() {

});

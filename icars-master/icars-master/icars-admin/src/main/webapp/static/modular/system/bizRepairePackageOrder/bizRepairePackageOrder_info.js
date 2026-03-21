/**
 * 初始化详情对话框
 */
var BizRepairePackageOrderInfoDlg = {
    bizRepairePackageOrderInfoData : {}
};

/**
 * 清除数据
 */
BizRepairePackageOrderInfoDlg.clearData = function() {
    this.bizRepairePackageOrderInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BizRepairePackageOrderInfoDlg.set = function(key, val) {
    this.bizRepairePackageOrderInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BizRepairePackageOrderInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
BizRepairePackageOrderInfoDlg.close = function() {
    parent.layer.close(window.parent.BizRepairePackageOrder.layerIndex);
}

/**
 * 收集数据
 */
BizRepairePackageOrderInfoDlg.collectData = function() {
    this
    .set('id')
    .set('orderno')
    .set('openid')
    .set('packageid')
    .set('phone')
    .set('price')
    .set('orderStatus')
    .set('lng')
    .set('lat')
    .set('address')
    .set('modifyTime')
    .set('createTime');
}

/**
 * 提交添加
 */
BizRepairePackageOrderInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/bizRepairePackageOrder/add", function(data){
        Feng.success("添加成功!");
        window.parent.BizRepairePackageOrder.table.refresh();
        BizRepairePackageOrderInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bizRepairePackageOrderInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
BizRepairePackageOrderInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/bizRepairePackageOrder/update", function(data){
        Feng.success("修改成功!");
        window.parent.BizRepairePackageOrder.table.refresh();
        BizRepairePackageOrderInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bizRepairePackageOrderInfoData);
    ajax.start();
}

$(function() {

});

/**
 * 初始化详情对话框
 */
var BizMaintainPackageOrderInfoDlg = {
    bizMaintainPackageOrderInfoData : {}
};

/**
 * 清除数据
 */
BizMaintainPackageOrderInfoDlg.clearData = function() {
    this.bizMaintainPackageOrderInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BizMaintainPackageOrderInfoDlg.set = function(key, val) {
    this.bizMaintainPackageOrderInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BizMaintainPackageOrderInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
BizMaintainPackageOrderInfoDlg.close = function() {
    parent.layer.close(window.parent.BizRepairePackageOrder.layerIndex);
}

/**
 * 收集数据
 */
BizMaintainPackageOrderInfoDlg.collectData = function() {
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
BizMaintainPackageOrderInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/bizMaintainPackageOrder/add", function(data){
        Feng.success("添加成功!");
        window.parent.BizRepairePackageOrder.table.refresh();
        BizMaintainPackageOrderInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bizMaintainPackageOrderInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
BizMaintainPackageOrderInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/bizMaintainPackageOrder/update", function(data){
        Feng.success("修改成功!");
        window.parent.BizRepairePackageOrder.table.refresh();
        BizMaintainPackageOrderInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bizMaintainPackageOrderInfoData);
    ajax.start();
}

$(function() {

});

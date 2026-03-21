/**
 * 初始化详情对话框
 */
var BizClaimInfoDlg = {
    bizClaimInfoData : {}
};

/**
 * 清除数据
 */
BizClaimInfoDlg.clearData = function() {
    this.bizClaimInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BizClaimInfoDlg.set = function(key, val) {
    this.bizClaimInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BizClaimInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
BizClaimInfoDlg.close = function() {
    parent.layer.close(window.parent.BizClaim.layerIndex);
}

/**
 * 收集数据
 */
BizClaimInfoDlg.collectData = function() {
    this
    .set('id')
    .set('phone')
    .set('name')
    .set('cph')
    .set('type')
    .set('status')
    .set('desc')
    .set('claimer')
    .set('lng')
    .set('lat')
    .set('address')
    .set('createtime');
}

/**
 * 提交添加
 */
BizClaimInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/bizClaim/add", function(data){
        Feng.success("添加成功!");
        window.parent.BizClaim.table.refresh();
        BizClaimInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bizClaimInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
BizClaimInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/bizClaim/update", function(data){
        Feng.success("修改成功!");
        window.parent.BizClaim.table.refresh();
        BizClaimInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bizClaimInfoData);
    ajax.start();
}

$(function() {

});

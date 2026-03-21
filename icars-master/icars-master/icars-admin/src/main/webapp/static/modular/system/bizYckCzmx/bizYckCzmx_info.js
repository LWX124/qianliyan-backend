/**
 * 初始化详情对话框
 */
var BizYckCzmxInfoDlg = {
    bizYckCzmxInfoData : {}
};

/**
 * 清除数据
 */
BizYckCzmxInfoDlg.clearData = function() {
    this.bizYckCzmxInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BizYckCzmxInfoDlg.set = function(key, val) {
    this.bizYckCzmxInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BizYckCzmxInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
BizYckCzmxInfoDlg.close = function() {
    parent.layer.close(window.parent.BizYckCzmx.layerIndex);
}

/**
 * 收集数据
 */
BizYckCzmxInfoDlg.collectData = function() {
    this
    .set('id')
    .set('openid')
    .set('orderNo')
    .set('amount')
    .set('operator')
    .set('detailType')
    .set('createTime');
}

/**
 * 提交添加
 */
BizYckCzmxInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/bizYckCzmx/add", function(data){
        Feng.success("添加成功!");
        window.parent.BizYckCzmx.table.refresh();
        BizYckCzmxInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bizYckCzmxInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
BizYckCzmxInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/bizYckCzmx/update", function(data){
        Feng.success("修改成功!");
        window.parent.BizYckCzmx.table.refresh();
        BizYckCzmxInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bizYckCzmxInfoData);
    ajax.start();
}

$(function() {

});

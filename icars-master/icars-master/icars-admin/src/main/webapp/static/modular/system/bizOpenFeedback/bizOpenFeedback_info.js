/**
 * 初始化用户反馈详情对话框
 */
var BizOpenFeedbackInfoDlg = {
    bizOpenFeedbackInfoData : {}
};

/**
 * 清除数据
 */
BizOpenFeedbackInfoDlg.clearData = function() {
    this.bizOpenFeedbackInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BizOpenFeedbackInfoDlg.set = function(key, val) {
    this.bizOpenFeedbackInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BizOpenFeedbackInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
BizOpenFeedbackInfoDlg.close = function() {
    parent.layer.close(window.parent.BizOpenFeedback.layerIndex);
}

/**
 * 收集数据
 */
BizOpenFeedbackInfoDlg.collectData = function() {
    this
    .set('id')
    .set('account')
    .set('content')
    .set('creTime')
    .set('type');
}

/**
 * 提交添加
 */
BizOpenFeedbackInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/bizOpenFeedback/add", function(data){
        Feng.success("添加成功!");
        window.parent.BizOpenFeedback.table.refresh();
        BizOpenFeedbackInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bizOpenFeedbackInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
BizOpenFeedbackInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/bizOpenFeedback/update", function(data){
        Feng.success("修改成功!");
        window.parent.BizOpenFeedback.table.refresh();
        BizOpenFeedbackInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bizOpenFeedbackInfoData);
    ajax.start();
}

$(function() {

});

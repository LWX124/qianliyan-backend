/**
 * 初始化详情对话框
 */
var BizClaimerShowInfoDlg = {
    bizClaimerShowInfoData : {}
};

/**
 * 清除数据
 */
BizClaimerShowInfoDlg.clearData = function() {
    this.bizClaimerShowInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BizClaimerShowInfoDlg.set = function(key, val) {
    this.bizClaimerShowInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BizClaimerShowInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
BizClaimerShowInfoDlg.close = function() {
    parent.layer.close(window.parent.BizClaimerShow.layerIndex);
}

/**
 * 收集数据
 */
BizClaimerShowInfoDlg.collectData = function() {
    this
    .set('id')
    .set('name')
    .set('imgUrl')
    .set('motto')
    .set('story')
    .set('status')
    .set('createtime');
}

/**
 * 提交添加
 */
BizClaimerShowInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/bizClaimerShow/add", function(data){
        Feng.success("添加成功!");
        window.parent.BizClaimerShow.table.refresh();
        BizClaimerShowInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bizClaimerShowInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
BizClaimerShowInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/bizClaimerShow/update", function(data){
        Feng.success("修改成功!");
        window.parent.BizClaimerShow.table.refresh();
        BizClaimerShowInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bizClaimerShowInfoData);
    ajax.start();
}

$(function() {
    // 初始化头像上传
    var avatarUp = new $WebUpload("imgUrl");
    avatarUp.setUploadBarId("progressBar");
    avatarUp.init();
});

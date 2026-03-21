/**
 * 初始化详情对话框
 */
var BizWxSalaryInfoDlg = {
    bizWxSalaryInfoData : {}
};

/**
 * 清除数据
 */
BizWxSalaryInfoDlg.clearData = function() {
    this.bizWxSalaryInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BizWxSalaryInfoDlg.set = function(key, val) {
    this.bizWxSalaryInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BizWxSalaryInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
BizWxSalaryInfoDlg.close = function() {
    parent.layer.close(window.parent.BizWxSalary.layerIndex);
}

/**
 * 收集数据
 */
BizWxSalaryInfoDlg.collectData = function() {
    this
    .set('id')
    .set('amount');
}

/**
 * 提交添加
 */
BizWxSalaryInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/bizWxSalary/add", function(data){
        Feng.success("添加成功!");
        window.parent.BizWxSalary.table.refresh();
        BizWxSalaryInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bizWxSalaryInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
BizWxSalaryInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/bizWxSalary/update", function(data){
        Feng.success("修改成功!");
        window.parent.BizWxSalary.table.refresh();
        BizWxSalaryInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bizWxSalaryInfoData);
    ajax.start();
}

$(function() {

});

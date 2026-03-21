/**
 * 初始化详情对话框
 */
var BizAccidLevelInfoDlg = {
    bizAccidLevelInfoData : {}
};

/**
 * 清除数据
 */
BizAccidLevelInfoDlg.clearData = function() {
    this.bizAccidLevelInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BizAccidLevelInfoDlg.set = function(key, val) {
    this.bizAccidLevelInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BizAccidLevelInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
BizAccidLevelInfoDlg.close = function() {
    parent.layer.close(window.parent.BizAccidLevel.layerIndex);
}

/**
 * 收集数据
 */
BizAccidLevelInfoDlg.collectData = function() {
    this
    .set('id')
    .set('amount')
    .set('level');
}
function isNum(val){
    // isNaN()函数 把空串 空格 以及NUll 按照0来处理 所以先去除
    if(val === "" || val ==null){
        return false;
    }
    if(!isNaN(val)){
        return true;
    }else{
        return false;
    }
}
/**
 * 提交添加
 */
BizAccidLevelInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    if(!isNum(this.bizAccidLevelInfoData.amount)){
        Feng.error("参数格式错误，请检查重新填写");
        return;
    }
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/bizAccidLevel/add", function(data){
        Feng.success("添加成功!");
        window.parent.BizAccidLevel.table.refresh();
        BizAccidLevelInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bizAccidLevelInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
BizAccidLevelInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/bizAccidLevel/update", function(data){
        Feng.success("修改成功!");
        window.parent.BizAccidLevel.table.refresh();
        BizAccidLevelInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bizAccidLevelInfoData);
    ajax.start();
}

$(function() {

});

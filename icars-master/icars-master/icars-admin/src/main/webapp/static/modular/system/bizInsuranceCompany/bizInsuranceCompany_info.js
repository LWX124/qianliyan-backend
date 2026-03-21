/**
 * 初始化详情对话框
 */
var BizInsuranceCompanyInfoDlg = {
    bizInsuranceCompanyInfoData : {},
    imageUrl : null
};

/**
 * 清除数据
 */
BizInsuranceCompanyInfoDlg.clearData = function() {
    this.bizInsuranceCompanyInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BizInsuranceCompanyInfoDlg.set = function(key, val) {
    this.bizInsuranceCompanyInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BizInsuranceCompanyInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
BizInsuranceCompanyInfoDlg.close = function() {
    parent.layer.close(window.parent.BizInsuranceCompany.layerIndex);
}

/**
 * 收集数据
 */
BizInsuranceCompanyInfoDlg.collectData = function() {
    this.set('name');
    if(BizInsuranceCompanyInfoDlg.imageUrl){
        this.bizInsuranceCompanyInfoData['url'] = BizInsuranceCompanyInfoDlg.imageUrl;
    }
}
/**
 * 提交添加
 */
BizInsuranceCompanyInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/bizInsuranceCompany/add", function(data){
        Feng.success("添加成功!");
        window.parent.BizInsuranceCompany.table.refresh();
        BizInsuranceCompanyInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bizInsuranceCompanyInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
BizInsuranceCompanyInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/bizInsuranceCompany/update", function(data){
        Feng.success("修改成功!");
        window.parent.BizInsuranceCompany.table.refresh();
        BizInsuranceCompanyInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bizInsuranceCompanyInfoData);
    ajax.start();
}

$(function() {

});

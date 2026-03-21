/**
 * 初始化小程序维修表详情对话框
 */
var XcxMaintenanceInfoDlg = {
    xcxMaintenanceInfoData : {}
};

/**
 * 清除数据
 */
XcxMaintenanceInfoDlg.clearData = function() {
    this.xcxMaintenanceInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
XcxMaintenanceInfoDlg.set = function(key, val) {
    this.xcxMaintenanceInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
XcxMaintenanceInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
XcxMaintenanceInfoDlg.close = function() {
    parent.layer.close(window.parent.XcxMaintenance.layerIndex);
}

/**
 * 收集数据
 */
XcxMaintenanceInfoDlg.collectData = function() {
    this
    .set('id')
    .set('title')
    .set('price')
    .set('imageUrl')
    .set('type')
    .set('content')
    .set('carType')
    .set('content',XcxMaintenanceInfoDlg.contentEditor.txt.html())
    .set('carType',XcxMaintenanceInfoDlg.carTypeEditor.txt.html());
}

/**
 * 提交添加
 */
XcxMaintenanceInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/xcxMaintenance/add", function(data){
        Feng.success("添加成功!");
        window.parent.XcxMaintenance.table.refresh();
        XcxMaintenanceInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.xcxMaintenanceInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
XcxMaintenanceInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/xcxMaintenance/update", function(data){
        Feng.success("修改成功!");
        window.parent.XcxMaintenance.table.refresh();
        XcxMaintenanceInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.xcxMaintenanceInfoData);
    ajax.start();
}


$(function () {
    Feng.initValidator("xcxMaintenanceForm", XcxMaintenanceInfoDlg.validateFields);
    //初始化编辑器
    var E = window.wangEditor;
    var contentEditor = new E('#content');
    contentEditor.create();
    contentEditor.txt.html($("#contentVal").val());
    XcxMaintenanceInfoDlg.contentEditor = contentEditor;

    var str=$("#typeHidden").val();
    if(str==null||str==undefined){
            $("#type").val(1);
    }else{
        $("#type").val($("#typeHidden").val());
    }

});
$(function () {
    Feng.initValidator("xcxMaintenanceForm", XcxMaintenanceInfoDlg.validateFields);
    //初始化编辑器
    var E = window.wangEditor;
    var carTypeEditor = new E('#carType');
    carTypeEditor.create();
    carTypeEditor.txt.html($("#carTypeVale").val());
    XcxMaintenanceInfoDlg.carTypeEditor = carTypeEditor;
});

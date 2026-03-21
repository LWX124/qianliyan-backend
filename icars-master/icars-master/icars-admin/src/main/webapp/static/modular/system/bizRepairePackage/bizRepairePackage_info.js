/**
 * 初始化详情对话框
 */
var BizRepairePackageInfoDlg = {
    bizRepairePackageInfoData : {},
    imageUrl: null
};

/**
 * 清除数据
 */
BizRepairePackageInfoDlg.clearData = function() {
    this.bizRepairePackageInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BizRepairePackageInfoDlg.set = function(key, val) {
    this.bizRepairePackageInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BizRepairePackageInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
BizRepairePackageInfoDlg.close = function() {
    parent.layer.close(window.parent.BizRepairePackage.layerIndex);
}

/**
 * 收集数据
 */
BizRepairePackageInfoDlg.collectData = function() {
    this
    .set('id')
    .set('packageName')
    .set('price')
    .set('prePrice');
    var details;
    $("[biztype='detail']").each(function(){
        if($(this).val()){
            details = details == undefined ?  $(this).val() : details + '#'+$(this).val();
        }
    });
    this.set('detail', details);
    if(!BizRepairePackageInfoDlg.imageUrl){
        Feng.error("请上传套餐首页图");
        return;
    }
    this.set('img', BizRepairePackageInfoDlg.imageUrl);
}

/**
 * 提交添加
 */
BizRepairePackageInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/bizRepairePackage/add", function(data){
        Feng.success("添加成功!");
        window.parent.BizRepairePackage.table.refresh();
        BizRepairePackageInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bizRepairePackageInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
BizRepairePackageInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/bizRepairePackage/update", function(data){
        Feng.success("修改成功!");
        window.parent.BizRepairePackage.table.refresh();
        BizRepairePackageInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bizRepairePackageInfoData);
    ajax.start();
}

/**
 * 添加明细栏位
 */
BizRepairePackageInfoDlg.addDetailInput = function() {
    $('#addLw').before('<div class="form-group">\n' +
        '    <label class="col-sm-3 control-label">  </label>\n' +
        '    <div class="col-sm-9">\n' +
        '        <input class="form-control" biztype="detail" name="price" type="text">\n' +
        '\n' +
        '    </div>\n' +
        '</div>');
}


$(function() {

});

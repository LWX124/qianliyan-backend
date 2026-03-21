/**
 * 初始化详情对话框
 */
var BizMaintainPackageInfoDlg = {
    bizMaintainPackageInfoData : {},
    imageUrl: null
};

/**
 * 清除数据
 */
BizMaintainPackageInfoDlg.clearData = function() {
    this.bizMaintainPackageInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BizMaintainPackageInfoDlg.set = function(key, val) {
    this.bizMaintainPackageInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BizMaintainPackageInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
BizMaintainPackageInfoDlg.close = function() {
    parent.layer.close(window.parent.BizRepairePackage.layerIndex);
}

/**
 * 收集数据
 */
BizMaintainPackageInfoDlg.collectData = function() {
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
    if(!BizMaintainPackageInfoDlg.imageUrl){
        Feng.error("请上传套餐首页图");
        return;
    }
    this.set('img', BizMaintainPackageInfoDlg.imageUrl);
}

/**
 * 提交添加
 */
BizMaintainPackageInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/bizMaintainPackage/add", function(data){
        Feng.success("添加成功!");
        window.parent.BizRepairePackage.table.refresh();
        BizMaintainPackageInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bizMaintainPackageInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
BizMaintainPackageInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/bizMaintainPackage/update", function(data){
        Feng.success("修改成功!");
        window.parent.BizRepairePackage.table.refresh();
        BizMaintainPackageInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bizMaintainPackageInfoData);
    ajax.start();
}

/**
 * 添加明细栏位
 */
BizMaintainPackageInfoDlg.addDetailInput = function() {
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

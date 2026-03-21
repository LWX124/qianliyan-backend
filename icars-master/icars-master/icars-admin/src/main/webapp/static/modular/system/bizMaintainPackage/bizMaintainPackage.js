/**
 * 管理初始化
 */
var BizMaintainPackage = {
    id: "BizMaintainPackageTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
BizMaintainPackage.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '套餐名称', field: 'packageName', visible: true, align: 'center', valign: 'middle'},
            {title: '套餐明细', field: 'detail', visible: true, align: 'center', formatter: aFormatter, valign: 'middle'},
            {title: '价格', field: 'price', visible: true, align: 'center', valign: 'middle'},
            {title: '原价', field: 'prePrice', visible: true, align: 'center', valign: 'middle'},
            {title: '状态', field: 'pstatusName', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'}
    ];
};
function aFormatter(value, row, index) {
    if(value == null || value == undefined || value.length < 1){
        return '';
    }
    return [
        '<a href="javascript:void(0);" onclick="showDetail(\''+value+'\')" >查看套餐商品明细</a>'
    ].join("")
}
function showDetail(val){
    $('#maintainDetail').empty();
    var details = val.split('#');
    details.forEach(function( val, index ) {
        $('#maintainDetail').append('<p style="padding: 5px;">套餐商品'+(index + 1)+':&nbsp;&nbsp;'+val+'</p>')
    });
    layer.open({
        type: 1,
        title: ['套餐商品明细', 'background-color: #1E9FFF'],
        // shadeClose: true,
        // area: ['640px', '750px'],
        area: '300px',
        // shade: 0.8,
        // offset: 'auto',
        content: $('#maintainDetail')
    });
}

/**
 * 检查是否选中
 */
BizMaintainPackage.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        BizMaintainPackage.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加
 */
BizMaintainPackage.openAddBizMaintainPackage = function () {
    var index = layer.open({
        type: 2,
        title: '添加',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/bizMaintainPackage/bizMaintainPackage_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看详情
 */
BizMaintainPackage.openBizMaintainPackageDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/bizMaintainPackage/bizMaintainPackage_update/' + BizMaintainPackage.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除
 */
BizMaintainPackage.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/bizMaintainPackage/delete", function (data) {
            Feng.success("删除成功!");
            BizMaintainPackage.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("bizMaintainPackageId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 上架/下架
 */
BizMaintainPackage.onShowOrOffShow = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/bizMaintainPackage/onShowOrOffShow", function (data) {
            Feng.success("操作成功!");
            BizMaintainPackage.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("bizMaintainPackageId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询列表
 */
BizMaintainPackage.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    BizMaintainPackage.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = BizMaintainPackage.initColumn();
    var table = new BSTable(BizMaintainPackage.id, "/bizMaintainPackage/list", defaultColunms);
    table.setPaginationType("server");
    BizMaintainPackage.table = table.init();
});

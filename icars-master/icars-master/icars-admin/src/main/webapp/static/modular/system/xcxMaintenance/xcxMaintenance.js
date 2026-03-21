/**
 * 小程序维修表管理初始化
 */
var XcxMaintenance = {
    id: "XcxMaintenanceTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
XcxMaintenance.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: 'id', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '标题', field: 'title', visible: true, align: 'center', valign: 'middle'},
            {title: '价格', field: 'price', visible: true, align: 'center', valign: 'middle'},
            {title: '首页图片', field: 'imageUrl', visible: true, align: 'center', valign: 'middle'},
            {title: '类型', field: 'type', visible: true, align: 'center', valign: 'middle'},
//            {title: '套餐内容', field: 'content', visible: true, align: 'center', valign: 'middle'},
//            {title: '使用车型', field: 'carType', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '修改时间', field: 'updateTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
XcxMaintenance.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        XcxMaintenance.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加小程序维修表
 */
XcxMaintenance.openAddXcxMaintenance = function () {
    var index = layer.open({
        type: 2,
        title: '添加小程序维修表',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/xcxMaintenance/xcxMaintenance_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看小程序维修表详情
 */
XcxMaintenance.openXcxMaintenanceDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '小程序维修表详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/xcxMaintenance/xcxMaintenance_update/' + XcxMaintenance.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除小程序维修表
 */
XcxMaintenance.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/xcxMaintenance/delete", function (data) {
            Feng.success("删除成功!");
            XcxMaintenance.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("xcxMaintenanceId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询小程序维修表列表
 */
XcxMaintenance.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    XcxMaintenance.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = XcxMaintenance.initColumn();
    var table = new BSTable(XcxMaintenance.id, "/xcxMaintenance/list", defaultColunms);
    table.setPaginationType("client");
    XcxMaintenance.table = table.init();
});


var NoticeInfoDlg = {
    noticeInfoData: {},
    editor: null,
    validateFields: {
        title: {
            validators: {
                notEmpty: {
                    message: '标题不能为空'
                }
            }
        }
    }
};
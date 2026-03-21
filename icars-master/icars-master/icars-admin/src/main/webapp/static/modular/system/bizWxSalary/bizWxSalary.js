/**
 * 管理初始化
 */
var BizWxSalary = {
    id: "BizWxSalaryTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
BizWxSalary.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '主键id', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '奖励金额', field: 'amount', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
BizWxSalary.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        BizWxSalary.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加
 */
BizWxSalary.openAddBizWxSalary = function () {
    var index = layer.open({
        type: 2,
        title: '添加',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/bizWxSalary/bizWxSalary_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看详情
 */
BizWxSalary.openBizWxSalaryDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/bizWxSalary/bizWxSalary_update/' + BizWxSalary.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除
 */
BizWxSalary.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/bizWxSalary/delete", function (data) {
            Feng.success("删除成功!");
            BizWxSalary.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("bizWxSalaryId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询列表
 */
BizWxSalary.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    BizWxSalary.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = BizWxSalary.initColumn();
    var table = new BSTable(BizWxSalary.id, "/bizWxSalary/list", defaultColunms);
    table.setPaginationType("client");
    BizWxSalary.table = table.init();
});

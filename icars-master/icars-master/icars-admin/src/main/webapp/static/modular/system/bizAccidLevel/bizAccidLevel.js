/**
 * 管理初始化
 */
var BizAccidLevel = {
    id: "BizAccidLevelTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
BizAccidLevel.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '主键id', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '事故扣除金额', field: 'amount', visible: true, align: 'center', valign: 'middle'},
            {title: '事故等级', field: 'level', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
BizAccidLevel.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        BizAccidLevel.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加
 */
BizAccidLevel.openAddBizAccidLevel = function () {
    var index = layer.open({
        type: 2,
        title: '添加',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/bizAccidLevel/bizAccidLevel_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看详情
 */
BizAccidLevel.openBizAccidLevelDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/bizAccidLevel/bizAccidLevel_update/' + BizAccidLevel.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除
 */
BizAccidLevel.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/bizAccidLevel/delete", function (data) {
            Feng.success("删除成功!");
            BizAccidLevel.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("bizAccidLevelId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询列表
 */
BizAccidLevel.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    BizAccidLevel.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = BizAccidLevel.initColumn();
    var table = new BSTable(BizAccidLevel.id, "/bizAccidLevel/list", defaultColunms);
    table.setPaginationType("client");
    BizAccidLevel.table = table.init();
});

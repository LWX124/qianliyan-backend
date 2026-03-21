/**
 * 用户反馈管理初始化
 */
var BizOpenFeedback = {
    id: "BizOpenFeedbackTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
BizOpenFeedback.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '主键', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '反馈账户', field: 'account', visible: true, align: 'center', valign: 'middle'},
            {title: '内容', field: 'content', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'creTime', visible: true, align: 'center', valign: 'middle'},
            {title: '来源', field: 'type', visible: true, align: 'center', valign: 'middle',formatter: function (value, row, index) {
                if(value==2){
                    return '视频端';
                }
                return '理赔端';
              }}
    ];
};

/**
 * 检查是否选中
 */
BizOpenFeedback.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        BizOpenFeedback.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加用户反馈
 */
BizOpenFeedback.openAddBizOpenFeedback = function () {
    var index = layer.open({
        type: 2,
        title: '添加用户反馈',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/bizOpenFeedback/bizOpenFeedback_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看用户反馈详情
 */
BizOpenFeedback.openBizOpenFeedbackDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '用户反馈详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/bizOpenFeedback/bizOpenFeedback_update/' + BizOpenFeedback.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除用户反馈
 */
BizOpenFeedback.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/bizOpenFeedback/delete", function (data) {
            Feng.success("删除成功!");
            BizOpenFeedback.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("bizOpenFeedbackId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询用户反馈列表
 */
BizOpenFeedback.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    BizOpenFeedback.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = BizOpenFeedback.initColumn();
    var table = new BSTable(BizOpenFeedback.id, "/bizOpenFeedback/list", defaultColunms);
    table.setPaginationType("client");
    BizOpenFeedback.table = table.init();
});

/**
 * 管理初始化
 */
var BizWxpayOrder = {
    id: "BizWxpayOrderTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
BizWxpayOrder.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '支付订单号', field: 'outTradeNo', visible: true, align: 'center', valign: 'middle'},
            {title: '支付金额', field: 'amount', visible: true, align: 'center', valign: 'middle'},
            {title: '支付状态', field: 'statusName', visible: true, align: 'center', valign: 'middle'},
            {title: '支付人ID', field: 'openid', visible: false, align: 'center', valign: 'middle'},
            {title: '支付人名称', field: 'name', visible: true, align: 'center', valign: 'middle'},
            {title: '支付人电话', field: 'phone', visible: true, align: 'center', valign: 'middle'},
            {title: '预支付ID', field: 'prepayId', visible: true, align: 'center', valign: 'middle'},
            {title: '支付通知生成时间', field: 'notifyTime', visible: true, align: 'center', valign: 'middle'},
            {title: '订单生成时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
BizWxpayOrder.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        BizWxpayOrder.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加
 */
BizWxpayOrder.openAddBizWxpayOrder = function () {
    var index = layer.open({
        type: 2,
        title: '添加',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/bizWxpayOrder/bizWxpayOrder_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看详情
 */
BizWxpayOrder.openBizWxpayOrderDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/bizWxpayOrder/bizWxpayOrder_update/' + BizWxpayOrder.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除
 */
BizWxpayOrder.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/bizWxpayOrder/delete", function (data) {
            Feng.success("删除成功!");
            BizWxpayOrder.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("bizWxpayOrderId",this.seItem.id);
        ajax.start();
    }
};
/**
 * 查询表单提交参数对象
 * @returns {{}}
 */
BizWxpayOrder.formParams = function() {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    queryData['outTradeNo'] = $("#outTradeNo").val();
    queryData['status'] = $("#status").val();
    var createTimeRange = $("#createStartTime").val();
    if(createTimeRange){
        queryData['createStartTime'] = createTimeRange.split('~')[0];
        queryData['createEndTime'] = createTimeRange.split('~')[1];
    }
    return queryData;
}
/**
 * 查询列表
 */
BizWxpayOrder.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    queryData['status'] = $("#status").val();
    queryData['outTradeNo'] = $("#outTradeNo").val();
    var createTimeRange = $("#createStartTime").val();
    if(createTimeRange){
        queryData['createStartTime'] = createTimeRange.split('~')[0];
        queryData['createEndTime'] = createTimeRange.split('~')[1];
    }else {
        queryData['createStartTime'] = null;
        queryData['createEndTime'] = null;
    }
    BizWxpayOrder.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = BizWxpayOrder.initColumn();
    var table = new BSTable(BizWxpayOrder.id, "/bizWxpayOrder/list", defaultColunms);
    table.setPaginationType("server");
    table.setQueryParams(BizWxpayOrder.formParams());
    BizWxpayOrder.table = table.init();
});

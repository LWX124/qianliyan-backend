/**
 * 管理初始化
 */
var BizYckCzmx = {
    id: "BizYckCzmxTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
BizYckCzmx.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '编号', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '用户ID', field: 'openid', visible: false, align: 'center', valign: 'middle'},
            {title: '用户姓名', field: 'name', visible: true, align: 'center', valign: 'middle'},
            {title: '用户手机号', field: 'name', visible: false, align: 'center', valign: 'middle'},
            {title: '充值订单号', field: 'orderNo', visible: true, align: 'center', valign: 'middle'},
            {title: '金额', field: 'amount', visible: true, align: 'center', valign: 'middle'},
            {title: '操作人', field: 'operator', visible: true, align: 'center', valign: 'middle'},
            {title: '操作类型', field: 'detailType', visible: false, align: 'center', valign: 'middle'},
            {title: '操作类型', field: 'detailTypeName', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
BizYckCzmx.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        BizYckCzmx.seItem = selected[0];
        return true;
    }
};

/**
 * 查询列表
 */
BizYckCzmx.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    queryData['detailType'] = $("#detailType").val();
    var createTimeRange = $("#createStartTime").val();
    if(createTimeRange){
        queryData['createStartTime'] = createTimeRange.split('~')[0];
        queryData['createEndTime'] = createTimeRange.split('~')[1];
    }else {
        queryData['createStartTime'] = null;
        queryData['createEndTime'] = null;
    }
    BizYckCzmx.table.refresh({query: queryData});
};
BizYckCzmx.formParams = function(){
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    queryData['detailType'] = $("#detailType").val();
    var createTimeRange = $("#createStartTime").val();
    if(createTimeRange){
        queryData['createStartTime'] = createTimeRange.split('~')[0];
        queryData['createEndTime'] = createTimeRange.split('~')[1];
    }else {
        queryData['createStartTime'] = null;
        queryData['createEndTime'] = null;
    }
    return queryData;
}
$(function () {
    var defaultColunms = BizYckCzmx.initColumn();
    var table = new BSTable(BizYckCzmx.id, "/bizYckCzmx/list", defaultColunms);
    table.setPaginationType("server");
    table.setQueryParams(BizYckCzmx.formParams());
    BizYckCzmx.table = table.init();
});

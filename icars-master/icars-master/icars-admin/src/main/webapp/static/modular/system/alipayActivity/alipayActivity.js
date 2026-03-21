/**
 * 支付宝营销红包活动管理初始化
 */
var AlipayActivity = {
    id: "AlipayActivityTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
AlipayActivity.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '主键id', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '活动号', field: 'crowd_no', visible: true, align: 'center', valign: 'middle'},
            {title: '活动名称', field: 'couponName', visible: true, align: 'center', valign: 'middle'},
            {title: '支付链接', field: 'pay_url', visible: true, align: 'center', formatter: aFormatter, valign: 'middle'},
            {title: '原始活动号', field: 'origin_crowd_no', visible: true, align: 'center', valign: 'middle'},
            {title: '活动总金额', field: 'totalAmount', visible: true, align: 'center', valign: 'middle'},
            {title: '活动已发放金额', field: 'sendAmount', visible: true, align: 'center', valign: 'middle'},
            {title: '红包总个数', field: 'totalCount', visible: true, align: 'center', valign: 'middle'},
            {title: '活动状态', field: 'campStatus', visible: true, align: 'center', valign: 'middle'},
            {title: '活动有效开始时间', field: 'startTime', visible: true, align: 'center', valign: 'middle'},
            {title: '活动有效结束时间', field: 'endTime', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
AlipayActivity.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        AlipayActivity.seItem = selected[0];
        return true;
    }
};
function aFormatter(value, row, index) {
    return [
        '<a href="'+value+'" target="view_window">点击支付</a>'
    ].join("")
}
/**
 * 查询表单提交参数对象
 * @returns {{}}
 */
AlipayActivity.formParams = function() {
    var queryData = {};
    queryData['campStatus'] = $("#campStatus").val();
    queryData['startTime'] = $("#startTime").val();
    queryData['endTime'] = $("#endTime").val();
    return queryData;
}
/**
 * 点击添加支付宝营销红包活动
 */
AlipayActivity.openAddAlipayActivity = function () {
    var index = layer.open({
        type: 2,
        title: '添加支付宝营销红包活动',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/alipayActivity/alipayActivity_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看支付宝营销红包活动详情
 */
// AlipayActivity.openAlipayActivityDetail = function () {
//     if (this.check()) {
//         var index = layer.open({
//             type: 2,
//             title: '支付宝营销红包活动详情',
//             area: ['800px', '420px'], //宽高
//             fix: false, //不固定
//             maxmin: true,
//             content: Feng.ctxPath + '/alipayActivity/alipayActivity_update/' + AlipayActivity.seItem.id
//         });
//         this.layerIndex = index;
//     }
// };

// /**
//  * 删除支付宝营销红包活动
//  */
// AlipayActivity.delete = function () {
//     if (this.check()) {
//         var ajax = new $ax(Feng.ctxPath + "/alipayActivity/delete", function (data) {
//             Feng.success("删除成功!");
//             AlipayActivity.table.refresh();
//         }, function (data) {
//             Feng.error("删除失败!" + data.responseJSON.message + "!");
//         });
//         ajax.set("alipayActivityId",this.seItem.id);
//         ajax.start();
//     }
// };
/**
 * 查询支付宝营销红包活动列表
 */
AlipayActivity.search = function () {
    var queryData = {};
    queryData['campStatus'] = $("#campStatus").val();
    queryData['startTime'] = $("#startTime").val();
    queryData['endTime'] = $("#endTime").val();
    AlipayActivity.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = AlipayActivity.initColumn();
    var table = new BSTable(AlipayActivity.id, "/alipayActivity/list", defaultColunms);
    table.setPaginationType("server");
    table.setQueryParams(AlipayActivity.formParams());
    AlipayActivity.table = table.init();
});

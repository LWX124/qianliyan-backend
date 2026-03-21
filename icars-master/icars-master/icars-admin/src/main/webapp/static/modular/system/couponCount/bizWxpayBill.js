/**
 * coupon管理初始化
 */
var BizWxpayBill = {
    id: "BizWxpayBillTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
BizWxpayBill.initColumn = function () {
    return [
        {field: 'selectItem', radio: false},
        {title: '城市', field: 'cityName', visible: true, align: 'center', valign: 'middle'},
        {title: '1.88元', field: 'oneAmountNumber', visible: true, align: 'center', valign: 'middle',formatter:function(value, row, index){
                   return [value+'个'].join("");
                   }},
        {title: '5元', field: 'fiveAmountNumber', visible: true, align: 'center', valign: 'middle',formatter:function(value, row, index){
                   return [value+'个'].join("");
                   }},
        {title: '10元', field: 'tenAmountNumber', visible: true, align: 'center', valign: 'middle',formatter:function(value, row, index){
                   return [value+'个'].join("");
                   }},
        {title: '总计个数', field: 'sumNumber', visible: true, align: 'center', valign: 'middle',formatter:function(value, row, index){
                   return [value+'个'].join("");
                   }},
        {title: '总金额', field: 'sumMoney', visible: true, align: 'center', valign: 'middle',formatter:function(value, row, index){
                    return [value.toFixed(2)+'元'].join("");
                 }},
        {title: '金额占比', field: 'percentage', visible: true, align: 'center', valign: 'middle',formatter:function(value, row, index){
                 return [(value*100).toFixed(2)+'%'].join("");
              }},
    ];
};

/**
 * 检查是否选中
 */
// BizWxpayBill.check = function () {
//     var selected = $('#' + this.id).bootstrapTable('getSelections');
//     if (selected.length == 0) {
//         Feng.info("请先选中表格中的某一记录！");
//         return false;
//     } else {
//         BizWxpayBill.seItem = selected[0];
//         return true;
//     }
// };

/**
 * 点击添加coupon
 */
BizWxpayBill.openAddBizWxpayBill = function () {
    var index = layer.open({
        type: 2,
        title: '添加coupon',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/bizWxpayBill/bizWxpayBill_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看coupon详情
 */
// BizWxpayBill.openBizWxpayBillDetail = function () {
//     if (this.check()) {
//         var index = layer.open({
//             type: 2,
//             title: 'coupon详情',
//             area: ['800px', '420px'], //宽高
//             fix: false, //不固定
//             maxmin: true,
//             content: Feng.ctxPath + '/bizWxpayBill/bizWxpayBill_update/' + BizWxpayBill.seItem.id
//         });
//         this.layerIndex = index;
//     }
// };

/**
 * 删除coupon
 */
// BizWxpayBill.delete = function () {
//     if (this.check()) {
//         var ajax = new $ax(Feng.ctxPath + "/bizWxpayBill/delete", function (data) {
//             Feng.success("删除成功!");
//             BizWxpayBill.table.refresh();
//         }, function (data) {
//             Feng.error("删除失败!" + data.responseJSON.message + "!");
//         });
//         ajax.set("bizWxpayBillId", this.seItem.id);
//         ajax.start();
//     }
// };

/**
 * 查询coupon列表
 */
BizWxpayBill.search = function () {
    var queryData = {};
    var time = $("#time").val();
    if(time){
        queryData['startTime'] = time.split('~')[0];
        queryData['endTime'] = time.split('~')[1];
    }
    console.log(queryData);
    BizWxpayBill.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = BizWxpayBill.initColumn();
    console.log("defaultColunms=" + defaultColunms);
    var table = new BSTable(BizWxpayBill.id, "/couponCount/list", defaultColunms);
    table.setPaginationType("client");
    BizWxpayBill.table = table.init();
});

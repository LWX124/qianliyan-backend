
var adjusters = {
    id: "adjusters",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
adjusters.initColumn = function () {
    return [
        {field: 'selectItem', radio: false},
        {title: '姓名', field: 'userName', visible: true, align: 'center', valign: 'middle'},
        {title: '身份', field: 'identity', visible: true, align: 'center', valign: 'middle'},
        {title: '是否收费', field: 'sfkf', visible: true, formatter:function(value, row, index){
                if(value=='N'){
                    return ['不收费'].join("");
                }
                 return ['收费'].join("");
//              return [
//                        '<a href="javascript:void(0);" onclick="showVideo(\''+value+'\')" >查看视频</a>'
//                    ].join("")
        },align: 'center', valign: 'middle'},
         {title: '所属组织', field: 'simplename', visible: true, align: 'center', valign: 'middle'},
        {title: '1.88元', field: 'amount188', visible: true, align: 'center', valign: 'middle'},
        {title: '5元', field: 'amount5', visible: true, align: 'center', valign: 'middle'},
        {title: '10元', field: 'amount10', visible: true, align: 'center', valign: 'middle'},
        {title: '信息总数', field: 'couponCount', visible: true, align: 'center', valign: 'middle'},
        {title: '红包总金额', field: 'couponSum', visible: true, align: 'center', valign: 'middle'},
        {title: '订单量', field: 'openClaimNum', visible: true, align: 'center', valign: 'middle'},
        {title: '订单金额', field: 'openClaimSum', visible: true, align: 'center', valign: 'middle'},
        {title: '开单成功率', field: 'openClaimRate', visible: true, align: 'center', valign: 'middle'}
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
adjusters.openAddBizWxpayBill = function () {
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


adjusters.search = function () {
    var queryData = {};
    var time = $("#time").val();
    var name = $("#name").val();
    queryData['name'] = name;
    var department = $("#department").val();
    queryData['department'] = department;
    if(time){
        queryData['startTime'] = time.split('~')[0];
        queryData['endTime'] = time.split('~')[1];
    }
    adjusters.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = adjusters.initColumn();
    var table = new BSTable(adjusters.id, "/claimUser/list", defaultColunms);
    table.setPaginationType("client");
    adjusters.table = table.init();
});

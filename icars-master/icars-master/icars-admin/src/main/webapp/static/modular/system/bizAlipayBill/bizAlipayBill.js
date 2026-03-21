/**
 * 支付宝支付管理管理初始化
 */
var BizAlipayBill = {
    id: "BizAlipayBillTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
BizAlipayBill.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '主键id', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '事故id', field: 'accid', visible: true, align: 'center', valign: 'middle'},
            {title: '支付订单号', field: 'outBizNo', visible: true, align: 'center', valign: 'middle'},
            {title: '支付宝账户', field: 'alipayAccount', visible: true, align: 'center', valign: 'middle'},
            {title: '支付金额', field: 'prizeAmount', visible: true, align: 'center', valign: 'middle'},
            {title: '支付状态', field: 'status', visible: true, align: 'center', valign: 'middle'},
            {title: '支付异常信息', field: 'subMsg', visible: true, align: 'center', valign: 'middle'},
            {title: '支付时间', field: 'payTime', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
BizAlipayBill.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        BizAlipayBill.seItem = selected[0];
        return true;
    }
};
//
// /**
//  * 点击添加支付宝支付管理
//  */
// BizAlipayBill.openAddBizAlipayBill = function () {
//     var index = layer.open({
//         type: 2,
//         title: '添加支付宝支付管理',
//         area: ['800px', '420px'], //宽高
//         fix: false, //不固定
//         maxmin: true,
//         content: Feng.ctxPath + '/bizAlipayBill/bizAlipayBill_add'
//     });
//     this.layerIndex = index;
// };

/**
 * 发起支付
 */
BizAlipayBill.rePay = function () {
    if (this.check() && this.seItem.status == '支付失败') {
        var ajax = new $ax(Feng.ctxPath + "/bizAlipayBill/rePay", function (data) {
            if(data.code == 200){
                Feng.success("支付成功!");
            }else {
                Feng.success(data.message + "!");
            }
            BizAlipayBill.table.refresh();
        }, function (data) {
            Feng.error("支付失败!" + data.responseJSON.message + "!");
        });
        ajax.set("accdId",this.seItem.accid);
        ajax.start();
    } else {
        Feng.success("已支付成功，不允许重复支付!");
    }
};

/**
 * 查询支付宝支付管理列表
 */
BizAlipayBill.search = function () {
    var queryData = {};
    queryData['alipayAccount'] = $("#alipayAccount").val();
    queryData['payStatus'] = $("#payStatus").val();
    queryData['startTime'] = $("#startTime").val();
    queryData['endTime'] = $("#endTime").val();
    BizAlipayBill.table.refresh({query: queryData});
};
/**
 * 查询表单提交参数对象
 * @returns {{}}
 */
BizAlipayBill.formParams = function() {
    var queryData = {};
    queryData['alipayAccount'] = $("#alipayAccount").val();
    queryData['payStatus'] = $("#payStatus").val();
    queryData['startTime'] = $("#startTime").val();
    queryData['endTime'] = $("#endTime").val();
    return queryData;
}

$(function () {
    var defaultColunms = BizAlipayBill.initColumn();
    var table = new BSTable(BizAlipayBill.id, "/bizAlipayBill/list", defaultColunms);
    table.setPaginationType("server");
    table.setQueryParams(BizAlipayBill.formParams());
    BizAlipayBill.table = table.init();
});

/**
 * 管理初始化
 */
var BizMaintainPackageOrder = {
    id: "BizMaintainPackageOrderTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
BizMaintainPackageOrder.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '订单号', field: 'orderno', width: '180', visible: true, align: 'center', valign: 'middle'},
            {title: '套餐名称', field: 'packageName', width: '100', visible: true, align: 'center', valign: 'middle'},
            {title: '套餐明细', field: 'detail', width: '150', visible: true, align: 'center', formatter: aFormatter, valign: 'middle'},
            {title: '微信用户', field: 'openid', width: '100', visible: false, align: 'center', valign: 'middle'},
            {title: '套餐id', field: 'packageid', width: '100', visible: false, align: 'center', valign: 'middle'},
            {title: '用户名称', field: 'name', width: '100', visible: true, align: 'center', valign: 'middle'},
            {title: '用户电话', field: 'phone', width: '100', visible: true, align: 'center', valign: 'middle'},
            {title: '价格', field: 'price', visible: true, width: '100', align: 'center', valign: 'middle'},
            {title: '订单状态', field: 'orderStatus', width: '100', visible: true, align: 'center', valign: 'middle'},
            {title: '经度', field: 'lng', visible: false, width: '100', align: 'center', valign: 'middle'},
            {title: '纬度', field: 'lat', visible: false, width: '100', align: 'center', valign: 'middle'},
            {title: '地址', field: 'address', width: '350', visible: true, align: 'center', valign: 'middle'},
            {title: '定位地图', field: 'mapUrl', width: '100', align: 'center', valign: 'middle',formatter: urlFormatter, sortable: false},
            {title: '维修工姓名', field: 'repaireName', width: '100', visible: true, align: 'center', valign: 'middle'},
            {title: '维修工电话', field: 'repairePhone', width: '100', visible: true, align: 'center', valign: 'middle'},
            {title: '修改时间', field: 'modifyTime', width: '180', visible: false, align: 'center', valign: 'middle'},
            {title: '下单时间', field: 'createTime', width: '180', visible: true, align: 'center', valign: 'middle'}
    ];
};
function showDetail(val){
    $('#maintainOrderDetail').empty();
    var details = val.split('#');
    details.forEach(function( val, index ) {
        $('#maintainOrderDetail').append('<p style="padding: 5px;">套餐商品'+(index + 1)+':&nbsp;&nbsp;'+val+'</p>')
    });
    layer.open({
        type: 1,
        title: ['套餐商品明细', 'background-color: #1E9FFF'],
        // shadeClose: true,
        // area: ['640px', '750px'],
        area: '300px',
        // shade: 0.8,
        // offset: 'auto',
        content: $('#maintainOrderDetail')
    });
}
function urlFormatter(value, row, index) {
    return [
        '<a href="'+value+'" target="view_window">点击查看地图</a>'
    ].join("")
}
/**
 * 点击弹出推送框 推送维修人员
 */
BizMaintainPackageOrder.pushRepaireMan = function () {
    if (this.check()){
        var index = layer.open({
            type: 2,
            title: '推送维修员工',
            area: ['800px', '560px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/bizMaintainPackageOrder/order_push'
        });
        this.layerIndex = index;
    }
};

BizMaintainPackageOrder.stopMaintain = function (){
    window.parent.document.getElementById("maintain_claim").pause();
}

/**
 * 接车确认
 */
BizMaintainPackageOrder.recept = function () {
    if (this.check()) {
        var orderId = this.seItem.id;
        var orderStatus = this.seItem.orderStatus;
        if (orderStatus == '已接车') {
            Feng.error("该订单已接车，请勿重复操作!");
            return;
        }
        var ajax = new $ax(Feng.ctxPath + "/bizMaintainPackageOrder/order/accept", function (data) {
            Feng.success("操作成功!");
            BizMaintainPackageOrder.table.refresh();
        }, function (data) {
            Feng.error("操作失败!" + data.responseJSON.message + "!");
        });
        ajax.set("orderid", orderId);
        ajax.start();
    }
};

/**
 * 取消保养单
 */
BizMaintainPackageOrder.cancel = function () {
    if (this.check()) {
        var orderId = this.seItem.id;
        var orderStatus = this.seItem.orderStatus;
        if (orderStatus == '已取消') {
            Feng.error("该订单已取消，请勿重复操作!");
            return;
        }
        var ajax = new $ax(Feng.ctxPath + "/bizMaintainPackageOrder/order/cancel", function (data) {
            Feng.success("操作成功!");
            BizMaintainPackageOrder.table.refresh();
        }, function (data) {
            Feng.error("操作失败!" + data.responseJSON.message + "!");
        });
        ajax.set("orderid", orderId);
        ajax.start();
    }
};

/**
 * 取消保养单
 */
BizMaintainPackageOrder.finish = function () {
    if (this.check()) {
        var orderId = this.seItem.id;
        var orderStatus = this.seItem.orderStatus;
        if (orderStatus == '已完成') {
            Feng.error("该订单已完成，请勿重复操作!");
            return;
        }
        var ajax = new $ax(Feng.ctxPath + "/bizMaintainPackageOrder/order/finish", function (data) {
            Feng.success("操作成功!");
            BizMaintainPackageOrder.table.refresh();
        }, function (data) {
            Feng.error("操作失败!" + data.responseJSON.message + "!");
        });
        ajax.set("orderid", orderId);
        ajax.start();
    }
};

function aFormatter(value, row, index) {
    if(value == null || value == undefined || value.length < 1){
        return '';
    }
    return [
        '<a href="javascript:void(0);" onclick="showDetail(\''+value+'\')" >查看套餐商品明细</a>'
    ].join("")
}
/**
 * 检查是否选中
 */
BizMaintainPackageOrder.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        BizMaintainPackageOrder.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加
 */
BizMaintainPackageOrder.openAddBizMaintainPackageOrder = function () {
    var index = layer.open({
        type: 2,
        title: '添加',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/bizMaintainPackageOrder/bizMaintainPackageOrder_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看详情
 */
BizMaintainPackageOrder.openBizMaintainPackageOrderDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/bizMaintainPackageOrder/bizMaintainPackageOrder_update/' + BizMaintainPackageOrder.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除
 */
BizMaintainPackageOrder.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/bizMaintainPackageOrder/delete", function (data) {
            Feng.success("删除成功!");
            BizMaintainPackageOrder.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("bizMaintainPackageOrderId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询列表
 */
BizMaintainPackageOrder.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    queryData['orderStatus'] = $("#orderStatus").val();
    var createTimeRange = $("#createStartTime").val();
    if(createTimeRange){
        queryData['createStartTime'] = createTimeRange.split('~')[0];
        queryData['createEndTime'] = createTimeRange.split('~')[1];
        // if((new Date(queryData['createEndTime']) - new Date(queryData['createStartTime']))/1000/60/60/24 > 7){
        //     Feng.error("创建时间查询范围不能大于7天!");
        //     return ;
        // }
    }else {
        queryData['createStartTime'] = null;
        queryData['createEndTime'] = null;
    }
    BizMaintainPackageOrder.table.refresh({query: queryData});
};

BizMaintainPackageOrder.resetSearch = function () {
    $("#condition").val("");
    $("#createStartTime").val("");
    $("#orderStatus").val("");
    BizMaintainPackageOrder.search();
}
$(function () {
    var defaultColunms = BizMaintainPackageOrder.initColumn();
    var table = new BSTable(BizMaintainPackageOrder.id, "/bizMaintainPackageOrder/list", defaultColunms);
    table.setPaginationType("server");
    BizMaintainPackageOrder.table = table.init();
});

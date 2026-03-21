/**
 * 管理初始化
 */
var PartnerOpenClaim = {
    id: "PartnerOpenClaimTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
PartnerOpenClaim.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: '编号', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {
            title: '订单号',
            field: 'orderno',
            width: '200',
            visible: true,
            align: 'center',
            formatter: orderFormatter,
            valign: 'middle'
        },
        {title: '推送4S门店', field: 'fullname', width: '100', visible: true, align: 'center', valign: 'middle'},
        {title: '客户手机号', field: 'phone', visible: true, width: '100', align: 'center', valign: 'middle'},
        {title: '客户姓名', field: 'name', visible: true, width: '100', align: 'center', valign: 'middle'},
        {title: '车牌号', field: 'cph', width: '100', visible: true, width: '150', align: 'center', valign: 'middle'},
        {title: '车架号', field: 'cjh', width: '100', visible: false, align: 'center', valign: 'middle'},
        {title: '汽车品牌', field: 'qcpp', width: '100', visible: true, align: 'center', valign: 'middle'},
        {title: '承诺客户', field: 'promise', width: '100', visible: true, align: 'center', valign: 'middle'},
        {title: '订单状态', field: 'statusName', width: '100', visible: true, align: 'center', valign: 'middle'},
        {title: '描述', field: 'desc', width: '100', visible: true, align: 'center', valign: 'middle'},
        {title: '经度', field: 'lng', width: '100', visible: false, align: 'center', valign: 'middle'},
        {title: '纬度', field: 'lat',width: '100',  visible: false, align: 'center', valign: 'middle'},
        {title: '定损金额', field: 'fixloss', width: '100', visible: true, align: 'center', valign: 'middle'},
        {title: '定位地址名称', field: 'address', width: '300', visible: true, align: 'center', valign: 'middle'},
        // {title: '理赔图片', field: 'claimImg', visible: true, align: 'center', valign: 'middle'},
        {title: '付款凭证号', field: 'payBillNo',width: '100',  visible: false, align: 'center', valign: 'middle'},
        // {title: '预交车时间', field: 'yjcsj', visible: true, align: 'center', valign: 'middle'},
        {title: '开单时间', field: 'createtime', visible: true, width: '200', align: 'center', valign: 'middle'},
        {title: '银行卡号', field: 'bankcard', width: '100', visible: false, align: 'center', valign: 'middle'},
        {title: '开户名', field: 'bankUserName', width: '100', visible: false, align: 'center', valign: 'middle'},
        {title: '开户银行', field: 'bankName', width: '100', visible: false, align: 'center', valign: 'middle'},
        {title: '开户银行支行', width: '100', field: 'bankSecondName', visible: false, align: 'center', valign: 'middle'},
        {title: '理赔顾问身份证', width: '100', field: 'idcard', visible: false, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
PartnerOpenClaim.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        PartnerOpenClaim.seItem = selected[0];
        return true;
    }
};
/**
 * 停止通知声音播放
 */
PartnerOpenClaim.stop = function () {
    window.parent.document.getElementById('alarm').pause();
};

function showDetail(orderNo) {
    var index = layer.open({
        type: 2,
        title: false,
        // shadeClose: true,
        // area: ['1600px', '700px'],
        // offset: 'auto',
        area: ['1080px', '450px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/openClaim/openClaim_detail/' + orderNo
    });
    this.layerIndex = index;
}

function orderFormatter(value, row, index) {
    return [
        '<a href="javascript:void(0);" onclick="showDetail(\'' + value + '\')" >' + value + '</a>'
    ].join("")
}

/**
 * 接车确认
 */
PartnerOpenClaim.openAcceptOpenClaim = function () {
    if (this.check()) {
        var claimOrderId = this.seItem.id;
        var statusName = this.seItem.statusName;
        if (statusName != '未接车') {
            Feng.error("该订单已接车，请勿重复操作!");
            return;
        }
        var ajax = new $ax(Feng.ctxPath + "/openClaim/partner/accept", function (data) {
            Feng.success("操作成功!");
            PartnerOpenClaim.table.refresh();
        }, function (data) {
            Feng.error("操作失败!" + data.responseJSON.message + "!");
        });
        ajax.set("id", claimOrderId);
        ajax.start();
    }
};

/**
 * 交车确认
 */
PartnerOpenClaim.openPlacedOpenClaim = function () {
    if (this.check()) {
        var claimOrderId = this.seItem.id;
        var statusName = this.seItem.statusName;
        if (statusName != '服务中') {
            Feng.error("只能操作服务中的订单!");
            return;
        }
        var ajax = new $ax(Feng.ctxPath + "/openClaim/partner/placed", function (data) {
            Feng.success("操作成功!");
            PartnerOpenClaim.table.refresh();
        }, function (data) {
            Feng.error("操作失败!" + data.responseJSON.message + "!");
        });
        ajax.set("id", claimOrderId);
        ajax.start();
    }
};

/**
 * 添加定损金额
 */
PartnerOpenClaim.openAddOpenClaimFixLoss = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '录入定损金额',
            area: ['400px', '400px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/openClaim/openClaim_addFixLoss/' + this.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 添加异常反馈
 */
PartnerOpenClaim.reportEx = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '异常反馈',
            area: ['400px', '400px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/openClaim/openClaim_addReportEx/' + this.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 确认结算，添加结算对账单凭证
 */
PartnerOpenClaim.openPayedOpenClaim = function () {
    if (this.check()) {
        // var index = layer.open({
        //     type: 2,
        //     title: '录入支付转账凭证',
        //     area: ['400px', '400px'], //宽高
        //     fix: false, //不固定
        //     maxmin: true,
        //     content: Feng.ctxPath + '/openClaim/openClaim_addPayBillNo/' + this.seItem.id
        // });
        // this.layerIndex = index;


        var ajax = new $ax(Feng.ctxPath + "/openClaim/partner/addPayBillNo", function (data) {
            Feng.success("操作成功!");
            window.parent.PartnerOpenClaim.table.refresh();
            OpenClaimInfoDlg.close();
        }, function (data) {
            Feng.error("操作失败!" + data.responseJSON.message + "!");
        });
        ajax.set('id', this.seItem.id);
        ajax.start();
    }
};

/**
 * 导出excel
 */
PartnerOpenClaim.export = function () {
    var queryStr = "";
    queryStr = queryStr + ($("#condition").val() ? 'condition=' + $("#condition").val() : "");
    queryStr = queryStr + ($("#orderno").val() ? 'orderno=' + $("#orderno").val() : "");
    queryStr = queryStr + ($("#orderStatus").val() ? 'status=' + $("#orderStatus").val() : "");
    var createTimeRange = $("#createStartTime").val();
    if (createTimeRange) {
        queryStr = queryStr + 'createStartTime=' + createTimeRange.split('~')[0];
        queryStr = queryStr + 'createEndTime=' + createTimeRange.split('~')[1];
    }
    window.location.href = Feng.ctxPath + "/openClaim/partner/list/export?" + queryStr;
}

/**
 * 打开查看详情
 */
PartnerOpenClaim.openOpenClaimDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/openClaim/openClaim_update/' + PartnerOpenClaim.seItem.id
        });
        this.layerIndex = index;
    }
};
PartnerOpenClaim.initParam = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    queryData['orderno'] = $("#orderno").val();
    queryData['status'] = $("#orderStatus").val();
    var createTimeRange = $("#createStartTime").val();
    if (createTimeRange) {
        queryData['createStartTime'] = createTimeRange.split('~')[0];
        queryData['createEndTime'] = createTimeRange.split('~')[1];
    }
    return queryData;
};
PartnerOpenClaim.queryFixLossSum = function () {
    var ajax = new $ax(Feng.ctxPath + "/openClaim/partner/fixLossSum", function (data) {
        $('#fixLossSumDiv').val(data);
    }, function (data) {
    });
    ajax.set(PartnerOpenClaim.initParam());
    ajax.start();
}

/**
 * 查询列表
 */
PartnerOpenClaim.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    queryData['orderno'] = $("#orderno").val();
    queryData['status'] = $("#orderStatus").val();
    var createTimeRange = $("#createStartTime").val();
    if (createTimeRange) {
        queryData['createStartTime'] = createTimeRange.split('~')[0];
        queryData['createEndTime'] = createTimeRange.split('~')[1];
    }
    PartnerOpenClaim.table.refresh({query: queryData});
    PartnerOpenClaim.queryFixLossSum();
};

$(function () {
    var defaultColunms = PartnerOpenClaim.initColumn();
    var table = new BSTable(PartnerOpenClaim.id, "/openClaim/partner/list", defaultColunms);
    table.setPaginationType("server");
    PartnerOpenClaim.table = table.init();
    PartnerOpenClaim.queryFixLossSum();
});

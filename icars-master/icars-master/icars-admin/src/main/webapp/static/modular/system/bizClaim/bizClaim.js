/**
 * 管理初始化
 */
var BizClaim = {
    id: "BizClaimTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
BizClaim.initColumn = function () {
    return [
            {field: 'selectItem', radio: true},
            {title: '主键', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '客户openid', field: 'openid', visible: false, align: 'center', valign: 'middle'},
            {title: '客户手机号', field: 'phone', visible: true, align: 'center', valign: 'middle'},
            {title: '客户姓名', field: 'name', visible: true, align: 'center', valign: 'middle'},
            {title: '车牌号', field: 'cph', visible: true, align: 'center', valign: 'middle'},
            {title: '理赔单类型', field: 'type', visible: true, align: 'center', valign: 'middle'},
            {title: '理赔状态', field: 'status', visible: true, align: 'center', valign: 'middle'},
            {title: '描述', field: 'desc', visible: true, align: 'center', valign: 'middle'},
            {title: '理赔负责人账号', field: 'claimer', visible: false, align: 'center', valign: 'middle'},
            {title: '理赔顾问', field: 'claimerName', visible: true, align: 'center', valign: 'middle'},
            {title: '理赔顾问电话', field: 'claimerPhone', visible: true, align: 'center', valign: 'middle'},
            {title: '定位地图', field: 'mapUrl', align: 'center', valign: 'middle',formatter: urlFormatter, sortable: false},
            {title: '经度', field: 'lng', visible: false, align: 'center', valign: 'middle'},
            {title: '纬度', field: 'lat', visible: false, align: 'center', valign: 'middle'},
            {title: '定位地址名称', field: 'address', width: '300', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createtime', width: '100', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
BizClaim.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        BizClaim.seItem = selected[0];
        return true;
    }
};
function urlFormatter(value, row, index) {
    return [
        '<a href="'+value+'" target="view_window">点击查看地图</a>'
    ].join("")
}
/**
 * 点击添加
 */
BizClaim.openAddBizClaim = function () {
    var index = layer.open({
        type: 2,
        title: '添加',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/bizClaim/bizClaim_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看详情
 */
BizClaim.openBizClaimDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/bizClaim/bizClaim_update/' + BizClaim.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除
 */
BizClaim.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/bizClaim/delete", function (data) {
            Feng.success("删除成功!");
            BizClaim.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("bizClaimId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询列表
 */
BizClaim.search = function () {
    var queryData = {};
    queryData['name'] = $("#name").val();
    queryData['phone'] = $("#phone").val();
    queryData['claimer'] = $("#claimer").val();
    queryData['status'] = $("#status").val();
    queryData['type'] = $("#type").val();
    var createTimeRange = $("#createStartTime").val();
    if(createTimeRange){
        queryData['startTime'] = createTimeRange.split('~')[0];
        queryData['endTime'] = createTimeRange.split('~')[1];
    }
    BizClaim.table.refresh({query: queryData});
};

/**
 * 点击弹出推送框 推送理赔老师
 */
BizClaim.pushClaims = function () {
    if (this.check()){
        var index = layer.open({
            type: 2,
            title: '推送理赔顾问',
            area: ['800px', '560px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/bizClaim/claims_push_claims'
        });
        this.layerIndex = index;
    }
};
/**
 * 停止通知声音播放
 */
BizClaim.stop = function () {
    window.parent.document.getElementById('alarm_claim').pause();
};
$(function () {
    var defaultColunms = BizClaim.initColumn();
    var table = new BSTable(BizClaim.id, "/bizClaim/list", defaultColunms);
    table.setPaginationType("server");
    BizClaim.table = table.init();
});

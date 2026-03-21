/**
 * 管理初始化
 */
var PushRecord = {
    id: "PushRecordTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
PushRecord.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '主键id', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '员工账号', field: 'account', visible: true, align: 'center', valign: 'middle'},
            {title: '员工姓名', field: 'name', visible: true, align: 'center', valign: 'middle'},
            {title: '员工部门', field: 'fullname', visible: true, align: 'center', valign: 'middle'},
            {title: '事故主键id', field: 'accid', visible: true, align: 'center', valign: 'middle'},
            {title: '视频', field: 'video', visible: true, align: 'center', formatter: aFormatter, valign: 'middle'},
            {title: '地址', field: 'address', visible: true, align: 'center', width: '300', valign: 'middle'},
            {title: '状态', field: 'status', visible: true, align: 'center', valign: 'middle'},
            {title: '推送时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '修改时间', field: 'modifyTime', visible: true, align: 'center', valign: 'middle'}
    ];
};
function aFormatter(value, row, index) {
    return [
        '<a href="javascript:void(0);" onclick="showVideo(\''+value+'\')" >查看视频</a>'
    ].join("")
}
function showVideo(val){
    $('#video').attr("src",val);
    layer.open({
        type: 1,
        title: false,
        shadeClose: true,
        area: ['400px', '350px'],
        content: $('#box')
    });
}
/**
 * 查询表单提交参数对象
 * @returns {{}}
 */
PushRecord.formParams = function() {
    var queryData = {};
    queryData['account'] = $("#account").val();
    queryData['pushStartTime'] = $("#pushStartTime").val();
    queryData['pushEndTime'] = $("#pushEndTime").val();
    return queryData;
}

/**
 * 检查是否选中
 */
PushRecord.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        PushRecord.seItem = selected[0];
        return true;
    }
};


/**
 * 删除
 */
PushRecord.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/pushRecord/delete", function (data) {
            Feng.success("删除成功!");
            PushRecord.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("pushRecordId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询列表
 */
PushRecord.search = function () {
    var queryData = {};
    queryData['account'] = $("#account").val();
    queryData['pushStartTime'] = $("#pushStartTime").val();
    queryData['pushEndTime'] = $("#pushEndTime").val();
    PushRecord.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = PushRecord.initColumn();
    var table = new BSTable(PushRecord.id, "/pushRecord/list", defaultColunms);
    table.setPaginationType("server");
    table.setQueryParams(PushRecord.formParams());
    PushRecord.table = table.init();
});

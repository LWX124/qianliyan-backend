/**
 * 管理初始化
 */
var PushRecordFS = {
    id: "PushRecordFSTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
PushRecordFS.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '主键id', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '事故主键id', field: 'accid', visible: false, align: 'center', valign: 'middle'},
            {title: '序号',field: '',align: 'center',formatter: function (value, row, index) {
                    var pageSize = PushRecordFS.table.btInstance.bootstrapTable('getOptions').pageSize;     //通过table的#id 得到每页多少条
                    var pageNumber = PushRecordFS.table.btInstance.bootstrapTable('getOptions').pageNumber; //通过table的#id 得到当前第几页
                    return pageSize * (pageNumber - 1) + index + 1;    // 返回每条的序号： 每页条数 *（当前页 - 1 ）+ 序号
                }
            },
            {title: '员工账号', field: 'account', visible: false, align: 'center', valign: 'middle'},
            {title: '员工部门', field: 'fullname', visible: true, align: 'center', valign: 'middle'},
            {title: '视频', field: 'video', visible: true, align: 'center', formatter: aFormatter, valign: 'middle'},
            {title: '地址', field: 'address', visible: true, align: 'center', valign: 'middle'},
            {title: '状态', field: 'status', visible: true, align: 'center', valign: 'middle'},
            {title: '推送时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '修改时间', field: 'modifyTime', visible: true, align: 'center', valign: 'middle'}
    ];
};
function aFormatter(value, row, index) {
    return [
        '<a href="javascript:void(0);" onclick="showVideo(\''+value+'\')">查看视频</a>'
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
    setTimeout(function () {
        if(PushRecordFS.check()){
            if(PushRecordFS.seItem.status == '未查看'){
                PushRecordFS.changeStatus(PushRecordFS.seItem.id);
            }
        }
    },500)
}

/**
 * 查询表单提交参数对象
 * @returns {{}}
 */
PushRecordFS.formParams = function() {
    var queryData = {};
    queryData['pushStartTime'] = $("#pushStartTime").val();
    queryData['pushEndTime'] = $("#pushEndTime").val();
    return queryData;
}

/**
 * 检查是否选中
 */
PushRecordFS.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        PushRecordFS.seItem = selected[0];
        return true;
    }
};

/**
 * 停止通知声音播放
 */
PushRecordFS.stop = function () {
    window.parent.document.getElementById('alarm').pause();
};

/**
 * 更改推送记录为已查看
 */
PushRecordFS.changeStatus = function (id) {
    if (id) {
        var ajax = new $ax(Feng.ctxPath + "/pushRecord/changeStatus", function (data) {
        }, function (data) {
        });
        ajax.set("id",id);
        ajax.start();
    }
};

/**
 * 查询列表
 */
PushRecordFS.search = function () {
    var queryData = {};
    queryData['pushStartTime'] = $("#pushStartTime").val();
    queryData['pushEndTime'] = $("#pushEndTime").val();
    PushRecordFS.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = PushRecordFS.initColumn();
    var table = new BSTable(PushRecordFS.id, "/pushRecord/pushFS/list", defaultColunms);
    table.setPaginationType("server");
    table.setQueryParams(PushRecordFS.formParams());
    PushRecordFS.table = table.init();
});

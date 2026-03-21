/**
 * 管理初始化
 */
var BizClaimerShow = {
    id: "BizClaimerShowTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
BizClaimerShow.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '主键', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '姓名', field: 'name', visible: true, align: 'center', valign: 'middle'},
            {title: '头像', field: 'imgUrl', visible: true, align: 'center', formatter: imgFormatter,valign: 'middle'},
            {title: '座右铭', field: 'motto', visible: true, align: 'center', valign: 'middle'},
            {title: '事迹简介', field: 'story', visible: true, align: 'center', valign: 'middle'},
            {title: '展示状态', field: 'status', visible: true, align: 'center',formatter: aFormatter, valign: 'middle'},
            {title: '创建时间', field: 'createtime', visible: true, align: 'center', valign: 'middle'}
    ];
};
function aFormatter(value, row, index) {
    if(value == 0){
        return [
            '展示中'
        ].join("")
    }else {
        return [
            '下架'
        ].join("")
    }

}

function showImage(val){
    $('#imgTag').attr("src",val);
    layer.open({
        type: 1,
        title: false,
        shadeClose: true,
        area: ['1120px', '750px'],
        offset: 'auto',
        content: $('#imgDiv')
    });
}


function imgFormatter(value, row, index) {
    return [
        '<a href="javascript:void(0);" onclick="showImage(\''+value+'\')" >查看图片</a>'
    ].join("")
}
/**
 * 检查是否选中
 */
BizClaimerShow.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        BizClaimerShow.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加
 */
BizClaimerShow.openAddBizClaimerShow = function () {
    var index = layer.open({
        type: 2,
        title: '添加',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/bizClaimerShow/bizClaimerShow_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看详情
 */
BizClaimerShow.openBizClaimerShowDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/bizClaimerShow/bizClaimerShow_update/' + BizClaimerShow.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除
 */
BizClaimerShow.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/bizClaimerShow/delete", function (data) {
            Feng.success("删除成功!");
            BizClaimerShow.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("bizClaimerShowId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询列表
 */
BizClaimerShow.search = function () {
    var queryData = {};
    queryData['name'] = $("#name").val();
    queryData['status'] = $("#status").val();
    BizClaimerShow.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = BizClaimerShow.initColumn();
    var table = new BSTable(BizClaimerShow.id, "/bizClaimerShow/list", defaultColunms);
    table.setPaginationType("client");
    BizClaimerShow.table = table.init();

});

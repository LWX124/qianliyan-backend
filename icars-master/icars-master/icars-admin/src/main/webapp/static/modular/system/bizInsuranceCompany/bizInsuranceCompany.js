/**
 * 管理初始化
 */
var BizInsuranceCompany = {
    id: "BizInsuranceCompanyTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
BizInsuranceCompany.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '编号', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '公司名称', field: 'name', visible: true, align: 'center', valign: 'middle'},
            {title: '公司图标', field: 'url', visible: true, align: 'center', formatter: aFormatter, valign: 'middle'}
    ];
};
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


function aFormatter(value, row, index) {
    return [
        '<a href="javascript:void(0);" onclick="showImage(\''+value+'\')" >查看图片</a>'
    ].join("")
}
/**
 * 检查是否选中
 */
BizInsuranceCompany.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        BizInsuranceCompany.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加
 */
BizInsuranceCompany.openAddBizInsuranceCompany = function () {
    var index = layer.open({
        type: 2,
        title: '添加',
        area: ['720px', '560px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/bizInsuranceCompany/bizInsuranceCompany_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看详情
 */
BizInsuranceCompany.openBizInsuranceCompanyDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '详情',
            area: ['720px', '560px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/bizInsuranceCompany/bizInsuranceCompany_update/' + BizInsuranceCompany.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除
 */
BizInsuranceCompany.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/bizInsuranceCompany/delete", function (data) {
            Feng.success("删除成功!");
            BizInsuranceCompany.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("bizInsuranceCompanyId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询列表
 */
BizInsuranceCompany.search = function () {
    var queryData = {};
    queryData['name'] = $("#name").val();
    BizInsuranceCompany.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = BizInsuranceCompany.initColumn();
    var table = new BSTable(BizInsuranceCompany.id, "/bizInsuranceCompany/list", defaultColunms);
    table.setPaginationType("client");
    BizInsuranceCompany.table = table.init();
});

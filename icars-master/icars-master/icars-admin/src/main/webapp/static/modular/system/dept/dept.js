/**
 * 部门管理初始化
 */
var Dept = {
    id: "DeptTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Dept.initColumn = function () {
    var columns =  [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle',width:'50px'},
        {title: '部门全称', field: 'fullname', width: '300', align: 'center', valign: 'middle', sortable: true},
        {title: '排序', field: 'num', align: 'center', visible: false, valign: 'middle', sortable: true},
        {title: '经度', field: 'lng', visible: false, align: 'center', valign: 'middle'},
        {title: '纬度', field: 'lat', visible: false, align: 'center', valign: 'middle'},
        {title: '图片', field: 'imgUrls', align: 'center', valign: 'middle',formatter: aFormatter, sortable: false},
        {title: '服务品牌', field: 'tips', width: '300', align: 'center', valign: 'middle', sortable: true},
        {title: '电话', field: 'synopsis', align: 'center', valign: 'middle', sortable: true},
        {title: '地址', field: 'address', width: '300', align: 'center', valign: 'middle', sortable: true}];
    return columns;
};
function showImage(val){
    $('#pointol').empty();
    $('#imgDiv').empty();

    var imgs = val.split('|');
    imgs.forEach(function( val, index ) {
        if(index == 0){
            $('<li data-target="#myCarousel" data-slide-to="'+index+'" class="active"></li>').appendTo($('#pointol'));
            $('<div class="item active"><img src="'+val+'"></div>').appendTo($('#imgDiv'));
        }else {
            $('<li data-target="#myCarousel" data-slide-to="'+index+'"></li>').appendTo($('#pointol'));
            $('<div class="item"><img src="'+val+'"></div>').appendTo($('#imgDiv'));
        }
    });
    layer.open({
        type: 1,
        title: false,
        shadeClose: true,
        area: ['640px', '750px'],
        offset: 'auto',
        content: $('#myCarousel')
    });
}
function aFormatter(value, row, index) {
    if(value == null || value == undefined || value.length < 1){
        return '';
    }
    return [
        '<a href="javascript:void(0);" onclick="showImage(\''+value+'\')" >查看明细</a>'
    ].join("")
}
/**
 * 检查是否选中
 */
Dept.check = function () {
    var selected = $('#' + this.id).bootstrapTreeTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Dept.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加部门
 */
Dept.openAddDept = function () {
    var index = layer.open({
        type: 2,
        title: '添加部门',
        area: ['900px', '800px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/dept/dept_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看部门详情
 */
Dept.openDeptDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '部门详情',
            area: ['800px', '800px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/dept/dept_update/' + Dept.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除部门
 */
Dept.delete = function () {
    if (this.check()) {

        var operation = function(){
            var ajax = new $ax(Feng.ctxPath + "/dept/delete", function () {
                Feng.success("删除成功!");
                Dept.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("deptId",Dept.seItem.id);
            ajax.start();
        };

        Feng.confirm("是否刪除该部门?", operation);
    }
};

/**
 * 查询部门列表
 */
Dept.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Dept.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Dept.initColumn();
    var table = new BSTreeTable(Dept.id, "/dept/list", defaultColunms);
    table.setExpandColumn(2);
    table.setIdField("id");
    table.setCodeField("id");
    table.setParentCodeField("pid");
    table.setExpandAll(true);
    Dept.table = table.init();;
});

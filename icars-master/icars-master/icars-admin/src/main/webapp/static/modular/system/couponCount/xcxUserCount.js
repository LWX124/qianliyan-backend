var wxUser = {
    id: "wxUser",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};


/**
 * 初始化表格的列
 */
wxUser.initColumn = function () {
    return [
        {field: 'selectItem', radio: false},
        {title: '时间', field: 'date', visible: true, align: 'center', valign: 'middle'},
        {title: '新增用户', field: 'number', visible: true, align: 'center', valign: 'middle'}
    ];
};



/**
 * 查询coupon列表
 */
wxUser.search = function () {
    var queryData = {};
    var time = $("#time").val();
    if (time) {
        queryData['startTime'] = time.split('~')[0];
        queryData['endTime'] = time.split('~')[1];
    }
    console.log(queryData);
    wxUser.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = wxUser.initColumn();
    console.log("defaultColunms=" + defaultColunms);
    var table = new BSTable(wxUser.id, "/xcxUserCount/list", defaultColunms);
    table.setPaginationType("client");
    wxUser.table = table.init();
});

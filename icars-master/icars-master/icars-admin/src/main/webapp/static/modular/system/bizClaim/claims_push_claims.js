/**
 *
 */
var MgrClaimsPushClaims = {
    id: "managerClaimsPushClaimsTable",//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
MgrClaimsPushClaims.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true},
        {title: '账户', field: 'account',  align: 'center', valign: 'middle'},
        {title: 'deptid', field: 'deptid', visible: false, align: 'center', valign: 'middle', sortable: true},
        {title: '名称', field: 'name', align: 'center', valign: 'middle', sortable: true},
        {title: '手机号', field: 'phone', align: 'center', valign: 'middle', sortable: true},
        {title: '公司部门', field: 'deptName', align: 'center', valign: 'middle', width: '300', sortable: true}];
    return columns;
};
/**
 * 检查是否选中
 */
MgrClaimsPushClaims.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        MgrClaimsPushClaims.seItem = selected[0];
        return true;
    }
};
/**
 * 推送
 * @param
 */
MgrClaimsPushClaims.pushClaims = function () {
    if (this.check()) {
        var account = this.seItem.account;
        var claimsID = window.parent.BizClaim.seItem.id;
        var ajax = new $ax(Feng.ctxPath + "/bizClaim/pushClaims", function (data) {
            if(data.code == 500){
                Feng.error(data.message + "!");
                return;
            }
            Feng.success("操作成功!");
            MgrClaimsPushClaims.close();
            window.parent.BizClaim.table.refresh();
        }, function (data) {
            Feng.error("操作失败!" + data.responseJSON.message + "!");
        });
        ajax.set("id", claimsID);
        ajax.set("account", account);
        ajax.start();
    }
};

/**
 * 关闭此对话框
 */
MgrClaimsPushClaims.close = function () {
    parent.layer.close(window.parent.BizClaim.layerIndex);
};
MgrClaimsPushClaims.search = function () {
    var queryData = {};
    queryData['name'] = $("#name").val();
    MgrClaimsPushClaims.table.refresh({query: queryData});
}
MgrClaimsPushClaims.resetSearch = function () {
    $("#name").val("");
    MgrClaimsPushClaims.search();
}
/**
 * 查询表单提交参数对象
 * @returns {{}}
 */
MgrClaimsPushClaims.formParams = function() {
    var queryData = {};
    queryData['name'] = $("#name").val();
    return queryData;
}

$(function () {
    var defaultColunms = MgrClaimsPushClaims.initColumn();
    var table = new BSTable("managerClaimsPushClaimsTable", "/accid/pushClaimsList", defaultColunms);
    table.setPaginationType("client");
    table.setQueryParams(MgrClaimsPushClaims.formParams());
    MgrClaimsPushClaims.table = table.init();
});
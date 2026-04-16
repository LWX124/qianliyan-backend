/**
 *
 */
var RedPayChoose = {
    id: "managerRedPayChooseTable",//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
RedPayChoose.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true},
        {title: '红包金额', field: 'amount', align: 'center', valign: 'middle', sortable: true}];
    return columns;
};

/**
 * 检查是否选中
 */
RedPayChoose.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        RedPayChoose.seItem = selected[0];
        return true;
    }
};
/**
 * 审核通过
 * @param
 */
RedPayChoose.checkSuccess = function () {
    if (this.check()) {
        var amount = this.seItem.amount;
        var accid = window.parent.MgrAccd.seItem.id;
        var reason = $("#reason").val();
        var ajax = new $ax(Feng.ctxPath + "/accid/checkSuccess", function (data) {
            if(data.code == 500){
                Feng.error(data.message + "!");
                MgrAccd.table.refresh();
                return;
            }
            Feng.success("操作成功!");
            // 审核操作后自动停止语音播报
            try { window.parent.document.getElementById('alarm').pause(); } catch(e) {}
            RedPayChoose.close();
            window.parent.MgrAccd.table.refresh();
        }, function (data) {
            Feng.error("操作失败!" + data.responseJSON.message + "!");
        });
        ajax.set("accdId", accid);
        ajax.set("amount", amount);
        ajax.set('reason', reason);
        ajax.start();
    }
};

/**
 * 关闭此对话框
 */
RedPayChoose.close = function () {
    parent.layer.close(window.parent.MgrAccd.layerIndex);
};
RedPayChoose.search = function () {
    var queryData = {};
    RedPayChoose.table.refresh({query: queryData});
}
/**
 * 查询表单提交参数对象
 * @returns {{}}
 */
RedPayChoose.formParams = function() {
    var queryData = {};
    return queryData;
}

$(function () {
    var defaultColunms = RedPayChoose.initColumn();
    var table = new BSTable("managerRedPayChooseTable", "/bizWxSalary/list", defaultColunms);
    table.setPaginationType("client");
    table.setQueryParams(RedPayChoose.formParams());
    RedPayChoose.table = table.init();
});
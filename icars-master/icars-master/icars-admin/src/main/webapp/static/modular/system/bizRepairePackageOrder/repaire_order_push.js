/**
 *
 */
var BizRepairePackageOrderPush = {
    id: "managerRepairePushTable",//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};
// var MgrAccdPush = {
//     userInfoData: {},
//     validateFields: {
//         account: {
//             validators: {
//                 notEmpty: {
//                     message: '账户不能为空'
//                 }
//             }
//         },
//         phone: {
//             validators: {
//                 notEmpty: {
//                     message: '手机号不能为空'
//                 }
//             }
//         },
//         name: {
//             validators: {
//                 notEmpty: {
//                     message: '姓名不能为空'
//                 }
//             }
//         },
//         citySel: {
//             validators: {
//                 notEmpty: {
//                     message: '部门不能为空'
//                 }
//             }
//         },
//         password: {
//             validators: {
//                 notEmpty: {
//                     message: '密码不能为空'
//                 },
//                 identical: {
//                     field: 'rePassword',
//                     message: '两次密码不一致'
//                 },
//             }
//         },
//         rePassword: {
//             validators: {
//                 notEmpty: {
//                     message: '密码不能为空'
//                 },
//                 identical: {
//                     field: 'password',
//                     message: '两次密码不一致'
//                 },
//             }
//         }
//     }
// };
/**
 * 初始化表格的列
 */
BizRepairePackageOrderPush.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true},
        {title: '账户', visible: false, field: 'account',  align: 'center', valign: 'middle'},
        {title: '名称', field: 'name', width: '100' ,align: 'center', valign: 'middle', sortable: true},
        {title: '手机号', field: 'phone', width: '100' ,align: 'center', valign: 'middle', sortable: true},
        // {title: '预存款余额', field: 'balance', width: '120' ,align: 'center', valign: 'middle', sortable: true},
        {title: '公司部门', field: 'deptName', align: 'center', valign: 'middle', width: '300', sortable: true}];
    return columns;
};
/**
 * 检查是否选中
 */
BizRepairePackageOrderPush.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        BizRepairePackageOrderPush.seItem = selected[0];
        return true;
    }
};
/**
 * 推送
 * @param
 */
BizRepairePackageOrderPush.push = function () {
    if (this.check()) {
        var account = this.seItem.account;
        var orderid = window.parent.BizRepairePackageOrder.seItem.id;
        var ajax = new $ax(Feng.ctxPath + "/bizRepairePackageOrder/pushRepaireman", function (data) {
            if(data.code == 500){
                Feng.error(data.message + "!");
                return;
            }
            Feng.success("操作成功!");
            BizRepairePackageOrderPush.close();
            window.parent.BizRepairePackageOrder.table.refresh();
        }, function (data) {
            Feng.error("操作失败!" + data.responseJSON.message + "!");
        });
        ajax.set("orderid", orderid);
        ajax.set("account", account);
        ajax.start();
    }
};

/**
 * 关闭此对话框
 */
BizRepairePackageOrderPush.close = function () {
    parent.layer.close(window.parent.BizRepairePackageOrder.layerIndex);
};
BizRepairePackageOrderPush.search = function () {
    var queryData = {};
    queryData['name'] = $("#name").val();
    BizRepairePackageOrderPush.table.refresh({query: queryData});
}
BizRepairePackageOrderPush.resetSearch = function () {
    $("#name").val("");
    BizRepairePackageOrderPush.search();
}
/**
 * 查询表单提交参数对象
 * @returns {{}}
 */
BizRepairePackageOrderPush.formParams = function() {
    var queryData = {};
    queryData['name'] = $("#name").val();
    return queryData;
}

$(function () {
    var defaultColunms = BizRepairePackageOrderPush.initColumn();
    var table = new BSTable("managerRepairePushTable", "/accid/pushMaintainList", defaultColunms);
    table.setPaginationType("client");
    table.setQueryParams(BizRepairePackageOrderPush.formParams());
    BizRepairePackageOrderPush.table = table.init();
});
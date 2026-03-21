/**
 *
 */
var MgrAccdPushClaims = {
    id: "managerAccdPushClaimsTable",//表格id
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
 * 初始化事故等级选择框
 */
MgrAccdPushClaims.initAccLevelSelection = function() {

    var ajax = new $ax(Feng.ctxPath + "/bizAccidLevel/list", function(data){
        for (var i = 0; i < data.length; i++) {
            $('#accLevel').append("<option value=" + data[i].amount + ">" + data[i].level + "</option>");
        }
        // 缺一不可
        $('#accLevel').selectpicker('refresh');
        $('#accLevel').selectpicker('render');
    },function(data){
        Feng.error("加载选项失败!" + data.responseJSON.message + "!");
    });
    ajax.start();
}
/**
 * 初始化表格的列
 */
MgrAccdPushClaims.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true},
        {title: '账户', visible: false, field: 'account',  align: 'center', valign: 'middle'},
        {title: 'deptid', field: 'deptid', width: '100' ,visible: false, align: 'center', valign: 'middle', sortable: true},
        {title: '名称', field: 'name', width: '100' ,align: 'center', valign: 'middle', sortable: true},
        {title: '手机号', field: 'phone', width: '100' ,align: 'center', valign: 'middle', sortable: true},
        {title: '预存款余额', field: 'balance', width: '120' ,align: 'center', valign: 'middle', sortable: true},
        {title: '公司部门', field: 'deptName', align: 'center', valign: 'middle', width: '300', sortable: true}];
    return columns;
};
/**
 * 检查是否选中
 */
MgrAccdPushClaims.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        MgrAccdPushClaims.seItem = selected[0];
        return true;
    }
};
/**
 * 推送
 * @param
 */
MgrAccdPushClaims.pushClaims = function () {
    if (this.check()) {
        var deptid = this.seItem.deptid;
        var account = this.seItem.account;
        var accid = window.parent.MgrAccd.seItem.id;
        var accLevelValue = $('#accLevel').find("option:selected")[0].value;
        var ajax = new $ax(Feng.ctxPath + "/accid/pushClaims", function (data) {
            if(data.code == 500){
                Feng.error(data.message + "!");
                return;
            }
            Feng.success("操作成功!");
            MgrAccdPush.close();
        }, function (data) {
            Feng.error("操作失败!" + data.responseJSON.message + "!");
        });
        ajax.set("accdId", accid);
        ajax.set("deptid", deptid);
        ajax.set("account", account);
        ajax.set("accLevelValue", accLevelValue);
        ajax.start();
    }
};

/**
 * 关闭此对话框
 */
MgrAccdPushClaims.close = function () {
    parent.layer.close(window.parent.MgrAccd.layerIndex);
};
MgrAccdPushClaims.search = function () {
    var queryData = {};
    queryData['name'] = $("#name").val();
    queryData['province'] = $("#province").text();
    queryData['city'] = $("#city").text();
    queryData['area'] = $('#district').text();
    MgrAccdPushClaims.table.refresh({query: queryData});
}
MgrAccdPushClaims.resetSearch = function () {
    $("#name").val("");
    MgrAccdPushClaims.search();
}
/**
 * 查询表单提交参数对象
 * @returns {{}}
 */
MgrAccdPushClaims.formParams = function() {
    var queryData = {};
    queryData['province'] = $("#province").text();
    queryData['city'] = $("#city").text();
    queryData['area'] = $('#district').text();
    queryData['name'] = $("#name").val();
    return queryData;
}

$(function () {
    var defaultColunms = MgrAccdPushClaims.initColumn();
    var table = new BSTable("managerAccdPushClaimsTable", "/accid/pushClaimsList", defaultColunms);
    MgrAccdPushClaims.initAccLevelSelection();
    table.setPaginationType("client");
    table.setQueryParams(MgrAccdPushClaims.formParams());
    MgrAccdPushClaims.table = table.init();
});
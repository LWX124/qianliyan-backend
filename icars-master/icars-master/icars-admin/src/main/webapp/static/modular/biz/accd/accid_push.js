/**
 *
 */
var MgrAccdPush = {
    id: "managerAccdPushTable",//表格id
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
MgrAccdPush.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '名称', field: 'name', align: 'center', valign: 'middle', sortable: true},
        {title: '服务品牌', field: 'tips', align: 'center', valign: 'middle', sortable: true},
        {title: '距离', field: 'distance', align: 'center', valign: 'middle', width: '300', sortable: true}];
    return columns;
};
/**
 * 检查是否选中
 */
MgrAccdPush.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        MgrAccdPush.seItem = selected[0];
        return true;
    }
};
/**
 * 推送
 * @param
 */
MgrAccdPush.pushFours = function () {
    if (this.check()) {
        var deptid = this.seItem.id;
        var accid = window.parent.MgrAccd.seItem.id;
        var ajax = new $ax(Feng.ctxPath + "/accid/pushFours", function (data) {
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
        ajax.start();
    }
};

/**
 * 关闭此对话框
 */
MgrAccdPush.close = function () {
    parent.layer.close(window.parent.MgrAccd.layerIndex);
};
MgrAccdPush.search = function () {
    var queryData = {};
    queryData['name'] = $("#name").val();
    queryData['carType'] = $("#carType").val();
    queryData['accid'] = window.parent.MgrAccd.seItem.id;
    MgrAccdPush.table.refresh({query: queryData});
}
MgrAccdPush.resetSearch = function () {
    $("#name").val("");
    $("#carType").val("");
    MgrAccdPush.search();
}
/**
 * 查询表单提交参数对象
 * @returns {{}}
 */
MgrAccdPush.formParams = function() {
    var queryData = {};
    queryData['name'] = $("#name").val();
    queryData['carType'] = $("#carType").val();
    queryData['accid'] = window.parent.MgrAccd.seItem.id;
    return queryData;
}

$(function () {
    var defaultColunms = MgrAccdPush.initColumn();
    var table = new BSTable("managerAccdPushTable", "/accid/pushFsList", defaultColunms);
    table.setPaginationType("client");
    table.setQueryParams(MgrAccdPush.formParams());
    MgrAccdPush.table = table.init();
});
/**
 *
 */
var MgrAccdCheckReason = {
    id: "managerAccdCheckReasonTable",//表格id
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
 * 提交审核不通过原因
 * @param
 */
MgrAccdCheckReason.submitReason = function () {
    var accdId = window.parent.MgrAccd.seItem.id;
    var reason = $("#reason").val();
    var reasonText = $("#reasonText").val();
    if (reason == null || reason == '') {
        reason = reasonText;
    }
    if (reasonText.length > 45) {
        alert("字数超限。")
        return;
    }
    var ajax = new $ax(Feng.ctxPath + "/accid/checkFail", function (data) {
        if (data.code == 500) {
            Feng.error(data.message + "!");
            MgrAccd.table.refresh();
            return;
        }
        Feng.success("操作成功!");
        // 审核操作后自动停止语音播报
        try { window.parent.document.getElementById('alarm').pause(); } catch(e) {}
        MgrAccdCheckReason.close();
        window.parent.MgrAccd.table.refresh();
    }, function (data) {
        Feng.error("操作失败!");
    });
    ajax.set("accdId", accdId);
    ajax.set("reason", reason);
    ajax.set("reasonText", reasonText);
    ajax.start();
};

/**
 * 关闭此对话框
 */
MgrAccdCheckReason.close = function () {
    parent.layer.close(window.parent.MgrAccd.layerIndex);
};


/**
 * 查询表单提交参数对象
 * @returns {{}}
 */
MgrAccdPushClaims.formParams = function () {
    var queryData = {};
    queryData['name'] = $("#name").val();
    return queryData;
}

$(function () {
});
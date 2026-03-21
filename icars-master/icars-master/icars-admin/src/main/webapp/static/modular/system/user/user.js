/**
 * 系统管理--用户管理的单例对象
 */
var MgrUser = {
    id: "managerTable",//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1,
    deptid:0
};

/**
 * 初始化表格的列
 */
MgrUser.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '账号', field: 'account', align: 'center', valign: 'middle', sortable: true},
        {title: '姓名', field: 'name', align: 'center', valign: 'middle', sortable: true},
        {title: '性别', field: 'sexName', align: 'center', valign: 'middle', sortable: true},
        {title: '角色', field: 'roleName', align: 'center', valign: 'middle', sortable: true},
        {title: '部门', field: 'deptName', align: 'center', valign: 'middle', sortable: true},
        {title: '事故推送是否收费', field: 'sfkf', visible: true, align: 'center', formatter: aFormatter, valign: 'middle', sortable: true},
        // {title: '邮箱', field: 'email', visible: false, align: 'center', valign: 'middle', sortable: true},
        {title: '预存款', field: 'balance', align: 'center', valign: 'middle', sortable: true},
        {title: '电话', field: 'phone', align: 'center', valign: 'middle', sortable: true},
        {title: '创建时间', field: 'createtime', visible: false, align: 'center', valign: 'middle', sortable: true},
        {title: '经度', field: 'lng', visible: false, align: 'center', valign: 'middle', sortable: true},
        {title: '纬度', field: 'lat', visible: false, align: 'center', valign: 'middle', sortable: true},
        {title: '状态', field: 'statusName', align: 'center', valign: 'middle', sortable: true}];
    return columns;
};
function aFormatter(value, row, index) {
    if(value == 'N'){
        return [
            '否'
        ].join("")
    }else {
        return [
            '是'
        ].join("")
    }

}
/**
 * 检查是否选中
 */
MgrUser.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        MgrUser.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加管理员
 */
MgrUser.openAddMgr = function () {
    var index = layer.open({
        type: 2,
        title: '添加管理员',
        area: ['800px', '560px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/mgr/user_add'
    });
    this.layerIndex = index;
};

/**
 * 点击添加用户经纬度坐标
 */
MgrUser.openAddLngLat = function () {
    var index = layer.open({
        type: 2,
        title: '搜索位置信息',
        area: ['800px', '560px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/mgr/user_add_location'
    });
    this.layerIndex = index;
};

/**
 * 点击修改按钮时
 * @param userId 管理员id
 */
MgrUser.openChangeUser = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '编辑管理员',
            area: ['800px', '450px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/mgr/user_edit/' + this.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 点击角色分配
 * @param
 */
MgrUser.roleAssign = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '角色分配',
            area: ['300px', '400px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/mgr/role_assign/' + this.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 查看环形好友列表
 * @param
 */
MgrUser.selectHuanxinFriendList = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '环信好友列表',
            area: ['300px', '600px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/mgr/toHuanxinFriendPage/' + this.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 点击查看推广二维码
 * @param
 */
MgrUser.getQrCode = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '二维码',
            area: ['300px', '290px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/mgr/toQrPage/' + this.seItem.account
        });
        this.layerIndex = index;
    }
};

/**
 * 删除用户
 */
MgrUser.delMgrUser = function () {
    if (this.check()) {

        var operation = function(){
            var userId = MgrUser.seItem.id;
            var ajax = new $ax(Feng.ctxPath + "/mgr/delete", function () {
                Feng.success("删除成功!");
                MgrUser.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("userId", userId);
            ajax.start();
        };

        Feng.confirm("是否删除用户" + MgrUser.seItem.account + "?",operation);
    }
};

/**
 * 冻结用户账户
 * @param userId
 */
MgrUser.freezeAccount = function () {
    if (this.check()) {
        var userId = this.seItem.id;
        var ajax = new $ax(Feng.ctxPath + "/mgr/freeze", function (data) {
            Feng.success("冻结成功!");
            MgrUser.table.refresh();
        }, function (data) {
            Feng.error("冻结失败!" + data.responseJSON.message + "!");
        });
        ajax.set("userId", userId);
        ajax.start();
    }
};

/**
 * 解除冻结用户账户
 * @param userId
 */
MgrUser.unfreeze = function () {
    if (this.check()) {
        var userId = this.seItem.id;
        var ajax = new $ax(Feng.ctxPath + "/mgr/unfreeze", function (data) {
            Feng.success("解除冻结成功!");
            MgrUser.table.refresh();
        }, function (data) {
            Feng.error("解除冻结失败!");
        });
        ajax.set("userId", userId);
        ajax.start();
    }
}

/**
 * 开放平台用户注册审核通过
 * @param userId
 */
MgrUser.checkOk = function () {
    if (this.check()) {
        var userId = this.seItem.id;
        var ajax = new $ax(Feng.ctxPath + "/mgr/checkOk", function (data) {
            Feng.success("注册审核成功!");
            MgrUser.table.refresh();
        }, function (data) {
            Feng.error("注册审核失败!");
        });
        ajax.set("userId", userId);
        ajax.start();
    }
}

/**
 * 重置密码
 */
MgrUser.resetPwd = function () {
    if (this.check()) {
        var userId = this.seItem.id;
        parent.layer.confirm('是否重置密码为111111？', {
            btn: ['确定', '取消'],
            shade: false //不显示遮罩
        }, function () {
            var ajax = new $ax(Feng.ctxPath + "/mgr/reset", function (data) {
                Feng.success("重置密码成功!");
            }, function (data) {
                Feng.error("重置密码失败!");
            });
            ajax.set("userId", userId);
            ajax.start();
        });
    }
};

MgrUser.resetSearch = function () {
    $("#name").val("");
    $("#beginTime").val("");
    $("#endTime").val("");

    MgrUser.search();
}

MgrUser.search = function () {
    var queryData = {};

    queryData['deptid'] = MgrUser.deptid;
    queryData['name'] = $("#name").val();
    queryData['beginTime'] = $("#beginTime").val();
    queryData['endTime'] = $("#endTime").val();

    MgrUser.table.refresh({query: queryData});
}

MgrUser.onClickDept = function (e, treeId, treeNode) {
    MgrUser.deptid = treeNode.id;
    MgrUser.search();
};
MgrUser.initHuanxinFriend = function (e, treeId, treeNode) {
    MgrUser.search();
     if (this.check()) {
        var userId = this.seItem.id;
        var ajax = new $ax(Feng.ctxPath + "/mgr/initHuanxinFriend", function (data) {
            Feng.success("初始化好友成功!");
        }, function (data) {
            Feng.error("初始化好友失败!");
        });
        ajax.set("userId", userId);
        ajax.start();
    }
};

$(function () {
    var defaultColunms = MgrUser.initColumn();
    var table = new BSTable("managerTable", "/mgr/list", defaultColunms);
    table.setPaginationType("server");
    MgrUser.table = table.init();
    var ztree = new $ZTree("deptTree", "/dept/tree");
    ztree.bindOnClick(MgrUser.onClickDept);
    ztree.init();
});

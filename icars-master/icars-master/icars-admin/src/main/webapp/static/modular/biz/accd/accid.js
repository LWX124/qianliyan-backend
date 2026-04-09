/**
 * 系统管理--事故上报管理的单例对象
 */
var MgrAccd = {
    id: "managerAccdTable",//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1,
};

/**
 * 初始化表格的列
 */
MgrAccd.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '序号',field: '',align: 'center',formatter: function (value, row, index) {
                var pageSize = MgrAccd.table.btInstance.bootstrapTable('getOptions').pageSize;     //通过table的#id 得到每页多少条
                var pageNumber = MgrAccd.table.btInstance.bootstrapTable('getOptions').pageNumber; //通过table的#id 得到当前第几页
                 return pageSize * (pageNumber - 1) + index + 1;    // 返回每条的序号： 每页条数 *（当前页 - 1 ）+ 序号
            }
        },
        {title: '上报人ID', field: 'openid', visible: false, align: 'center', valign: 'middle', sortable: true},
        {title: '上报人电话', field: 'phone', width: '120', visible: true, align: 'center', valign: 'middle', sortable: true},
        {title: '上报人昵称', field: 'wxname', width: '120', visible: true, align: 'center', valign: 'middle', sortable: true},
        {title: '来源', field: 'source', width: '80', visible: true, align: 'center', valign: 'middle', sortable: true},
        {title: '视频', field: 'video', align: 'center', valign: 'middle',formatter: aFormatter, sortable: false},
        {title: '定位地图', field: 'mapUrl', align: 'center', valign: 'middle',formatter: urlFormatter, sortable: false},
        {title: '上报总数', field: 'totalAcc', align: 'center', valign: 'middle',  sortable: true},
        {title: '有效数', field: 'exist', align: 'center', valign: 'middle',  sortable: true},
        {title: '无效数', field: 'notexist', align: 'center', valign: 'middle',  sortable: true},
        {title: '地址', field: 'address', align: 'center', valign: 'middle', width: '200', sortable: true},
        {title: '是否有效', field: 'realness', align: 'center', valign: 'middle', width: '80', cellStyle:{css:{"color": "red"}},sortable: true},
        {title: '经度', field: 'lng', visible: false, align: 'center', valign: 'middle', sortable: true},
        {title: '纬度', field: 'lat', visible: false, align: 'center', valign: 'middle', sortable: true},
        {title: '推送人', field: 'name', align: 'center', valign: 'middle', sortable: true},
        {title: '审核人', field: 'checkId', visible: false, align: 'center', valign: 'middle', sortable: true},
        {title: '上报时间', field: 'createTime', width: '180', align: 'center', valign: 'middle', sortable: true},
        {title: '审核时间', field: 'checkTime',visible: false,  align: 'center', valign: 'middle', sortable: true},
        {title: 'status', field: 'status', visible: false, align: 'center', valign: 'middle'},
        {title: 'blackList', field: 'blackList', visible: false, align: 'center', valign: 'middle'},
        {title: '状态', field: 'statusName', align: 'center', valign: 'middle', sortable: true},
        {title: '审核原因', field: 'reason', visible: true, align: 'center', valign: 'middle', width: '150'},
        {title: '红包金额', field: 'amount', visible: true, align: 'center', valign: 'middle', width: '80'},
        {title: '收取金额', field: 'backAmount', visible: true, align: 'center', valign: 'middle', width: '80'},];
    return columns;
};

/**
 * 检查是否选中
 */
MgrAccd.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        MgrAccd.seItem = selected[0];
        return true;
    }
};


/**
 * 审核通过
 * @param
 */
// MgrAccd.checkSuccess = function () {
//     if (this.check()) {
//         var accdId = this.seItem.id;
//         var status = this.seItem.status;
//         if(status != 1){
//             Feng.error("只能审核未审核事故!");
//             return;
//         }
//         var ajax = new $ax(Feng.ctxPath + "/accid/checkSuccess", function (data) {
//             if(data.code == 500){
//                 Feng.error(data.message + "!");
//                 MgrAccd.table.refresh();
//                 return;
//             }
//             Feng.success("操作成功!");
//             MgrAccd.table.refresh();
//         }, function (data) {
//             Feng.error("操作失败!" + data.responseJSON.message + "!");
//         });
//         ajax.set("accdId", accdId);
//         ajax.start();
//     }
// };


/**
 * 修改用户黑名单状态
 * @param
 */
MgrAccd.addBlackList = function () {
    if (this.check()) {
        var openid = this.seItem.openid;
        var blackList = this.seItem.blackList;
        var ajax = new $ax(Feng.ctxPath + "/accid/addBlackList", function (data) {
            if(data.code == 500){
                Feng.error(data.message + "!");
                MgrAccd.table.refresh();
                return;
            }
            Feng.success("操作成功!");
            MgrAccd.table.refresh();
        }, function (data) {
            Feng.error("操作失败!" + data.responseJSON.message + "!");
        });
        ajax.set("openid", openid);
        ajax.set("blackList", blackList == 0 ? 1 : 0);
        ajax.start();
    }
};

/**
 * 审核不通过
 * @param
 */
// MgrAccd.checkFail = function () {
//     if (this.check()) {
//         var accdId = this.seItem.id;
//         var status = this.seItem.status;
//         if(status != 1){
//             Feng.error("只能审核未审核事故!");
//             return;
//         }
//         var ajax = new $ax(Feng.ctxPath + "/accid/checkFail", function (data) {
//             if(data.code == 500){
//                 Feng.error(data.message + "!");
//                 MgrAccd.table.refresh();
//                 return;
//             }
//             Feng.success("操作成功!");
//             MgrAccd.table.refresh();
//         }, function (data) {
//             Feng.error("操作失败!");
//         });
//         ajax.set("accdId", accdId);
//         ajax.start();
//     }
// }

/**
 * 点击弹出推送框
 */
MgrAccd.pushFourS = function () {
    if (this.check()){
        var index = layer.open({
            type: 2,
            title: '推送4S门店',
            area: ['800px', '560px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/accid/accid_push'
        });
        this.layerIndex = index;
    }
};
/**
 * 点击弹出推送框 推送理赔老师
 */
MgrAccd.pushClaims = function () {
    if (this.check()){
        var index = layer.open({
            type: 2,
            title: '推送理赔顾问',
            area: ['800px', '560px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/accid/accid_push_claims'
        });
        this.layerIndex = index;
    }
};

/**
 * 点击弹出层，输入审核不过原因
 */
MgrAccd.checkFailReason = function () {
    if (this.check()){
        if(this.seItem.status != 1){
            Feng.error("只能审核未审核事故!");
            return;
        }
        var index = layer.open({
            type: 2,
            title: '审核不通过原因录入',
            area: ['800px', '200px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/accid/accid_check_reason'
        });
        this.layerIndex = index;
    }
};

/**
 * 审核通过，点击弹出层，选择红包金额
 */
MgrAccd.checkSuccessChoose = function () {
    if (this.check()){
        if(this.seItem.status != 1){
            Feng.error("只能审核未审核事故!");
            return;
        }
        var index = layer.open({
            type: 2,
            title: '选择奖励金额',
            area: ['800px', '560px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/accid/redpay_choose'
        });
        this.layerIndex = index;
    }
};



MgrAccd.resetSearch = function () {
    $("#openid").val("");
    $("#createStartTime").val("");
    // $("#createEndTime").val("");
    $("#checkStartTime").val("");
    // $("#checkEndTime").val("");
    MgrAccd.search();
}

MgrAccd.search = function () {
    var queryData = {};

    queryData['openid'] = $("#openid").val();
    queryData['name'] = $("#name").val();
    var createTimeRange = $("#createStartTime").val();
    if(createTimeRange){
        queryData['createStartTime'] = createTimeRange.split('~')[0];
        queryData['createEndTime'] = createTimeRange.split('~')[1];
        if((new Date(queryData['createEndTime']) - new Date(queryData['createStartTime']))/1000/60/60/24 > 7){
            Feng.error("创建时间查询范围不能大于7天!");
            return ;
        }
    }else {
        queryData['createStartTime'] = null;
        queryData['createEndTime'] = null;
    }
    var checkTimeRange = $("#checkStartTime").val();
    if(checkTimeRange){
        queryData['checkStartTime'] = checkTimeRange.split('~')[0];
        queryData['checkEndTime'] = checkTimeRange.split('~')[1];
    }else {
        queryData['checkStartTime'] = null;
        queryData['checkEndTime'] = null;
    }

    queryData['checkStatus'] = $("#checkStatus").val();
    queryData['pushStatus'] = $("#pushStatus").val();
    MgrAccd.table.refresh({query: queryData});

    MgrAccd.queryRedPackSum(queryData);
}

/**
 * 停止通知声音播放
 */
MgrAccd.stop = function () {
    window.parent.document.getElementById('alarm').pause();
};

/**
 * 查询表单提交参数对象
 * @returns {{}}
 */
MgrAccd.formParams = function() {
    var queryData = {};

    queryData['openid'] = $("#openid").val();
    var createTimeRange = $("#createStartTime").val();
    if(createTimeRange){
        queryData['createStartTime'] = createTimeRange.split('~')[0];
        queryData['createEndTime'] = createTimeRange.split('~')[1];
    }

    var checkTimeRange = $("#checkStartTime").val();
    if(checkTimeRange){
        queryData['checkStartTime'] = checkTimeRange.split('~')[0];
        queryData['checkEndTime'] = checkTimeRange.split('~')[1];
    }
    return queryData;
}

function showVideo(val){
    $('#video').attr("src",val);
    layer.open({
        type: 1,
        title: false,
        shadeClose: true,
        area: ['400px', '350px'],
        content: $('#box')
    });
}

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
    if(value.indexOf('mp4') != -1){
        return [
            '<a href="javascript:void(0);" onclick="showVideo(\''+value+'\')" >查看视频</a>'
        ].join("")
    }else {
        return [
            '<a href="javascript:void(0);" onclick="showImage(\''+value+'\')" >查看图片</a>'
        ].join("")
    }

}
function urlFormatter(value, row, index) {
    return [
        '<a href="'+value+'" target="view_window">点击查看地图</a>'
    ].join("")
}
MgrAccd.queryRedPackSum = function (queryData){
    var ajax = new $ax(Feng.ctxPath + "/accid/redpack/sum", function (data) {
        $('#redpackSumDiv').val(data);
    }, function (data) {
    });
    ajax.set(queryData);
    ajax.start();
}


$(function () {
    var defaultColunms = MgrAccd.initColumn();
    var table = new BSTable("managerAccdTable", "/accid/list", defaultColunms);
    table.setPaginationType("server");
    table.height = undefined; // 不限制表格高度，允许内容完整显示
    table.setRowStyle(function (row, index) {
        var style = {};
        if(row.blackList != 0){
            style={css:{'background-color':'red'}};
        }
        return style;
    });
    table.setQueryParams(MgrAccd.formParams());
    MgrAccd.table = table.init();
    MgrAccd.queryRedPackSum(MgrAccd.formParams());

    // 每次 bootstrap-table 渲染表体后，用 JS 直接写 inline style 覆盖
    function applyScrollFix() {
        var tableEl = document.getElementById('managerAccdTable');
        if (!tableEl) return;

        // 让 ibox-content 可横向滚动
        var iboxContent = tableEl.closest('.ibox-content');
        if (iboxContent) {
            iboxContent.style.overflowX = 'auto';
            iboxContent.style.overflowY = 'visible';
        }

        // fixed-table-body 横向可滚动
        var fixedBody = tableEl.closest('.fixed-table-body');
        if (fixedBody) {
            fixedBody.style.overflowX = 'auto';
            fixedBody.style.overflowY = 'visible';
        }

        // fixed-table-container 不截断
        var fixedContainer = tableEl.closest('.fixed-table-container');
        if (fixedContainer) {
            fixedContainer.style.overflowX = 'auto';
            fixedContainer.style.overflowY = 'visible';
        }

        // 表格本身：最小宽度撑开，自动布局让各列按内容分配
        tableEl.style.minWidth = '1800px';
        tableEl.style.width = 'auto';
        tableEl.style.tableLayout = 'auto';
    }

    // 初始渲染后执行
    setTimeout(applyScrollFix, 500);

    // 每次数据加载完毕后再执行（翻页/搜索/刷新都会触发）
    $('#managerAccdTable').on('post-body.bs.table', function () {
        applyScrollFix();
    });


    $("button[name='refresh']").click(function(){
        MgrAccd.search()
    })

});


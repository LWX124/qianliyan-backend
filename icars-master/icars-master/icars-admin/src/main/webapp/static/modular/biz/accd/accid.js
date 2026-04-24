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
        {field: 'selectItem', radio: true, width: 36},
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '序号',field: '', width: 60, align: 'center',formatter: function (value, row, index) {
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
        {title: '分享次数', field: 'shareCount', align: 'center', valign: 'middle', sortable: true},
        {title: '被打开次数', field: 'shareOpenCount', align: 'center', valign: 'middle', sortable: true},
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
        '<a href="'+value+'" target="view_window">查看</a>',
        ' | ',
        '<a href="javascript:void(0);" data-lat="'+row.lat+'" data-lng="'+row.lng+'" data-addr="'+(row.address || '').replace(/"/g,'&quot;')+'" onclick="copyShareLink(this)">复制分享链接</a>'
    ].join("")
}

function copyShareLink(el) {
    var lat = el.getAttribute('data-lat');
    var lng = el.getAttribute('data-lng');
    var addr = el.getAttribute('data-addr');
    var url = 'https://apis.map.qq.com/uri/v1/marker?marker=coord:' + lat + ',' + lng + ';title:事故位置;addr:' + encodeURIComponent(addr) + '&referer=myapp';
    if (navigator.clipboard && navigator.clipboard.writeText) {
        navigator.clipboard.writeText(url).then(function() {
            Feng.success("地图链接已复制，可粘贴到微信分享");
        });
    } else {
        var textarea = document.createElement('textarea');
        textarea.value = url;
        document.body.appendChild(textarea);
        textarea.select();
        document.execCommand('copy');
        document.body.removeChild(textarea);
        Feng.success("地图链接已复制，可粘贴到微信分享");
    }
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

    // ===== 新记录自动刷新 & 高亮 =====
    var initialMaxId = 0;
    var polledLatestId = 0;
    var isFirstLoad = true;

    // 每次表格渲染后：记录初始 maxId + 高亮新记录
    $('#managerAccdTable').on('post-body.bs.table', function () {
        var data = $('#managerAccdTable').bootstrapTable('getData');
        if (isFirstLoad && data.length > 0) {
            for (var i = 0; i < data.length; i++) {
                if (data[i].id > initialMaxId) initialMaxId = data[i].id;
            }
            polledLatestId = initialMaxId;
            isFirstLoad = false;
        }
        // 高亮新记录：ID > initialMaxId 且 status == 1（未审核）
        var $rows = $('#managerAccdTable tbody tr');
        for (var i = 0; i < data.length; i++) {
            if (data[i].id > initialMaxId && data[i].status == 1) {
                $($rows[i]).addClass('new-record');
            } else {
                $($rows[i]).removeClass('new-record');
            }
        }
        applyScrollFix();
    });

    // 轮询检测新记录（每 15 秒），用现有 list 接口取第一条
    setInterval(function () {
        $.post(Feng.ctxPath + "/accid/list", {limit: 1, offset: 0}, function (data) {
            if (data && data.rows && data.rows.length > 0) {
                var latestId = data.rows[0].id;
                if (latestId > polledLatestId && polledLatestId > 0) {
                    polledLatestId = latestId;
                    MgrAccd.table.refresh();
                }
            }
        });
    }, 15000);

    // 每次 bootstrap-table 渲染表体后，用 JS 直接写 inline style 覆盖
    function applyScrollFix() {
        var tableEl = document.getElementById('managerAccdTable');
        if (!tableEl) return;

        var iboxContent = tableEl.closest('.ibox-content');
        if (iboxContent) {
            iboxContent.style.overflowX = 'auto';
            iboxContent.style.overflowY = 'visible';
        }

        var fixedBody = tableEl.closest('.fixed-table-body');
        if (fixedBody) {
            fixedBody.style.overflowX = 'auto';
            fixedBody.style.overflowY = 'visible';
        }

        var fixedContainer = tableEl.closest('.fixed-table-container');
        if (fixedContainer) {
            fixedContainer.style.overflowX = 'auto';
            fixedContainer.style.overflowY = 'visible';
        }

        tableEl.style.minWidth = '1800px';
        tableEl.style.width = 'auto';
        tableEl.style.tableLayout = 'auto';
    }

    // 初始渲染后执行
    setTimeout(applyScrollFix, 500);

    $("button[name='refresh']").click(function(){
        MgrAccd.search()
    })

});


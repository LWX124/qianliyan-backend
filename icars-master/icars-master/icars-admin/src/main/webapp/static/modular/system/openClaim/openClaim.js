/**
 * 管理初始化
 */
var OpenClaim = {
    id: "OpenClaimTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 重置
 */
OpenClaim.reSearch = function () {
    $("#condition").val('');
    $("#deptid").val('');
    $("#orderno").val('');
    $("#orderStatus").val('');
    $("#createStartTime").val('');
    OpenClaim.initHeader();
    OpenClaim.search();
}

/**
 * 初始化表格的列
 */
OpenClaim.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: '编号', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {
            title: '订单号',
            field: 'orderno',
            width: '200',
            visible: true,
            align: 'center',
            formatter: orderFormatter,
            valign: 'middle'
        },
        {title: '推送4S门店', field: 'fullname', width: '200', visible: true, align: 'center', valign: 'middle'},
        {title: '4S电话', field: 'synopsis', width: '200', visible: true, align: 'center', valign: 'middle'},
        {title: '订单上报人', field: 'openid', visible: false, align: 'center', valign: 'middle'},
        {title: '客户手机号', field: 'phone', width: '100', visible: true, align: 'center', valign: 'middle'},
        {title: '客户姓名', field: 'name', width: '100', visible: true, align: 'center', valign: 'middle'},
        {title: '车牌号', field: 'cph', width: '100', width: '100', visible: true, align: 'center', valign: 'middle'},
        {title: '车架号', field: 'cjh', width: '100', visible: true, align: 'center', valign: 'middle'},
        {title: '汽车品牌', field: 'qcpp', width: '100', visible: true, align: 'center', valign: 'middle'},
        {title: '订单状态', field: 'statusName', width: '100', visible: true, align: 'center', valign: 'middle'},
        {title: '上报人手机号', field: 'claimerPhone', width: '100', visible: true, align: 'center', valign: 'middle'},
        {title: '人伤情况', field: 'personHurts', width: '100', visible: true, align: 'center', valign: 'middle'},
        {title: '物损情况', field: 'goodsHurts', width: '100', visible: true, align: 'center', valign: 'middle'},

        {title: '施救公司', field: 'rescue', width: '100', visible: true, align: 'center', valign: 'middle'},
        {title: '施救费用', field: 'rescueFee', width: '100', visible: true, align: 'center', valign: 'middle'},
        {title: '保险公司', field: 'insurer', width: '100', visible: true, align: 'center', valign: 'middle'},
        {title: '保险购买渠道', field: 'insurAccess', width: '100', visible: true, align: 'center', valign: 'middle'},
        {title: '承诺客户', field: 'promise', width: '100', visible: true, align: 'center', valign: 'middle'},
        {title: '事故类型', field: 'accType', width: '100', visible: true, align: 'center', valign: 'middle'},
        {title: '预估费用', field: 'preFee', width: '100', visible: true, align: 'center', valign: 'middle'},
        {title: '结算方式', field: 'settleType', width: '100', visible: true, align: 'center', valign: 'middle'},
        {
            title: '救援费用收款方式',
            field: 'rescueFeeSettleType',
            width: '100',
            visible: true,
            align: 'center',
            valign: 'middle'
        },
        {
            title: '定位地图',
            field: 'mapUrl',
            width: '100',
            align: 'center',
            valign: 'middle',
            formatter: urlFormatter,
            sortable: false
        },
        {title: '经度', field: 'lng', visible: false, align: 'center', valign: 'middle'},
        {title: '纬度', field: 'lat', visible: false, align: 'center', valign: 'middle'},
        {title: '定损金额', field: 'fixloss', width: '100', visible: true, align: 'center', valign: 'middle'},
        {title: '公司提成金额', field: 'rebateForCompany', width: '100', visible: true, align: 'center', valign: 'middle'},
        {title: '业务员提成金额', field: 'rebateForEmp', width: '100', visible: true, align: 'center', valign: 'middle'},
        {title: '定位地址名称', field: 'address', width: '300', visible: true, align: 'center', valign: 'middle'},
        {
            title: '理赔现场图片',
            field: 'claimImgList',
            width: '100',
            visible: true,
            align: 'center',
            formatter: aFormatter,
            valign: 'middle'
        },
        {
            title: '理赔资料图片',
            field: 'detailImgList',
            width: '100',
            visible: true,
            align: 'center',
            formatter: aFormatter,
            valign: 'middle'
        },
        {title: '付款凭证号', field: 'payBillNo', width: '200', visible: true, align: 'center', valign: 'middle'},
        {title: '内部付款凭证号', field: 'payBillNoForClaim', width: '200', visible: true, align: 'center', valign: 'middle'},
        {title: '描述', field: 'desc', width: '100', visible: true, align: 'center', valign: 'middle'},
        {title: '预交车时间', field: 'yjcsj', width: '100', visible: true, align: 'center', valign: 'middle'},
        {title: '开单时间', field: 'createtime', width: '200', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
OpenClaim.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        OpenClaim.seItem = selected[0];
        return true;
    }
};

function urlFormatter(value, row, index) {
    return [
        '<a href="' + value + '" target="view_window">点击查看地图</a>'
    ].join("")
}


/**
 * 隐藏部门选择的树
 */
OpenClaim.hideDeptSelectTree = function () {
    $("#menuContent").fadeOut("fast");
    $("body").unbind("mousedown", onBodyDown);// mousedown当鼠标按下就可以触发，不用弹起
};
/**
 * 点击部门input框时
 *
 * @param e
 * @param treeId
 * @param treeNode
 * @returns
 */
OpenClaim.onClickDept = function (e, treeId, treeNode) {
    $("#citySel").attr("value", instance.getSelectedVal());
    $("#deptid").attr("value", treeNode.id);
};

function onBodyDown(event) {
    if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(
        event.target).parents("#menuContent").length > 0)) {
        OpenClaim.hideDeptSelectTree();
    }
}

/**
 * 显示部门选择的树
 *
 * @returns
 */
OpenClaim.showDeptSelectTree = function () {
    var cityObj = $("#citySel");
    var cityOffset = $("#citySel").offset();
    $("#menuContent").css({
        left: cityOffset.left + "px",
        top: cityOffset.top + cityObj.outerHeight() + "px"
    }).slideDown("fast");

    $("body").bind("mousedown", onBodyDown);
};
/**
 * 确认结算，添加结算对账单凭证
 */
OpenClaim.openPayedOpenClaim = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '录入结算转账凭证',
            area: ['400px', '400px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/openClaim/openClaim_addPayBillNoForClaim/' + this.seItem.id
        });
        this.layerIndex = index;
    }
};

function showImage(val) {
    $('#pointol').empty();
    $('#imgDiv').empty();

    var imgs = val.split(',');
    imgs.forEach(function (val, index) {
        if (index == 0) {
            $('<li data-target="#myCarousel" data-slide-to="' + index + '" class="active"></li>').appendTo($('#pointol'));
            $('<div class="item active"><img src="' + val + '"></div>').appendTo($('#imgDiv'));
        } else {
            $('<li data-target="#myCarousel" data-slide-to="' + index + '"></li>').appendTo($('#pointol'));
            $('<div class="item"><img src="' + val + '"></div>').appendTo($('#imgDiv'));
        }
    });
    layer.open({
        type: 1,
        title: false,
        shadeClose: true,
        area: ['640px', '750px'],
        offset: 'auto',
        content: $('#myCarousel')
    });
}


function showDetail(orderNo) {
    layer.open({
        type: 2,
        title: false,
        shadeClose: true,
        area: ['1600px', '700px'],
        offset: 'auto',
        content: Feng.ctxPath + '/openClaim/openClaim_detail/' + orderNo
    });
}

function aFormatter(value, row, index) {
    if (value == null || value == undefined || value.length < 1) {
        return '';
    }
    return [
        '<a href="javascript:void(0);" onclick="showImage(\'' + value + '\')" >查看图片</a>'
    ].join("")
}

function orderFormatter(value, row, index) {
    return [
        '<a href="javascript:void(0);" onclick="showDetail(\'' + value + '\')" >' + value + '</a>'
    ].join("")
}

OpenClaim.initParam = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    queryData['orderno'] = $("#orderno").val();
    queryData['status'] = $("#orderStatus").val();
    queryData['deptid'] = $("#deptid").val();
    var createTimeRange = $("#createStartTime").val();
    if (createTimeRange) {
        queryData['createStartTime'] = createTimeRange.split('~')[0];
        queryData['createEndTime'] = createTimeRange.split('~')[1];
    }
    return queryData;
};

OpenClaim.stopOpenClaim = function () {
    window.parent.document.getElementById("open_alarm_claim").pause();
}

OpenClaim.queryFixLossSum = function () {
    var ajax = new $ax(Feng.ctxPath + "/openClaim/fixLossSum", function (data) {
        $('#fixLossSumDiv').val(data.toFixed(2));
    }, function (data) {
    });
    ajax.set(OpenClaim.initParam());
    ajax.start();
}

/**
 * 导出excel
 */
OpenClaim.export = function () {
    var queryStr = "";
    queryStr = queryStr + ($("#condition").val() ? 'condition=' + $("#condition").val() : "");
    queryStr = queryStr + ($("#orderno").val() ? 'orderno=' + $("#orderno").val() : "");
    queryStr = queryStr + ($("#orderStatus").val() ? 'status=' + $("#orderStatus").val() : "");
    queryStr = queryStr + ($("#deptid").val() ? 'deptid=' + $("#deptid").val() : "");
    var createTimeRange = $("#createStartTime").val();
    if (createTimeRange) {
        queryStr = queryStr + 'createStartTime=' + createTimeRange.split('~')[0];
        queryStr = queryStr + 'createEndTime=' + createTimeRange.split('~')[1];
    }
    window.location.href = Feng.ctxPath + "/openClaim/list/export?" + queryStr;
}


/**
 * 点击添加
 */
OpenClaim.openAddOpenClaim = function () {
    var index = layer.open({
        type: 2,
        title: '添加',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/openClaim/openClaim_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看详情
 */
OpenClaim.openOpenClaimDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/openClaim/openClaim_update/' + OpenClaim.seItem.id
        });
        this.layerIndex = index;
    }
};

OpenClaim.updateState=function() {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '详情',
            area: ['500px', '300px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/openClaim/openClaim_update/' + OpenClaim.seItem.id
        });
        this.layerIndex = index;
    }

}
/**
 * 删除
 */
OpenClaim.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/openClaim/delete", function (data) {
            Feng.success("删除成功!");
            OpenClaim.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("openClaimId", this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询列表
 */
OpenClaim.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    queryData['deptid'] = $("#deptid").val();
    queryData['orderno'] = $("#orderno").val();
    queryData['status'] = $("#orderStatus").val();
    var createTimeRange = $("#createStartTime").val();
    if (createTimeRange) {
        queryData['createStartTime'] = createTimeRange.split('~')[0];
        queryData['createEndTime'] = createTimeRange.split('~')[1];
    }else{
         queryData['createStartTime'] = '';
         queryData['createEndTime'] = '';
    }
    OpenClaim.table.refresh({query: queryData});
    OpenClaim.queryFixLossSum();
};

$(function () {
    var defaultColunms = OpenClaim.initColumn();
    var table = new BSTable(OpenClaim.id, "/openClaim/list", defaultColunms);
    table.setPaginationType("server");
    OpenClaim.table = table.init();
    $('body').attr('class', 'white-bg');
    $('.ibox-title').css('border-color', '#ffffff');
    $('.form-control[readonly]').css('background-color', '#ffffff');
    //初始化部门选择组件 START
    var ztree = new $ZTree("treeDemo", "/dept/tree");
    ztree.bindOnClick(OpenClaim.onClickDept);
    ztree.init();
    instance = ztree;
    //初始化部门选择组件 END
    OpenClaim.queryFixLossSum();

    //初始化统计数据
    var ajax = new $ax(Feng.ctxPath + "/openClaim/countData", function (data) {
        if (data != null) {

            OpenClaim.initHeader(data);
        }

    }, function (data) {
    });
    ajax.start();

});

OpenClaim.initHeader=function(data){
    if(data==undefined||data==null){
        data={
            openOrder:0,
            receipt:0,
            money:0,
            crosstown:0,
            settlement:0
        }
    }
    var dataStr = [{
        text: '开单',
        des: '今日开单:' + data.openOrder + '单',
    },
        {
            text: '接单',
            des: '今日已接车:' + data.receipt + '辆',
        },
        {
            text: '金额',
            des: '今日金额:' + data.money + '元',

        },
        {
            text: '交车',
            des: '今日交车:' + data.crosstown + '辆',
        },
        {
            text: '结算',
            des: '今日结算:' + data.settlement + '辆',
        }
    ]
    new T({
        data: dataStr,
        box: '.box',
        index: 6,
        callBack: function (e, index) {
            var date = new Date().Format("yyyy-MM-dd");
            var date2 = date + ' ~ ' + date;
            $("#createStartTime").val(date2);
            if (index == 0) {
                //开单状态
                $("#orderStatus").val(-1);
            } else if (index == 1) {
                //接单
                $("#orderStatus").val(1);
            } else if (index == 2) {
                //今日金额
                // $("#orderStatus").val(1);
            } else if (index == 3) {
                //今日交车
                $("#orderStatus").val(2);
            } else if (index == 4) {
                //今日结算
                $("#orderStatus").val(3);
            }
            OpenClaim.search();
        }
    });
    $("#title_count").show();
}

Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}


function T(o) {
    this.init(o)
}

T.prototype = {
    init(o) {
        const n = {
            data: o.data || [],
            box: o.box ? document.querySelector(o.box) : document.body,
            index: o.index || 0,
            callBack: o.callBack || new Function()
        }
        Object.assign(this, n)
        this.changeActive()
        this.render()
    },
    changeActive() {
        this.data = this.data.map((item, index) => {
            item.active = false
            item.next = true
            if (index == this.index) item.active = true
            if (index == this.data.length - 1) item.next = false
            return item
        })
    },
    render() {
        let index = 0,
            text = this.data.reduce((result, item) => {
                let str = `<li data-index='${index}'>
              <div class="content">
                  <div class="item ${item.active ? 'active' : 'default'}">${item.text}</div>
                  ${item.next ? '<div class="arrow" right></div>' : ''}
              </div>
              <p>${item.des}</p>
            </li>`
                result += str
                index++
                return result
            }, '')
        this.box.innerHTML = text
        this.bindEvents()
    },
    bindEvents() {
        const $lis = this.box.querySelectorAll('li')
        Array.from($lis).forEach(li => {
            li.addEventListener('click', (e) => {
                this.index = li.getAttribute('data-index')
                this.changeActive()
                this.callBack(e, this.index)
                this.render()
            })
        })
    }
}

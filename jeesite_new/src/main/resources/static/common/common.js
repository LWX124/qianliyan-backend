/*!
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * 项目自定义的公共JavaScript，可覆盖jeesite.js里的方法
 */

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

function countData() {
    $.ajax({
        type: "POST",
        url: "/jeesite/a/app/appIndent/countData?val=1",
        dataType: "json",
        success: function (data) {
            initHeader(data);
        }
    });
}


function initHeader(data){
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
        text: '新订单',
        des: '今日开单:' + data.openOrder + '单',
    },
        {
            text: '服务中',
            des: '今日已接车:' + data.receipt + '辆',
        },
        {
            text: '金额',
            des: '今日金额:' + data.money + '元',

        },
        {
            text: '已交车',
            des: '今日交车:' + data.crosstown + '辆',
        },
        {
            text: '已结算',
            des: '今日结算:' + data.settlement + '辆',
        }
    ]
    new T({
        data: dataStr,
        box: '.countBox',
        index: 6,
        callBack: function (e, index) {
            var date = new Date().Format("yyyy-MM-dd");
            var date2 = date + ' ~ ' + date;
            $("#createTime_gte").val(date+" 00:00:00");
            $("#createTime_lte").val(date+" 23:59:59");

            var ddd = $("#state").select2();    //获取select 的ID

            if (index == 0) {
                //开单状态
                ddd.val(1).trigger("change");    //这个values就是代表你选中的值
            } else if (index == 1) {
                //接单
                ddd.val(3).trigger("change");    //这个values就是代表你选中的值
            } else if (index == 2) {
                //今日金额
                ddd.val(3).trigger("change");    //这个values就是代表你选中的值
            } else if (index == 3) {
                //今日交车
                ddd.val(4).trigger("change");    //这个values就是代表你选中的值
            } else if (index == 4) {
                //今日结算
                ddd.val(5).trigger("change");    //这个values就是代表你选中的值
            }
            ddd.change();
            $('#dataGrid').dataGrid('reloadGrid');
            // countData()
        }
    });
    $("#title_count").show();
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

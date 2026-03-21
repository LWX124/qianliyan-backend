/**
 * 初始化详情对话框
 */
var OpenClaimInfoDlg = {
    imageUrls : null,
    openClaimInfoData : {}
};

$("#status").val($("#statusValue").val());


/**
 * 清除数据
 */
OpenClaimInfoDlg.clearData = function() {
    this.openClaimInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
OpenClaimInfoDlg.set = function(key, val) {
    this.openClaimInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
OpenClaimInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
OpenClaimInfoDlg.close = function() {
    parent.layer.close(window.parent.OpenClaim.layerIndex);
}


/**
 * 关闭此对话框
 */
OpenClaimInfoDlg.close1 = function() {
    parent.layer.close(window.parent.OpenClaim.layerIndex);
}

/**
 * 收集数据
 */
OpenClaimInfoDlg.collectData = function() {
    this
    .set('id')
    .set('fixloss')
    .set('payBillNo')
    .set('payBillNoForClaim')
    .set('exMgs');

    if(OpenClaimInfoDlg.imageUrls){
        this.openClaimInfoData['exImgUrls'] = OpenClaimInfoDlg.imageUrls;
    }
}

/**
 * 提交添加
 */
OpenClaimInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/openClaim/add", function(data){
        Feng.success("添加成功!");
        window.parent.PartnerOpenClaim.table.refresh();
        OpenClaimInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.openClaimInfoData);
    ajax.start();
}

/**
 * 提交添加
 */
OpenClaimInfoDlg.addFixlossSubmit = function() {

    if(!isNum($('#fixloss').val())){
        Feng.error("参数格式错误，请检查重新填写");
        return;
    }
    this.clearData();
    this.collectData();

    var ajax = new $ax(Feng.ctxPath + "/openClaim/partner/addFixloss", function(data){
        Feng.success("操作成功!");
        window.parent.PartnerOpenClaim.table.refresh();
        OpenClaimInfoDlg.close();
    },function(data){
        Feng.error("操作失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.openClaimInfoData);
    ajax.start();
}

/**
 * 提交添加转账凭证号
 */
OpenClaimInfoDlg.addPayBillNoSubmit = function() {

    this.clearData();
    this.collectData();

    var ajax = new $ax(Feng.ctxPath + "/openClaim/partner/addPayBillNo", function(data){
        Feng.success("操作成功!");
        window.parent.PartnerOpenClaim.table.refresh();
        OpenClaimInfoDlg.close();
    },function(data){
        Feng.error("操作失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.openClaimInfoData);
    ajax.start();
}

/**
 * 提交异常信息
 */
OpenClaimInfoDlg.addExMsgSubmit = function() {

    this.clearData();
    this.collectData();

    var ajax = new $ax(Feng.ctxPath + "/openClaim/partner/addExMsg", function(data){
        Feng.success("操作成功!");
        window.parent.PartnerOpenClaim.table.refresh();
        OpenClaimInfoDlg.close();
    },function(data){
        Feng.error("操作失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.openClaimInfoData);
    ajax.start();
}

/**
 * 提交添加转账凭证号 针对理赔老师
 */
OpenClaimInfoDlg.addPayBillNoForClaimSubmit = function() {

    this.clearData();
    this.collectData();

    var ajax = new $ax(Feng.ctxPath + "/openClaim/partner/addPayBillNoForClaim", function(data){
        Feng.success("操作成功!");
        window.parent.OpenClaim.table.refresh();
        OpenClaimInfoDlg.close1();
    },function(data){
        Feng.error("操作失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.openClaimInfoData);
    ajax.set("settleAccounts",$("#settleAccounts").val());
    ajax.start();
}


function isNum(val){
    // isNaN()函数 把空串 空格 以及NUll 按照0来处理 所以先去除
    if(val === "" || val ==null){
        return false;
    }
    if(!isNaN(val)){
        return true;
    }else{
        return false;
    }
}

/**
 * 提交修改
 */
OpenClaimInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/openClaim/update", function(data){
        Feng.success("修改成功!");
        window.parent.OpenClaim.table.refresh();
        OpenClaimInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    this.openClaimInfoData.id=$("#id").val();
    this.openClaimInfoData.status=$("#status").val();
    ajax.set(this.openClaimInfoData);
    ajax.start();
}

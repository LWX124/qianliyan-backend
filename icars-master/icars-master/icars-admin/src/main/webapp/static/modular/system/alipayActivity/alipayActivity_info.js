/**
 * 初始化支付宝活动对话框
 */
var AlipayActivityInfoDlg = {
    alipayActivityInfoData: {},
    validateFields: {
        totalAmount: {
            validators: {
                numeric: {
                    message: '只能输入数字'
                }
            }
        },
        startTime: {
            validators: {
                notEmpty: {
                    message: '开始时间不能为空'
                }
            }
        },
        endTime: {
            validators: {
                notEmpty: {
                    message: '结束时间不能为空'
                }
            }
        },
        totalCount: {
            validators: {
                numeric: {
                    message: '只能输入数字'
                }
            }
        }
    }
};



/**
 * 关闭此对话框
 */
AlipayActivityInfoDlg.close = function () {
    parent.layer.close(window.parent.AlipayActivity.layerIndex);
};

/**
 * 验证数据是否为空
 */
AlipayActivityInfoDlg.validate = function () {
    $('#alipayActivityInfoForm').data("bootstrapValidator").resetForm();
    $('#alipayActivityInfoForm').bootstrapValidator('validate');
    return $("#alipayActivityInfoForm").data('bootstrapValidator').isValid();
};
/**
 * 提交
 */
AlipayActivityInfoDlg.addSubmit = function () {
    this.clearData();
    this.collectData();
    if (!this.validate()) {
        return;
    }
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/alipayActivity/add", function (data) {
        Feng.success("创建成功!");
        window.parent.AlipayActivity.table.refresh();
        AlipayActivityInfoDlg.close();
    }, function (data) {
        Feng.error("创建失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.alipayActivityInfoData);
    ajax.start();
};

/**
 * 清除数据
 */
AlipayActivityInfoDlg.clearData = function () {
    this.alipayActivityInfoData = {};
};

/**
 * 收集数据
 */
AlipayActivityInfoDlg.collectData = function () {
    this.set('totalAmount').set('totalCount').set('startTime').set('endTime');
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
AlipayActivityInfoDlg.set = function (key, value) {
    if(typeof value == "undefined"){
        if(typeof $("#" + key).val() =="undefined"){
            var str="";
            var ids="";
            $("input[name='"+key+"']:checkbox").each(function(){
                if(true == $(this).is(':checked')){
                    str+=$(this).val()+",";
                }
            });
            if(str){
                if(str.substr(str.length-1)== ','){
                    ids = str.substr(0,str.length-1);
                }
            }else{
                $("input[name='"+key+"']:radio").each(function(){
                    if(true == $(this).is(':checked')){
                        ids=$(this).val()
                    }
                });
            }
            this.alipayActivityInfoData[key] = ids;
        }else{
            this.alipayActivityInfoData[key]= $("#" + key).val();
        }
    }

    return this;
};
/**
 * 提交修改
 */
AlipayActivityInfoDlg.editSubmit = function () {
    this.collectData();
    var ajax = new $ax(Feng.ctxPath + "/alipayActivity/update", function (data) {
        Feng.success("修改成功!");
        window.parent.AlipayActivity.table.refresh();
        AlipayActivityInfoDlg.close();
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set('dictId',$("#dictId").val());
    ajax.set('dictName',this.dictName);
    ajax.set('dictCode',this.dictCode);
    ajax.set('dictTips',this.dictTips);
    ajax.set('dictValues',this.mutiString);
    ajax.start();
};
$(function () {
    Feng.initValidator("alipayActivityInfoForm", AlipayActivityInfoDlg.validateFields);
});

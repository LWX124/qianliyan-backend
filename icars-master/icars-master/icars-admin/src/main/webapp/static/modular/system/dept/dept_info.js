/**
 * 初始化部门详情对话框
 */
var DeptInfoDlg = {
    deptInfoData: {},
    imageUrls: null,
    zTreeInstance: null,
    validateFields: {
        simplename: {
            validators: {
                notEmpty: {
                    message: '部门名称不能为空'
                }
            }
        },
        fullname: {
            validators: {
                notEmpty: {
                    message: '部门全称不能为空'
                }
            }
        },
        pName: {
            validators: {
                notEmpty: {
                    message: '上级名称不能为空'
                }
            }
        },
        lng: {
            validators: {
                notEmpty: {
                    message: '经度不能为空'
                }
            }
        },
        lat: {
            validators: {
                notEmpty: {
                    message: '纬度不能为空'
                }
            }
        }
    }
};

/**
 * 清除数据
 */
DeptInfoDlg.clearData = function () {
    this.deptInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
DeptInfoDlg.set = function (key, val) {
    this.deptInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
DeptInfoDlg.get = function (key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
DeptInfoDlg.close = function () {
    parent.layer.close(window.parent.Dept.layerIndex);
}

/**
 * 点击部门ztree列表的选项时
 *
 * @param e
 * @param treeId
 * @param treeNode
 * @returns
 */
DeptInfoDlg.onClickDept = function (e, treeId, treeNode) {
    $("#pName").attr("value", DeptInfoDlg.zTreeInstance.getSelectedVal());
    $("#pid").attr("value", treeNode.id);
}


/**
 * 显示部门选择的树
 *
 * @returns
 */
DeptInfoDlg.showDeptSelectTree = function () {
    var pName = $("#pName");
    var pNameOffset = $("#pName").offset();
    $("#parentDeptMenu").css({
        left: pNameOffset.left + "px",
        top: pNameOffset.top + pName.outerHeight() + "px"
    }).slideDown("fast");

    $("body").bind("mousedown", onBodyDown);
}

/**
 * 隐藏部门选择的树
 */
DeptInfoDlg.hideDeptSelectTree = function () {
    $("#parentDeptMenu").fadeOut("fast");
    $("body").unbind("mousedown", onBodyDown);// mousedown当鼠标按下就可以触发，不用弹起
}


/**
 * 收集数据
 */
DeptInfoDlg.collectData = function () {
    this.set('id').set('simplename').set('fullname').set('tips').set('pid').set('lng').set('lat').set('synopsis').set('scaleForCompany').set('scaleForEmp');

    if (DeptInfoDlg.imageUrls) {
        this.deptInfoData['imgUrls'] = DeptInfoDlg.imageUrls;
    }
    if ($('#city').text() && $('#province').text() && $('#district').text()) {
        this.deptInfoData['address'] = $('#province').text() + '-' + $('#city').text() + '-' + $('#district').text() + $('#address').val();
    }
    var companys = $('#winsuranceCompany').find("option:selected");
    if (companys.size() > 0) {
        var companyids = null;
        $.each(companys, function (index, obj) {
            companyids = companyids == null ? obj.value : companyids + ',' + obj.value;
        });
        this.deptInfoData['companyids'] = companyids;
    }
}


/**
 * 验证数据是否为空
 */
DeptInfoDlg.validate = function () {
    $('#deptInfoForm').data("bootstrapValidator").resetForm();
    $('#deptInfoForm').bootstrapValidator('validate');
    return $("#deptInfoForm").data('bootstrapValidator').isValid();
}

/**
 * 验证是否选择省市区
 */
DeptInfoDlg.validateAddress = function () {
    var address = this.deptInfoData.address;
    if (address) {
        return true;
    } else {
        return false;
    }
};

/**
 * 验证返点比例是否正确
 */
DeptInfoDlg.validateScale = function () {
    var scaleForCompany = this.deptInfoData.scaleForCompany;
    var scaleForEmp = this.deptInfoData.scaleForEmp;
    if (isNum(scaleForCompany) && isNum(scaleForEmp)) {
        if (scaleForCompany > 0 && scaleForCompany <= 100 && scaleForEmp > 0 && scaleForEmp <= 100) {
            return true;
        }
        return false;
    } else {
        return false;
    }
};

function isNum(val) {
    // isNaN()函数 把空串 空格 以及NUll 按照0来处理 所以先去除
    if (val === "" || val == null) {
        return false;
    }
    if (!isNaN(val)) {
        return true;
    } else {
        return false;
    }
}

/**
 * 提交添加部门
 */
DeptInfoDlg.addSubmit = function () {

    this.clearData();
    this.collectData();


    if ($("#pName").val() != "顶级") {
        if (!this.validate()) {
            return;
        }
        if (!this.validateAddress()) {
            Feng.error("请选择省市区");
            return;
        }

        if (!this.validateScale()) {
            Feng.error("请检查返点是否填写正确");
            return;
        }
    }


    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/dept/add", function (data) {
        Feng.success("添加成功!");
        window.parent.Dept.table.refresh();
        DeptInfoDlg.close();
    }, function (data) {
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.deptInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
DeptInfoDlg.editSubmit = function () {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    // if (!this.validateAddress()) {
    //     Feng.error("请选择省市区");
    //     return;
    // }

    if (!this.validateScale()) {
        Feng.error("请检查返点是否填写正确");
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/dept/update", function (data) {
        Feng.success("修改成功!");
        window.parent.Dept.table.refresh();
        DeptInfoDlg.close();
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.deptInfoData);
    ajax.start();
}

/**
 * 初始化合作4S店选择框
 */
DeptInfoDlg.initCompanySelection = function () {

    var ajax = new $ax(Feng.ctxPath + "/bizInsuranceCompany/listForSelection", function (data) {
        for (var i = 0; i < data.length; i++) {
            $('#winsuranceCompany').append("<option value=" + data[i].id + ">" + data[i].name + "</option>");
        }
        // 缺一不可
        $('#winsuranceCompany').selectpicker('refresh');
        $('#winsuranceCompany').selectpicker('render');
        if ($('#companyids').val()) {
            $('#winsuranceCompany').selectpicker('val', $('#companyids').val().split(','));
        }
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.start();
}

function onBodyDown(event) {
    if (!(event.target.id == "menuBtn" || event.target.id == "parentDeptMenu" || $(
        event.target).parents("#parentDeptMenu").length > 0)) {
        DeptInfoDlg.hideDeptSelectTree();
    }
}

$(function () {
    Feng.initValidator("deptInfoForm", DeptInfoDlg.validateFields);
    DeptInfoDlg.initCompanySelection();


    var ztree = new $ZTree("parentDeptMenuTree", "/dept/tree");
    ztree.bindOnClick(DeptInfoDlg.onClickDept);
    ztree.init();

    DeptInfoDlg.zTreeInstance = ztree;
});

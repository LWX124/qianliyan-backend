/**
 * 系统管理--用户管理的单例对象
 */
var AddUserLocation = {
    id: "mapTable",//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1,
    lng:'',
    lat:'',
    deptid:0
};

/**
/**
 * 点击添加用户经纬度坐标
 */
AddUserLocation.fillLocation = function () {
    window.parent.UserInfoDlg.set('lng',AddUserLocation.lng);
    window.parent.UserInfoDlg.set('lat',AddUserLocation.lat);
    parent.layer.close(window.parent.UserInfoDlg.layerIndex);
};

AddUserLocation.close = function () {
    parent.layer.close(window.parent.UserInfoDlg.layerIndex);
};


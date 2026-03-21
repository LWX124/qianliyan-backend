package com.jeesite.modules.app.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class VehicleLicenseResult implements Serializable {
    private String userId;		// 拍卖信息id
    private String number;		// 号牌号码
    private String vehicleType;		// 车辆类型
    private String name;		// 所有人
    private String address;		// 地址
    private Long engineNo;		// 发动机号码
    private String vin;		// 车辆识别代号
    private String model;		// 品牌型号
    private Date registerDate;		// 注册日期
    private Date issueDate;		// 发证日期
    private String useCharacter;		// 使用性质
}

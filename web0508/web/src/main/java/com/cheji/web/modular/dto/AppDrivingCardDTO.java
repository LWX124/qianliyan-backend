package com.cheji.web.modular.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
@Data
public class AppDrivingCardDTO {
    private Long id;
    private String userId;		// 拍卖信息id
    private String number;		// 号牌号码
    private String vehicleType;		// 车辆类型
    private String name;		// 所有人
    private String address;		// 地址
    private String engineNo;		// 发动机号码
    private String vin;		// 车辆识别代号
    private String model;		// 品牌型号
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date registerDate;		// 注册日期
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date issueDate;		// 发证日期
    private String useCharacter;		// 使用性质
    private String imgUrl;		// 图片地址
}

package com.cheji.web.modular.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.cheji.web.modular.domain.AppAuctionImgEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class UsedCarVo implements Serializable {

    private String id;

    private String drivingLicense;      //行驶证图片

    @NotEmpty(message = "车牌号不能为空！")
    private String plateNo;      //车牌号

    @NotEmpty(message = "品牌型号不能为空！")
    private String brand;       //品牌型号

    @NotEmpty(message = "车主名字不能为空！")
    private String name;       //车主名字

    @NotEmpty(message = "电话不能为空！")
    private String phone;       //车辆所有人电话

    private String licenseAddress;      //行驶证上居住地址

    private String carIdentificationNumber;    //车辆识别码

    private String engine;      //发动机号码

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date registerDate;        //注册日期

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date issueDate;       //发证日期

    @NotEmpty(message = "车辆性质不能为空！")
    private String useNature;       //使用性质，0，非运营，1.运营，2，营转非

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date productDate;      //出厂日期

    private String color;       //车身颜色

    private String parkingPlace;        //停放地

    @NotEmpty(message = "事故类型不能为空！")
    private String accidentType;           //事故类型

    private String fuel;        //燃油类型

    private String displacement;        //排量

    private String mileage;     //行驶里程

    private String change;      //是否改装,0否,1是

    private String key;     //钥匙

    private String plateNumber;     //车牌数量，几张

    private String overhaul;        //是否拆解,0否,1是

    private String frameNoDamagedCondition;     //车架号是否受损,0否,1是

    private String transfer;        //过户次数

    @JsonFormat(pattern = "yyyy-MM", timezone = "GMT+8")
    private Date annualCheck;     //年检期限

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date compulsoryInsuranceVilidity;     //交强险有效期

    private String owner;       //所有人,个人,公司

    private String carSourceType;     //车源类型

    private String damageReason;        //车损原因

    private String auctionType;      //拍卖类型 一口价/拍卖

    private BigDecimal price;      //起拍价一口价

    @NotEmpty(message = "carState不能为空！")
    private Integer carState;       //提交状态 0,保存1,待审核,2,未通过,3,通过,7,拍卖中,8,拍卖完成,9,流拍,10,过户审核,11,过户审核未通过, 12过户完成

    private String carIntroduction;    //车辆介绍

    private List<String> beforeRepair; //修复之前照片

    private List<String> afterRepair;  //修复之后照片

    private String announcements;  //车辆介绍

    private String explain;                     //审核失败

    private Long beginTime;     //开始时间

    private Long endTime;       //结束时间

    private String fixedPrice;      //是否是一口价

    private BigDecimal carBondAmt;     //车辆保证金（元）

    private BigDecimal carServiceAmt;     //车辆服务费（元）

    private BigDecimal luxuryCarPrice;  //精品车价格

    private BigDecimal firstAmount;         //首付金额
}

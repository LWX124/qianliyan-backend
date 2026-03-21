package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 拍卖基本
 * </p>
 *
 * @author yang
 */
@TableName("app_auction")
@Data
public class AppAuctionEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String brand;       //品牌型号
    private String name;       //所有人名字
    private String displacement;        //排量
    private String transmission;        //传动类型
    private String fuel;        //燃油类型
    @TableField("register_date")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date registerDate;        //注册日期
    @TableField("issue_date")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date issueDate;       //发证日期
    @TableField("annual_check")
    @JsonFormat(pattern = "yyyy-MM", timezone = "GMT+8")
    private Date annualCheck;     //年检期限
    @TableField("compulsory_insurance_vilidity")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date compulsoryInsuranceVilidity;     //交强险有效期
    @TableField("registered_residence")
    private String registeredResidence;     //户籍地
    @TableField("parking_place")
    private String parkingPlace;        //停放地
    @TableField("accident_type")
    private String accidentType;        //车辆状态,1.保险车,2.残值车,3.水淹车,4.火烧车,5.一口价
    private String sunroof;     //是否有天窗
    @TableField("frame_no")
    private String frameNo;     //车架号
    @TableField("use_nature")
    private String useNature;       //使用性质
    @TableField("frame_no_damaged_condition")
    private String frameNoDamagedCondition;     //车架号是否受损,0否,1是
    private String engine;      //发动机
    private String mileage;     //行驶里程
    private String key;     //钥匙
    @TableField("purchase_tax")
    private String purchaseTax;     //购置税
    @TableField("plate_number")
    private String plateNumber;     //车牌数
    @TableField("registration_certificate")
    private String registrationCertificate;     //登记证书
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;      //
    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updateTime;      //
    private String overhaul;        //是否拆检,0否,1是
    private String announcements;       //注意事项
    @TableField("car_state")
    private Integer carState;       //0,保存1,待审核,2,未通过,3,通过,7,拍卖中,8,拍卖完成,9,流拍,10,过户审核,11,过户审核未通过, 12过户完成
    @TableField("other_field")
    private String otherField;      //扩展字段
    private String color;       //车身颜色
    private String mortgage;        //抵押按揭
    private String second;      //二次事故
    private String change;      //是否改装,0否,1是
    private String transfer;        //过户次数
    private String owner;       //所有人,个人,公司
    private String duty;        //事故责任
    @TableField("duty_book")
    private String dutyBook;        //责任书
    @TableField("driving_license")
    private String drivingLicense;      //行驶证
    private String licence;     //驾照
    private String insurance;       //保险公司
    @TableField("ins_remark")
    private String insRemark;   //保险介绍
    private String repair;      //维修预估
    @TableField("user_id")
    private String userId;      //登录人id
    @TableField("plate_no")
    private String plateNo;      //车牌号

    @TableField("auction_type")
    private String auctionType;      //拍卖类型

    @TableField("damage_reason")
    private String damageReason;      //车损原因

    private BigDecimal price;      //起拍价
    @TableField("fixed_price")
    private String fixedPrice;      //是否是一口价
    @TableField("insured_amount")
    private BigDecimal insuredAmount;      //保险金额
    @TableField("up_state")
    private Integer upState;      //上架状态,0.未上架,1,已上架

    @TableField("product_date")
    @JsonFormat(pattern = "yyyy-MM", timezone = "GMT+8")
    private Date productDate;      //出厂日期

    private String phone;       //车辆所有人电话

    @TableField("license_address")
    private String licenseAddress;

    @TableField("car_identification_number")
    private String  carIdentificationNumber;    //车辆识别码

    @TableField("source_type")
    private Integer sourceType;      //信息来源,1,拍卖车，2,二手车

    @TableField("car_introduction")
    private String carIntroduction;     //车辆介绍

    @TableField("car_bond_amt")
    private BigDecimal carBondAmt;     //车辆保证金（元）

    @TableField("car_service_amt")
    private BigDecimal carServiceAmt;     //车辆服务费（元）


    @TableField("luxury_car_price")
    private BigDecimal luxuryCarPrice;  //二手车——精品车价格

    @TableField("first_amount")
    private BigDecimal firstAmount;     // 首付价格

    @TableField(exist = false)
    private String explain;     //审核失败说明

    @TableField(exist = false)
    private Long beginTime;     //开始时间

    @TableField(exist = false)
    private Long endTime;       //结束时间

    @TableField(exist = false)
    private String collect;       //收藏

    @TableField(exist = false)
    private String selfState;       //自定义状态

    @TableField(exist = false)
    private BigDecimal topPrice;       //自定义状态

    @TableField(exist = false)
    private Integer carCount;       //剩余参拍次数

    @TableField(exist = false)
    private String isWarnCar;       //是否是提醒车

    @TableField(exist = false)
    private String isPayBail;       //是否支付保证金

    @TableField(exist = false)
    private BigDecimal addPrice;       //加的价格

    @TableField(exist = false)
    private Integer serviceFee;       //服务费率

    @TableField(exist = false)
    private BigDecimal otherFee;       //其他费用

    @TableField(exist = false)
    private BigDecimal bail;       //保证金

    @TableField(exist = false)
    private Integer bidNum;       //出价人数

    @TableField(exist = false)
    private Integer warnNum;       //提醒人数

    @TableField(exist = false)
    private Long browseNum;       //浏览次数

    @TableField(exist = false)
    private AppAuctionCounselorEntity counselor;       //服务顾问

    //图片的的封装
    @TableField(exist = false)
    private List<AppAuctionImgEntity> imgList;

    //图片的的封装
    @TableField(exist = false)
    private List<AppAuctionImgEntity> beforeRepair;

    //图片的的封装
    @TableField(exist = false)
    private List<AppAuctionImgEntity> afterRepair;

}







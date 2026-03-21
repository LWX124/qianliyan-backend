package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 拍卖上架
 * </p>
 *
 * @author yang
 */
@TableName("app_auction_find_car")
@Data
public class AppAuctionFindCarEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updateTime;

    @TableField("user_id")
    private Long userId;

    private String brand;       //品牌
    private String series;      //车系
    private String color;       //颜色
    @TableField("car_age")
    private String carAge;     //车龄
    private String type;        //事故类型
    private String address;     //需求地址
    private String remark;     //备注
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String validity;        //有效期

}

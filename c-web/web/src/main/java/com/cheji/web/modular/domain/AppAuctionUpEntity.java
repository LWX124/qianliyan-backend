package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 拍卖上架
 * </p>
 *
 * @author yang
 */
@TableName("app_auction_up")
@Data
public class AppAuctionUpEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    //关联拍卖车id
    @TableField("car_id")
    private Long carId;

    @TableField("counselor_id")
    private String counselorId;

    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    @TableField("begin_time")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date beginTime;

    @TableField("end_time")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endTime;

    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updateTime;
    
    //服务费
    @TableField("service_fee")
    private Integer serviceFee;
    //其他费用
    @TableField("other_fee")
    private BigDecimal otherFee;
    // 车牌号
    private String brand;
    // 失败说明
    private String explain;
    //图片的的封装
    @TableField(exist = false)
    private List<String> imgs;

}

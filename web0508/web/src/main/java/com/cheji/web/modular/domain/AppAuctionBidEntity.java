package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
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
@TableName("app_auction_bid_log")
@Data
public class AppAuctionBidEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("valid")
    private int valid;

    @TableField("car_id")
    @NotNull(message = "车辆ID必填")
    private Long carId;

    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updateTime;

    @NotNull(message = "出价(单位元)")
    private BigDecimal bid;

    @TableField(exist = false)
    private Integer endTimes;

    @TableField(exist = false)
    private Long bidTime;

    @TableField(exist = false)
    private String userName;
}

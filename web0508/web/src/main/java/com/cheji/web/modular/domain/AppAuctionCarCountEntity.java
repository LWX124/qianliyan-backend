package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 拍卖上架
 * </p>
 *
 * @author yang
 */
@TableName("app_auction_car_count")
@Data
public class AppAuctionCarCountEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("user_id")
    private Long userId;

    @TableField("car_id")
    private String CarId;

    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updateTime;
    @TableField("bid_state")
    private String bidState;


}

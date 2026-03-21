package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 拍卖上架
 * </p>
 *
 * @author yang
 */
@TableName("app_auction_collect")
@Data
public class AppAuctionCollectEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    //登录用户ID
    @TableField("user_id")
    private Long userId;

    @TableField("is_enabled")
    private Integer isEnabled;
    //收藏的车辆ID
    @TableField("car_id")
    private Long carId;
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;
}

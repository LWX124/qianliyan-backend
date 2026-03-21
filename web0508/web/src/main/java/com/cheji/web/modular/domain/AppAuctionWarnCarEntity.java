package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 车辆提醒订阅
 * </p>
 *
 * @author yang
 */
@TableName("app_auction_warn_car")
@Data
public class AppAuctionWarnCarEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("user_id")
    private Long userId;


    @TableField("car_id")
    private Long carId;

    @TableField("is_enabled")
    private Integer isEnabled;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;
}

package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 拍卖上架
 * </p>
 *
 * @author yang
 */
@TableName("app_auction_hot")
@Data
public class AppAuctionHotEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("car_id")
    private Long carId;

    @TableField("is_enabled")
    private Integer isEnabled;//0有效,1失效

    private Long hits;
    private String city;


}

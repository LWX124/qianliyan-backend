package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 拍卖上架
 * </p>
 *
 * @author yang
 */
@TableName("app_auction_brand_subscription")
@Data
public class AppAuctionBrandSubEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    @NotNull
    private Long userId;

    @TableField("img_url")
    private String imgUrl;

    @TableField("vip_lv")
    private Integer vipLv;

    @NotNull
    private String brand;

}

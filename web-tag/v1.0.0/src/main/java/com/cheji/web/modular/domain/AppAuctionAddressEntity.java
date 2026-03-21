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

/**
 * <p>
 * 拍卖上架
 * </p>
 *
 * @author yang
 */
@TableName("app_auction_address")
@Data
public class AppAuctionAddressEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private String address;

    private String phone;

    private String area;

    @TableField("is_default")
    private String isDefault;//是否是默认地址,1是,2不是

    @TableField("user_id")
    private Long userId;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;


}

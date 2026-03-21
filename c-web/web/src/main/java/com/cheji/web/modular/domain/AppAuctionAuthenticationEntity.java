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
 * 拍卖上架
 * </p>
 *
 * @author yang
 */
@TableName("app_auction_authentication")
@Data
public class AppAuctionAuthenticationEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("user_id")
    private Long userId;

    @TableField("sign_state")
    private String signState;

    @TableField("auth_state")
    private String authState;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

    @TableField(exist = false)
    private String authImg;
}

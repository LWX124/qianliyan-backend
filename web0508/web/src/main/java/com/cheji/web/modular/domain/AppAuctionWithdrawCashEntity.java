package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
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
@TableName("app_auction_withdrawcash")
@Data
public class AppAuctionWithdrawCashEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("user_id")
    private Long userId;

    private Integer state;       //提现状态,0,提现中,1,成功,2,失败

    private Integer allow;       //是否允许提现,1是 ,2否

    private String des;       //描述

    @TableField("vip_lv")
    private Integer vipLv;

    @TableField("user_bank_id")
    private String userBankId;

    private BigDecimal amount;      //提现金额,单位分

    @TableField("create_time")
    private Date createTime;

    @TableField("outcash_time")
    private Date outcashTime;

}

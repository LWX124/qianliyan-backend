package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 用户vip详情
 * </p>
 *
 * @author yang
 */
@TableName("app_auction_vip_control")
@Data
public class AppAuctionVipControlEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;        //用户id

    @TableField("vip_lv")
    private String vipLv;       //vip等级

    @TableField("vip_id")
    private String vipId;       //vip编号

    private BigDecimal amount;      //签约保证金

    private Integer state;      //状态,1使用中,2提现中,3已失效

    @TableField("car_count")
    private Integer carCount;    //参拍台次

    @TableField("freeze_count")
    private Integer freezeCount;        //冻结台次

    private Integer offer;        //每月报价台次

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;


}

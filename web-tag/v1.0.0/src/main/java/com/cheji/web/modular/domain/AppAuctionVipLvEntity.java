package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.cheji.web.constant.AppAuctionVipSet;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 会员等级
 * </p>
 *
 * @author yang
 */
@TableName("app_auction_vip_lv")
@Data
public class AppAuctionVipLvEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @NotNull(message = "等级必填")
    private String lv;

    @TableField("lv_name")
    private String lvName;

    @NotNull(message = "金额必填")
    private BigDecimal amount;

    @TableField("time_out")
    private Integer timeOut;  //参拍台次

    @TableField("brand_num")
    private Integer brandNum;  //品牌订阅数量

    @TableField("open_explain")
    private String openExplain;

    @TableField("close_explain")
    private String closeExplain;

    @TableField(exist = false)
    private String openTitle;

    @TableField(exist = false)
    private String closeTitle;

    //图片的的封装
    @TableField(exist = false)
    private List<AppAuctionImgAdvEntity> imgList;

}

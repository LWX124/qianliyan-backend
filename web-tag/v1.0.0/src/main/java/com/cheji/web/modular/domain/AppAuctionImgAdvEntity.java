package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 拍卖图片
 * </p>
 *
 * @author yang
 */
@TableName("app_auction_img_adv")
@Data
public class AppAuctionImgAdvEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String url;
    //关联拍卖车id
    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;
    private String state;
    private String type;        //图片类型,1广告图片,2vip相关图片
    private String info;
    @TableField("vip_lv")
    private String vipLv;
}

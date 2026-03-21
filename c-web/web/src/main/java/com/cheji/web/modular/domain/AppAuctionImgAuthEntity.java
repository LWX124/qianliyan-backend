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
@TableName("app_auction_img_auth")
@Data
public class AppAuctionImgAuthEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String url;
    //关联拍卖车id
    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;
    private String state;       //图片状态
    private String type;        //图片类型,1个人认证,2企业认证
    @TableField("user_id")
    private Long userId;
}

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
 * 拍卖图片
 * </p>
 *
 * @author yang
 */
@TableName("app_auction_message_identify")
@Data
public class AppAuctionMessageIdentifyEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("id_name")
    private String idName;
    @TableField("id_number")
    private String idNumber;
    @TableField("business_name")
    private String businessName;
    @TableField("business_number")
    private String businessNumber;
    //关联拍卖车id
    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

    private String type;        //1个人认证,2企业认证
    @TableField("user_id")
    private Long userId;

    @TableField(exist = false)
    private List<AppAuctionImgAuthEntity> authImg;

    private String phone;

    @TableField(exist = false)
    private String code;

}

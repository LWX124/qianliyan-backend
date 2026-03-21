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
@TableName("app_auction_session")
@Data
public class AppAuctionSessionEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("begin_time")
    private Date beginTime;     //拍卖会开始时间

    private Integer duration;        //拍卖时长(分钟)

    @TableField("create_time")
    private Date createTime;        //创建时间

    @TableField("update_time")
    private Date updateTime;

    private String state;       //拍卖会状态

    private String title;       //拍卖会名


}

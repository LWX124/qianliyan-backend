package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 拍卖图片
 * </p>
 *
 * @author yang
 */
@TableName("app_auction_img")
@Data
public class AppAuctionImgEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String url;
    private Integer index;
    //关联拍卖车id
    @TableField("car_id")
    private Long carId;
    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

    private String state;       //图片类型,1车辆图片,2保险资料,3,4,5过户资料

}

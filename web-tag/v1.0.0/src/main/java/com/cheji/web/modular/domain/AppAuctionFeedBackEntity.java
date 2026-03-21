package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 拍卖上架
 * </p>
 *
 * @author yang
 */
@TableName("app_auction_feedback")
@Data
public class AppAuctionFeedBackEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("user_id")
    private Long userId;

    //我的资金
    private String feedback;

}

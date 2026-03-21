package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

/**
 * <p>
 * 拍卖上架
 * </p>
 *
 * @author yang
 */
@TableName("app_auction_home_page")
@Data
public class AppAuctionHomePageEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String info;        //跳转链接
    //我的资金
    private String url;        //URL地址
    private Integer type;       //地址类型,1视频  2图片


}

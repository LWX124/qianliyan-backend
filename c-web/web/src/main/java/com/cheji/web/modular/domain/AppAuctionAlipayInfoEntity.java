package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.Date;

@TableName("app_auction_alipay_info")
@Data
public class AppAuctionAlipayInfoEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 商户私钥
     */
    @TableField("merchant_private_key")
    private String merchantPrivateKey;
    @TableField("app_id")
    private String appId;
    /**
     * 阿里公钥
     */
    @TableField("alipay_public_key")
    private String alipayPublicKey;
    @TableField("crate_time")
    private Date crateTime;
    @TableField("update_time")
    private Date updateTime;
    private String protocol;
    @TableField("gateway_host")
    private String gatewayHost;
    @TableField("sign_type")
    private String signType;
    @TableField("notify_url")
    private String notifyUrl;
    @TableField("return_url")
    private String returnUrl;


}
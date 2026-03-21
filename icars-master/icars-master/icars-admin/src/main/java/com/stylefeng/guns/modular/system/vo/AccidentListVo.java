package com.stylefeng.guns.modular.system.vo;

import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;

@Data
public class AccidentListVo implements Serializable {

    private Long accId;

    /**
     * 图片或者视频
     */
    private String url;

    /**
     * 地址
     */
    private String address;

    /**
     * 是否通过 '状态(1：未审核  2：审核通过  3：审核失败）',
     */
    private Integer isPass;

    /**
     * 失败原因
     */
    private String reason;

}

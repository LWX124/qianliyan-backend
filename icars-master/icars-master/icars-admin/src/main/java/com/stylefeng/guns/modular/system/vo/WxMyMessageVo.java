package com.stylefeng.guns.modular.system.vo;

import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
public class WxMyMessageVo implements Serializable {
    /**
     * 用户编号
     */
    private Integer userIdNumber;

    /**
     * 合计奖励
     */
    private Integer submitNumber;

    /**
     * 报案奖励
     */
    private BigDecimal reportAmount;

    /**
     * 任务奖励
     */
    private Integer taskAmount = 0;

}

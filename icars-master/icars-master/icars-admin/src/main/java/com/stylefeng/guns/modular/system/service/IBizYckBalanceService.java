package com.stylefeng.guns.modular.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.stylefeng.guns.modular.system.model.BizYckBalance;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-10
 */
public interface IBizYckBalanceService extends IService<BizYckBalance> {
    boolean addBalance(@Param("amount")BigDecimal amount, @Param("openid")String openid, @Param("modifyTime")Date modifyTime,
                       @Param("account")String account);

    boolean reduceBalance(@Param("amount")BigDecimal amount, @Param("account")String account, @Param("modifyTime")Date modifyTime);
}

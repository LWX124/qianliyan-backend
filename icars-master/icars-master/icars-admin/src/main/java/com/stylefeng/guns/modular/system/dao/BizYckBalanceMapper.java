package com.stylefeng.guns.modular.system.dao;

import com.stylefeng.guns.modular.system.model.BizYckBalance;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-10
 */
public interface BizYckBalanceMapper extends BaseMapper<BizYckBalance> {
    int addBalance(@Param("amount")BigDecimal amount, @Param("openid")String openid, @Param("modifyTime")Date modifyTime);
    int addBalanceByAccount(@Param("amount")BigDecimal amount, @Param("account")String account, @Param("modifyTime")Date modifyTime);
    int reduceBalance(@Param("amount")BigDecimal amount, @Param("account")String account, @Param("modifyTime")Date modifyTime);
}

package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.domain.SettlePlusEntity;

import java.math.BigDecimal;

/**
 * <p>
 * 结算plus会员明细 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-10-08
 */
public interface SettlePlusMapper extends BaseMapper<SettlePlusEntity> {

    BigDecimal findtodatSettle(Integer id);

    BigDecimal findMonthSettle(Integer id);

    BigDecimal findSettle(Integer id);
}

package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.domain.UserPlusRoyaltyEntity;

import java.math.BigDecimal;

/**
 * <p>
 * 用户开通plus会员提成表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-10-08
 */
public interface UserPlusRoyaltyMapper extends BaseMapper<UserPlusRoyaltyEntity> {

    BigDecimal findtodayRoyalty(Integer id);

    BigDecimal findMonthRoyalty(Integer id);

    BigDecimal findRoyalty(Integer id);

}

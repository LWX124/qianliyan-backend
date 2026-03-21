package com.cheji.web.modular.mapper;

import com.cheji.web.modular.cwork.Personal;
import com.cheji.web.modular.domain.AppSubstituteDrivingIndentEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 代驾订单表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2020-06-08
 */
public interface AppSubstituteDrivingIndentMapper extends BaseMapper<AppSubstituteDrivingIndentEntity> {

    Personal findIndentCount(String userId);

    List<Integer> selectNoPayList(Integer userid);

    AppSubstituteDrivingIndentEntity selectBySubNumber(String orderNumber);

}

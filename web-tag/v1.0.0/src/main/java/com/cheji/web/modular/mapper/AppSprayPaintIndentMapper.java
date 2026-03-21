package com.cheji.web.modular.mapper;

import com.cheji.web.modular.cwork.Personal;
import com.cheji.web.modular.domain.AppSprayPaintIndentEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 喷漆订单表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2020-03-13
 */
public interface AppSprayPaintIndentMapper extends BaseMapper<AppSprayPaintIndentEntity> {

    Personal findIndentCount(String userId);

    List<Integer> findOtherIndent(Integer userid);

    AppSprayPaintIndentEntity selectByNumber(String orderNumber);
}

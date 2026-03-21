package com.cheji.web.modular.mapper;

import com.cheji.web.modular.cwork.Personal;
import com.cheji.web.modular.domain.AppYearCheckIndentEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 年检订单表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2020-04-02
 */
public interface AppYearCheckIndentMapper extends BaseMapper<AppYearCheckIndentEntity> {

    Personal findIndentCount(String userId);

    List<Integer> findNoPayIndent(Integer userid);

    AppYearCheckIndentEntity selectByYearNumber(String orderNumber);
}

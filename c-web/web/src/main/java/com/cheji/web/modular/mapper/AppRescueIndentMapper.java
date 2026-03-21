package com.cheji.web.modular.mapper;

import com.cheji.web.modular.cwork.Personal;
import com.cheji.web.modular.domain.AppRescueIndentEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 * 救援表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2020-02-21
 */
public interface AppRescueIndentMapper extends BaseMapper<AppRescueIndentEntity> {

    Personal findIndentCount(String userId);

    String findImg(Integer type);
}

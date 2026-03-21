package com.cheji.b.modular.mapper;

import com.cheji.b.modular.domain.AppBeautyPriceDetailEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-12-20
 */
public interface AppBeautyPriceDetailMapper extends BaseMapper<AppBeautyPriceDetailEntity> {

    String findbeautyType(int i);
}

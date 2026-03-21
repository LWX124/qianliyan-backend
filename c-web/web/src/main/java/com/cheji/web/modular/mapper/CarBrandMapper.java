package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.domain.CarBrandEntity;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-09-19
 */
public interface CarBrandMapper extends BaseMapper<CarBrandEntity> {

    Integer findId(String carType);
}

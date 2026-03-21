package com.cheji.b.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.domain.CarBrandEntity;
import com.cheji.b.modular.mapper.CarBrandMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-10-09
 */
@Service
public class CarBrandService extends ServiceImpl<CarBrandMapper, CarBrandEntity> implements IService<CarBrandEntity> {

}

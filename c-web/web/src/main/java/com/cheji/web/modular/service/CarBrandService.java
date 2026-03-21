package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.CarBrandEntity;
import com.cheji.web.modular.mapper.CarBrandMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-09-19
 */
@Service
public class CarBrandService extends ServiceImpl<CarBrandMapper, CarBrandEntity> implements IService<CarBrandEntity> {


    @Resource
    private CarBrandMapper carBrandMapper;

    public Integer findId(String carType) {
        return carBrandMapper.findId(carType);
    }
}

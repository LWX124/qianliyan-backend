package com.cheji.b.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.domain.AppBeautyPriceDetailEntity;
import com.cheji.b.modular.mapper.AppBeautyPriceDetailMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-12-20
 */
@Service
public class AppBeautyPriceDetailService extends ServiceImpl<AppBeautyPriceDetailMapper, AppBeautyPriceDetailEntity> implements IService<AppBeautyPriceDetailEntity> {
    @Resource
    private AppBeautyPriceDetailMapper appBeautyPriceDetailMapper;

    public String findbeautyType(int i) {
        return appBeautyPriceDetailMapper.findbeautyType(i);
    }
}

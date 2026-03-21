package com.cheji.b.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.domain.AppUpMerchantsEntity;
import com.cheji.b.modular.mapper.AppUpMerchantsMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 通过图片上架的4s店表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2022-04-13
 */
@Service
public class AppUpMerchantsService extends ServiceImpl<AppUpMerchantsMapper, AppUpMerchantsEntity> implements IService<AppUpMerchantsEntity> {

}

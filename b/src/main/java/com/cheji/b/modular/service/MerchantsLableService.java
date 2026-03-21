package com.cheji.b.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.domain.MerchantsLableEntity;
import com.cheji.b.modular.mapper.MerchantsLableMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商户和标签关系表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-09-28
 */
@Service
public class MerchantsLableService extends ServiceImpl<MerchantsLableMapper, MerchantsLableEntity> implements IService<MerchantsLableEntity> {

}

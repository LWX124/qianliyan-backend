package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.MerchantsLableEntity;
import com.cheji.web.modular.mapper.MerchantsLableMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商户和标签关系表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-08-20
 */
@Service
public class MerchantsLableService extends ServiceImpl<MerchantsLableMapper, MerchantsLableEntity> implements IService<MerchantsLableEntity> {

}

package com.cheji.b.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.domain.MerchantsServicerEntity;
import com.cheji.b.modular.mapper.MerchantsServicerMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商户服务顾问表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-10-09
 */
@Service
public class MerchantsServicerService extends ServiceImpl<MerchantsServicerMapper, MerchantsServicerEntity> implements IService<MerchantsServicerEntity> {

}

package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.MerchantsServicerEntity;
import com.cheji.web.modular.mapper.MerchantsServicerMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商户服务顾问表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-08-23
 */
@Service
public class MerchantsServicerService extends ServiceImpl<MerchantsServicerMapper, MerchantsServicerEntity> implements IService<MerchantsServicerEntity> {

}

package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.MerchantsBrandEntity;
import com.cheji.web.modular.mapper.MerchantsBrandMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 品牌和商户关联表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-10-10
 */
@Service
public class MerchantsBrandService extends ServiceImpl<MerchantsBrandMapper, MerchantsBrandEntity> implements IService<MerchantsBrandEntity> {

}

package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.cheji.web.modular.domain.AppCouponTypeEntity;
import com.cheji.web.modular.mapper.AppCouponTypeMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 优惠劵类型表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2020-07-17
 */
@Service
public class AppCouponTypeService extends ServiceImpl<AppCouponTypeMapper, AppCouponTypeEntity> implements IService<AppCouponTypeEntity> {

}

package com.cheji.b.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.domain.AppPushMessEntity;
import com.cheji.b.modular.mapper.AppPushMessMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 车辆订单推送给商户记录表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2022-04-13
 */
@Service
public class AppPushMessService extends ServiceImpl<AppPushMessMapper, AppPushMessEntity> implements IService<AppPushMessEntity> {

}

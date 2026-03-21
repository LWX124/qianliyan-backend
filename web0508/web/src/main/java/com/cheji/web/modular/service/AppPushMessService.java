package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.AppPushMessEntity;
import com.cheji.web.modular.mapper.AppPushMessMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 车辆订单推送给商户记录表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2021-01-20
 */
@Service
public class AppPushMessService extends ServiceImpl<AppPushMessMapper, AppPushMessEntity> implements IService<AppPushMessEntity> {

}

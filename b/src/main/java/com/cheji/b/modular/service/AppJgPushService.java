package com.cheji.b.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.domain.AppJgPushEntity;
import com.cheji.b.modular.mapper.AppJgPushMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 激光推送表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-11-13
 */
@Service
public class AppJgPushService extends ServiceImpl<AppJgPushMapper, AppJgPushEntity> implements IService<AppJgPushEntity> {

}

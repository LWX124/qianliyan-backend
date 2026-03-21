package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.AppJgPushEntity;
import com.cheji.web.modular.mapper.AppJgPushMapper;
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

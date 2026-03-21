package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.cheji.web.modular.domain.AppBUserConfigEntity;
import com.cheji.web.modular.mapper.AppBUserConfigMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商户关联配置表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-12-25
 */
@Service
public class AppBUserConfigService extends ServiceImpl<AppBUserConfigMapper, AppBUserConfigEntity> implements IService<AppBUserConfigEntity> {

}

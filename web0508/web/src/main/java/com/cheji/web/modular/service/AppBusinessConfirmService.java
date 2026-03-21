package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.cheji.web.modular.domain.AppBusinessConfirmEntity;
import com.cheji.web.modular.mapper.AppBusinessConfirmMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商户业务确认表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2020-07-09
 */
@Service
public class AppBusinessConfirmService extends ServiceImpl<AppBusinessConfirmMapper, AppBusinessConfirmEntity> implements IService<AppBusinessConfirmEntity> {

}

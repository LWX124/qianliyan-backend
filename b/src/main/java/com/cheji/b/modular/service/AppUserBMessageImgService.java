package com.cheji.b.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.domain.AppUserBMessageImgEntity;
import com.cheji.b.modular.mapper.AppUserBMessageImgMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * b端服务信息表
 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2020-04-29
 */
@Service
public class AppUserBMessageImgService extends ServiceImpl<AppUserBMessageImgMapper, AppUserBMessageImgEntity> implements IService<AppUserBMessageImgEntity> {

}

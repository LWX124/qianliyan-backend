package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.cheji.web.modular.domain.AppUserBMessageImgEntity;
import com.cheji.web.modular.mapper.AppUserBMessageImgMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * b端服务信息表
 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2020-05-06
 */
@Service
public class AppUserBMessageImgService extends ServiceImpl<AppUserBMessageImgMapper, AppUserBMessageImgEntity> implements IService<AppUserBMessageImgEntity> {

}

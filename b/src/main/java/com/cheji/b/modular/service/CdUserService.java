package com.cheji.b.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.domain.CdUserEntity;
import com.cheji.b.modular.mapper.CdUserMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 车店用户表
 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2022-01-11
 */
@Service
public class CdUserService extends ServiceImpl<CdUserMapper, CdUserEntity> implements IService<CdUserEntity> {

}

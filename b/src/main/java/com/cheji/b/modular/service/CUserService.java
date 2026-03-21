package com.cheji.b.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.domain.CUserEntity;
import com.cheji.b.modular.mapper.CUserMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-09-26
 */
@Service
public class CUserService extends ServiceImpl<CUserMapper, CUserEntity> implements IService<CUserEntity> {

}

package com.cheji.b.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.domain.UserPlusEntity;
import com.cheji.b.modular.mapper.UserPlusMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-10-08
 */
@Service
public class UserPlusService extends ServiceImpl<UserPlusMapper, UserPlusEntity> implements IService<UserPlusEntity> {

}

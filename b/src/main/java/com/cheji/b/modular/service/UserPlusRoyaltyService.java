package com.cheji.b.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.domain.UserPlusRoyaltyEntity;
import com.cheji.b.modular.mapper.UserPlusRoyaltyMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户开通plus会员提成表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-10-08
 */
@Service
public class UserPlusRoyaltyService extends ServiceImpl<UserPlusRoyaltyMapper, UserPlusRoyaltyEntity> implements IService<UserPlusRoyaltyEntity> {

}

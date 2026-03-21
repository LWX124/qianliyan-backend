package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.UserPlusRoyaltyEntity;
import com.cheji.web.modular.mapper.UserPlusRoyaltyMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

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
    @Resource
    private UserPlusRoyaltyMapper userPlusRoyaltyMapper;

    public BigDecimal findtodayRoyalty(Integer id) {
        return userPlusRoyaltyMapper.findtodayRoyalty(id);
    }

    public BigDecimal findMonthRoyalty(Integer id) {
        return userPlusRoyaltyMapper.findMonthRoyalty(id);
    }

    public BigDecimal findRoyalty(Integer id) {
        return userPlusRoyaltyMapper.findRoyalty(id);
    }
}

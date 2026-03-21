package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.cwork.BankCard;
import com.cheji.web.modular.domain.UserBankEntity;
import com.cheji.web.modular.mapper.UserBankMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-09-02
 */
@Service
public class UserBankService extends ServiceImpl<UserBankMapper, UserBankEntity> implements IService<UserBankEntity> {

    @Resource
    private UserBankMapper userBankMapper;


    public List<BankCard> findBankAndTail(String id) {
        return userBankMapper.findBankAndTail(id);
    }
}

package com.stylefeng.guns.modular.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.stylefeng.guns.modular.system.dao.BizYckBalanceMapper;
import com.stylefeng.guns.modular.system.model.BizYckBalance;
import com.stylefeng.guns.modular.system.service.IBizYckBalanceService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-10
 */
@Service
public class BizYckBalanceServiceImpl extends ServiceImpl<BizYckBalanceMapper, BizYckBalance> implements IBizYckBalanceService {
    @Override
    public boolean addBalance(BigDecimal amount, String openid, @Param("modifyTime") Date modifyTime, @Param("account") String account) {
        int effectived = this.baseMapper.addBalanceByAccount(amount, account, modifyTime);
        return effectived == 1 ? true : false;
    }

    @Override
    public boolean reduceBalance(BigDecimal amount, String account, Date modifyTime) {
        int effectived = this.baseMapper.reduceBalance(amount, account, modifyTime);
        return effectived == 1 ? true : false;
    }
}

package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.SettlePlusEntity;
import com.cheji.web.modular.mapper.SettlePlusMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * <p>
 * 结算plus会员明细 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-10-08
 */
@Service
public class SettlePlusService extends ServiceImpl<SettlePlusMapper, SettlePlusEntity> implements IService<SettlePlusEntity> {
    @Resource
    private SettlePlusMapper settlePlusMapper;

    public BigDecimal findtodaySettle(Integer id) {
        return settlePlusMapper.findtodatSettle(id);
    }

    public BigDecimal findMonthSettle(Integer id) {
        return settlePlusMapper.findMonthSettle(id);
    }

    public BigDecimal findSettle(Integer id) {
        return settlePlusMapper.findSettle(id);
    }
}

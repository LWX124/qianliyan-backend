package com.cheji.b.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.domain.SettlePlusEntity;
import com.cheji.b.modular.mapper.SettlePlusMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}

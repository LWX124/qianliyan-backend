package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.AppAuctionBailRefundLogEntity;
import com.cheji.web.modular.mapper.AppAuctionBailRefundLogMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 *     支付记录
 * </p>
 *
 * @author yang
 */
@Service
public class AppAuctionBailRefundLogService extends ServiceImpl<AppAuctionBailRefundLogMapper, AppAuctionBailRefundLogEntity> implements IService<AppAuctionBailRefundLogEntity> {
}

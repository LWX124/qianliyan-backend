package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.AppAuctionMyEntity;
import com.cheji.web.modular.domain.AppAuctionSubscriptionEntity;
import com.cheji.web.modular.mapper.AppAuctionMyMapper;
import com.cheji.web.modular.mapper.AppAuctionSubscriptionMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 *     订阅相关
 * </p>
 *
 * @author yang
 */
@Service
public class AppAuctionSubscriptionService extends ServiceImpl<AppAuctionSubscriptionMapper, AppAuctionSubscriptionEntity> implements IService<AppAuctionSubscriptionEntity> {
}

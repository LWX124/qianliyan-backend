package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.AppAuctionFeedBackEntity;
import com.cheji.web.modular.domain.AppAuctionTransactionLogEntity;
import com.cheji.web.modular.mapper.AppAuctionFeedBackMapper;
import com.cheji.web.modular.mapper.AppAuctionTransactionLogMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 *     交易记录
 * </p>
 *
 * @author yang
 */
@Service
public class AppAuctionTransactionLogService extends ServiceImpl<AppAuctionTransactionLogMapper, AppAuctionTransactionLogEntity> implements IService<AppAuctionTransactionLogEntity> {
}

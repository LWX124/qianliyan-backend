package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.AppAuctionMyEntity;
import com.cheji.web.modular.mapper.AppAuctionMyMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 *     我的
 * </p>
 *
 * @author yang
 */
@Service
public class AppAuctionMyService extends ServiceImpl<AppAuctionMyMapper, AppAuctionMyEntity> implements IService<AppAuctionMyEntity> {
}

package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.AppAuctionHomePageEntity;
import com.cheji.web.modular.mapper.AppAuctionHomePageMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 *     主页
 * </p>
 *
 * @author yang
 */
@Service
public class AppAuctionHomePageService extends ServiceImpl<AppAuctionHomePageMapper, AppAuctionHomePageEntity> implements IService<AppAuctionHomePageEntity> {
}

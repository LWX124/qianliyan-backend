package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.AppAuctionCarCountEntity;
import com.cheji.web.modular.mapper.AppAuctionCarCountMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 *     车数量
 * </p>
 *
 * @author yang
 */
@Service
public class AppAuctionCarCountService extends ServiceImpl<AppAuctionCarCountMapper, AppAuctionCarCountEntity> implements IService<AppAuctionCarCountEntity> {


}

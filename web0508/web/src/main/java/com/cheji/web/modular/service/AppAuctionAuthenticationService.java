package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.AppAuctionAuthenticationEntity;
import com.cheji.web.modular.domain.AppAuctionMyEntity;
import com.cheji.web.modular.mapper.AppAuctionAuthenticationMapper;
import com.cheji.web.modular.mapper.AppAuctionMapper;
import com.cheji.web.modular.mapper.AppAuctionMyMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 *     拍卖接口
 * </p>
 *
 * @author yang
 */
@Service
public class AppAuctionAuthenticationService extends ServiceImpl<AppAuctionAuthenticationMapper, AppAuctionAuthenticationEntity> implements IService<AppAuctionAuthenticationEntity> {
}

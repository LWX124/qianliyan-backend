package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.AppAuctionMyEntity;
import com.cheji.web.modular.domain.AppFileUrlEntity;
import com.cheji.web.modular.mapper.AppAuctionMyMapper;
import com.cheji.web.modular.mapper.AppFileUrlMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 *     拍卖接口
 * </p>
 *
 * @author yang
 */
@Service
public class AppFileUrlService extends ServiceImpl<AppFileUrlMapper, AppFileUrlEntity> implements IService<AppFileUrlEntity> {
}

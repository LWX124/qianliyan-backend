package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.AppAuctionMessageIdentifyEntity;
import com.cheji.web.modular.mapper.AppAuctionMessageIdentifyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *     认证信息
 * </p>
 *
 * @author yang
 */
@Service
public class AppAuctionMessageIdentifyService extends ServiceImpl<AppAuctionMessageIdentifyMapper, AppAuctionMessageIdentifyEntity> implements IService<AppAuctionMessageIdentifyEntity> {

}

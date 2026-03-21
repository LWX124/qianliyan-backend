package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.AppAuctionAlipayInfoEntity;
import com.cheji.web.modular.domain.AppAuctionHotEntity;
import com.cheji.web.modular.mapper.AppAuctionAlipayInfoMapper;
import com.cheji.web.modular.mapper.AppAuctionHotMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *     获取ali支付相关信息
 * </p>
 *
 * @author yang
 */
@Service
public class AppAuctionAlipayInfoService extends ServiceImpl<AppAuctionAlipayInfoMapper, AppAuctionAlipayInfoEntity> implements IService<AppAuctionAlipayInfoEntity> {

}

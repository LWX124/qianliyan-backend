package com.cheji.web.modular.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.AppAuctionVipControlEntity;
import com.cheji.web.modular.domain.AppAuctionVipLvEntity;
import com.cheji.web.modular.domain.AppUserEntity;
import com.cheji.web.modular.mapper.AppAuctionVipControlMapper;
import com.cheji.web.modular.mapper.AppAuctionVipLvMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *     vip
 * </p>
 *
 * @author yang
 */
@Service
public class AppAuctionVipControlService extends ServiceImpl<AppAuctionVipControlMapper, AppAuctionVipControlEntity> implements IService<AppAuctionVipControlEntity> {

    @Resource
    private AppAuctionVipControlMapper appAuctionVipControlMapper;

    public List<AppAuctionVipControlEntity> findVips(List<Long> list) {
        return appAuctionVipControlMapper.findVips(list);
    }

    public void updateStateByUser(Long userId, Integer state) {
        appAuctionVipControlMapper.updateStateByUser(userId,state);
    }
}

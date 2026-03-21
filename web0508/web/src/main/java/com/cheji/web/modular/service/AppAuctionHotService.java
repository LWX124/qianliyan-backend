package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.AppAuctionHotEntity;
import com.cheji.web.modular.mapper.AppAuctionHotMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *     热门推荐
 * </p>
 *
 * @author yang
 */
@Service
public class AppAuctionHotService extends ServiceImpl<AppAuctionHotMapper, AppAuctionHotEntity> implements IService<AppAuctionHotEntity> {

    @Resource
    private AppAuctionHotMapper appAuctionHotMapper;

    public List<AppAuctionHotEntity> findHot(String city){
        return appAuctionHotMapper.findHot(city);
    }
}

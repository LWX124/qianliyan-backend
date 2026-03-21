package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.domain.AppAuctionImgEntity;
import com.cheji.web.modular.mapper.AppAuctionImgMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *     拍卖车详情图
 * </p>
 *
 * @author yang
 */
@Service
public class AppAuctionImgService extends ServiceImpl<AppAuctionImgMapper, AppAuctionImgEntity> implements IService<AppAuctionImgEntity> {

    @Resource
    AppAuctionImgMapper appAuctionImgMapper;

    public List<AppAuctionImgEntity> selectByCarId(Long car_id) {
        return appAuctionImgMapper.selectByCarId(car_id);
    }

    public String findImgByRepair(Long carId) {
        return appAuctionImgMapper.findImgByRepair(carId);
    }

//    public void batchDelete(List<Long> upIds) {
//        appAuctionImgMapper.batchDelete(upIds);
//    }
}

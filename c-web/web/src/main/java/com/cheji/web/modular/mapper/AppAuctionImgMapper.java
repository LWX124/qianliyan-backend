package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.domain.AppAuctionImgEntity;

import java.util.List;

/**
 * <p>
 * 拍卖接口
 * </p>
 *
 * @author yang
 */
public interface AppAuctionImgMapper extends BaseMapper<AppAuctionImgEntity> {


    List<AppAuctionImgEntity> selectByCarId(Long carId);

    String findImgByRepair(Long carId);

//    void batchDelete(List<Long> upIds);
}

package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.domain.AppAuctionBidEntity;
import com.cheji.web.modular.domain.AppAuctionEntity;
import com.cheji.web.modular.domain.AppAuctionHotEntity;

import java.util.List;

/**
 * <p>
 * 拍卖接口
 * </p>
 *
 * @author yang
 */
public interface AppAuctionBidMapper extends BaseMapper<AppAuctionBidEntity> {

    Integer getValidCount(Integer id);

    //查询在拍车辆的出价记录
    Integer selectCountBidByUser(Long userId, Integer zero);
}

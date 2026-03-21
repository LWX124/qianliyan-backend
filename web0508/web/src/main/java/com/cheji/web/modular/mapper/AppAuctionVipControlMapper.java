package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.domain.AppAuctionVipControlEntity;
import com.cheji.web.modular.domain.AppAuctionVipLvEntity;

import java.util.List;

/**
 * <p>
 * 拍卖接口
 * </p>
 *
 * @author yang
 */
public interface AppAuctionVipControlMapper extends BaseMapper<AppAuctionVipControlEntity> {


    List<AppAuctionVipControlEntity> findVips(List<Long> list);

    void updateStateByUser(Long userId, Integer state);

    AppAuctionVipControlEntity selectByUserId(Integer userId);
}

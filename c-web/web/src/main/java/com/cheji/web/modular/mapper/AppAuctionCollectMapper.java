package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.domain.AppAuctionCollectEntity;

/**
 * <p>
 * 拍卖接口
 * </p>
 redisTemplate
 * @author yang
 */
public interface AppAuctionCollectMapper extends BaseMapper<AppAuctionCollectEntity> {


    AppAuctionCollectEntity isCollect(Long id, Long carId);
}

package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.domain.AppAuctionOnePriceCarLogEntity;

/**
 * <p>
 * 拍卖接口
 * </p>
 *
 * @author yang
 */
public interface AppAuctionOnePriceCarLogMapper extends BaseMapper<AppAuctionOnePriceCarLogEntity> {

    Integer selectCountByUserId(Integer userId);

    Integer countByUserId(Long userId);

    Integer sumByUserId(Integer userId);
}

package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.domain.AppAuctionOrderEntity;

/**
 * <p>
 * 拍卖接口
 * </p>
 *
 * @author yang
 */
public interface AppAuctionOrderMapper extends BaseMapper<AppAuctionOrderEntity> {


    Integer countOrderByUser(Long userId, Integer state);
}

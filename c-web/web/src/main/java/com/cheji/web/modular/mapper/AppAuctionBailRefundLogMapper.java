package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.domain.AppAuctionBailRefundLogEntity;

/**
 * <p>
 * 拍卖接口
 * </p>
 *
 * @author yang
 */
public interface AppAuctionBailRefundLogMapper extends BaseMapper<AppAuctionBailRefundLogEntity> {


    AppAuctionBailRefundLogEntity selectByOutTradeNo(String outTradeNo);
}

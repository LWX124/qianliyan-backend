package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.domain.AppAuctionPayLogEntity;

/**
 * <p>
 * 拍卖接口
 * </p>
 *
 * @author yang
 */
public interface AppAuctionPayLogMapper extends BaseMapper<AppAuctionPayLogEntity> {


    AppAuctionPayLogEntity findByNo(String orderId);


}

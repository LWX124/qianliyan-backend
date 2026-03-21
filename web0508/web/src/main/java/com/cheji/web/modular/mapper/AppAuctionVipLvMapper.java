package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.domain.AppAuctionSessionEntity;
import com.cheji.web.modular.domain.AppAuctionVipLvEntity;

/**
 * <p>
 * 拍卖接口
 * </p>
 *
 * @author yang
 */
public interface AppAuctionVipLvMapper extends BaseMapper<AppAuctionVipLvEntity> {


    AppAuctionVipLvEntity selectByVip(String vipLv);
}

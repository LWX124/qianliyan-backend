package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.domain.AppAuctionEntity;
import com.cheji.web.modular.domain.AppAuctionUpEntity;
import com.cheji.web.modular.dto.AuctionCarListDto;

import java.util.List;

/**
 * <p>
 * 拍卖接口
 * </p>
 *
 * @author yang
 */
public interface AppAuctionUpMapper extends BaseMapper<AppAuctionUpEntity> {

    List<AppAuctionEntity> todayNew(Integer page, AuctionCarListDto ac);

    Integer todayNewCount(Integer page, AuctionCarListDto ac);
}

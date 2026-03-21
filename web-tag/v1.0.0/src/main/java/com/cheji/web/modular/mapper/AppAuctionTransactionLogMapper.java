package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cheji.web.modular.domain.AppAuctionFeedBackEntity;
import com.cheji.web.modular.domain.AppAuctionTransactionLogEntity;

import java.util.List;

/**
 * <p>
 * 拍卖接口
 * </p>
 *
 * @author yang
 */
public interface AppAuctionTransactionLogMapper extends BaseMapper<AppAuctionTransactionLogEntity> {


    List<AppAuctionTransactionLogEntity> selectByUserAndType(Integer userId, Integer queryType, Integer offset,Integer limit);
}

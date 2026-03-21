package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.domain.AppAuctionHotEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 拍卖接口
 * </p>
 *
 * @author yang
 */
public interface AppAuctionHotMapper extends BaseMapper<AppAuctionHotEntity> {

    List<AppAuctionHotEntity> findHot(@Param("city") String city);
    Integer findHotCount(@Param("city") String city);

}

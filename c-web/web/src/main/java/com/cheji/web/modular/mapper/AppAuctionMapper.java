package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cheji.web.modular.domain.AppAuctionEntity;
import com.cheji.web.modular.domain.AppJgPushEntity;
import com.cheji.web.modular.dto.AuctionCarListDto;
import com.cheji.web.modular.dto.UsedCarListVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 拍卖接口
 * </p>
 *
 * @author yang
 */
public interface AppAuctionMapper extends BaseMapper<AppAuctionEntity> {

    List<AppAuctionEntity> search(@Param("address") String address, @Param("brand")String brand, @Param("current")Integer current, @Param("size")Integer size);

    Integer getCarCount();

    List<AppAuctionEntity> selectByCondition(AuctionCarListDto dto);

    Integer countByCondition(AuctionCarListDto dto);

    Integer countOldCarNum(AuctionCarListDto dto);

    List<UsedCarListVo> findListByUsedCar(Map<String, Object> params);

    List<UsedCarListVo> findListHotByUsedCar(Map<String, Object> params);

}

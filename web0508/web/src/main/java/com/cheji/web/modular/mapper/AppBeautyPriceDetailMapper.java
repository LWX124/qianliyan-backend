package com.cheji.web.modular.mapper;

import com.cheji.web.modular.domain.AppBeautyPriceDetailEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-12-20
 */
public interface AppBeautyPriceDetailMapper extends BaseMapper<AppBeautyPriceDetailEntity> {

    List<String> findUserByBeauty(@Param("beautyType")String beautyType, @Param("cityCode")Integer cityCode);

    List<AppBeautyPriceDetailEntity> findBeautyDetails(@Param("userB") String userB, @Param("beautyType")String beautyType);

    List<AppBeautyPriceDetailEntity> findDetailsByUserId(String userBId);
}

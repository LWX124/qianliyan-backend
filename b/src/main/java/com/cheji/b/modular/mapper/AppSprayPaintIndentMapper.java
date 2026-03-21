package com.cheji.b.modular.mapper;

import com.cheji.b.modular.domain.AppCleanIndetEntity;
import com.cheji.b.modular.domain.AppSprayPaintIndentEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.b.modular.dto.CenterDetailsDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 喷漆订单表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2020-03-17
 */
public interface AppSprayPaintIndentMapper extends BaseMapper<AppSprayPaintIndentEntity> {

    CenterDetailsDto selectSprayIndentCenterMes(Integer id);

    List<AppCleanIndetEntity> newSprayPaintIndentList(@Param("id")Integer id, @Param("pagesize")Integer pagesize, @Param("type")Integer type);

    //查询是否使用优惠
    Integer selectByCoupon(String cleanIndentNumber);

//    @Param("id")Integer id,  @Param("pagesize")Integer pagesize, @Param("type")String type
}

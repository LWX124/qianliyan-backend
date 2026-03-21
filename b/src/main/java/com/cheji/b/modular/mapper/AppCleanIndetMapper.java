package com.cheji.b.modular.mapper;

import com.cheji.b.modular.domain.AppCleanIndetEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.b.modular.dto.CenterDetailsDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 洗车订单表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-12-31
 */
public interface AppCleanIndetMapper extends BaseMapper<AppCleanIndetEntity> {

    List<AppCleanIndetEntity> findCleanIndent(@Param("id")Integer id,  @Param("pagesize")Integer pagesize, @Param("type")String type);

    List<AppCleanIndetEntity> findCleanIndent3(@Param("id")Integer id,  @Param("pagesize")Integer pagesize, @Param("type")String type);

    AppCleanIndetEntity findCleanIndentDetails(String cleanIndentNumber);

    String findCarImgPx(Integer carType);

    String findCarImgJx(Integer carType);

    AppCleanIndetEntity findBeautyDetails(String cleanIndentNumber);

    Integer findTodayCleanIndetn(Integer id);

    String findBeautyImg(Integer cleanType);

    CenterDetailsDto selectCleanCenterMes(Integer userBId);

    List<AppCleanIndetEntity> newCleanIndentList(@Param("id")Integer id,  @Param("pagesize")Integer pagesize, @Param("type")Integer type);
}

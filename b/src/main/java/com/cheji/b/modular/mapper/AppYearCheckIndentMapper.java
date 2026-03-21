package com.cheji.b.modular.mapper;

import com.cheji.b.modular.domain.AppCleanIndetEntity;
import com.cheji.b.modular.domain.AppYearCheckIndentEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.b.modular.dto.CenterDetailsDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 年检订单表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2020-04-02
 */
public interface AppYearCheckIndentMapper extends BaseMapper<AppYearCheckIndentEntity> {

    CenterDetailsDto selectYearIndentCenterMes(Integer userBId);

    List<AppCleanIndetEntity> newYearCheckIndentList(@Param("userBId")Integer userBId, @Param("type")Integer type,@Param("pagesize") Integer pagesize);
    //@Param("userBId")Integer userBId, @Param("pagesize")Integer pagesize, @Param("type")Integer type
}

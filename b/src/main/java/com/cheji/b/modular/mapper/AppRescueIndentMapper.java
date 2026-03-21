package com.cheji.b.modular.mapper;

import com.cheji.b.modular.domain.AppCleanIndetEntity;
import com.cheji.b.modular.domain.AppRescueIndentEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.b.modular.dto.CenterDetailsDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 救援表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2020-03-05
 */
public interface AppRescueIndentMapper extends BaseMapper<AppRescueIndentEntity> {

    CenterDetailsDto selelctRescueIndentCenterMes(Integer userBId);

    List<AppCleanIndetEntity> newRescueIndentList(@Param("userBId")Integer userBId, @Param("type")Integer type, @Param("pagesize")Integer pagesize);
  //  @Param("userBId")Integer userBId, @Param("pagesize")Integer pagesize, @Param("type")Integer type
}

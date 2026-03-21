package com.cheji.b.modular.mapper;

import com.cheji.b.modular.domain.AppCleanIndetEntity;
import com.cheji.b.modular.domain.AppSubstituteDrivingIndentEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 代驾订单表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2020-06-09
 */
public interface AppSubstituteDrivingIndentMapper extends BaseMapper<AppSubstituteDrivingIndentEntity> {

    AppSubstituteDrivingIndentEntity selectMonthIncome(Integer id);

    AppSubstituteDrivingIndentEntity selectAllMess(Integer id);

    List<AppCleanIndetEntity> newSubDriIndentList(@Param("id")Integer id, @Param("type")Integer type, @Param("pagesize")Integer pagesize);
}

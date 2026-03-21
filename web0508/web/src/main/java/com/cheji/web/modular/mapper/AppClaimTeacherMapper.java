package com.cheji.web.modular.mapper;

import com.cheji.web.modular.domain.AppClaimTeacherEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.domain.PushBillEntity;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 理赔老师表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2021-03-01
 */
public interface AppClaimTeacherMapper extends BaseMapper<AppClaimTeacherEntity> {


    Integer findAllMesCount(@Param("id") Integer id, @Param("year")String year, @Param("month")String month);

    Integer findGoMess(@Param("id") Integer id, @Param("year")String year, @Param("month")String month);

    List<PushBillEntity> findCarCount(@Param("id") Integer id, @Param("year")String year, @Param("month")String month);

    Integer findCheckMessage(@Param("id") Integer id, @Param("year")String year, @Param("month")String month);

    Integer findDealCount(@Param("id") Integer id, @Param("year")String year, @Param("month")String month);

    BigDecimal findDealOutput(@Param("id") Integer id, @Param("year")String year, @Param("month")String month);

    BigDecimal findOutput(@Param("id") Integer id, @Param("year")String year, @Param("month")String month);

    Integer findFinshMess(@Param("id") Integer id, @Param("year")String year, @Param("month")String month);

    BigDecimal findAllIncome(@Param("id") Integer id, @Param("year")String year, @Param("month")String month);


}

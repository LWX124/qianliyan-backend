package com.cheji.b.modular.mapper;

import com.cheji.b.modular.domain.CdIndentEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 车电订单表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2022-01-12
 */
public interface CdIndentMapper extends BaseMapper<CdIndentEntity> {

    Integer findNewIndent(Integer type);


    List<CdIndentEntity> selectByType(@Param("type") Integer type, @Param("text") String text, @Param("pagesize") Integer pagesize,@Param("time") String time);

    List<String> selectLiPei();

    Integer selectCount(String time, String type);

    CdIndentEntity selectIsRepaid(String plate);

    BigDecimal selectAllIncome(String time);

    BigDecimal selectAccessories(String time);

    BigDecimal selectRepair(String time);

    BigDecimal selectAllWorkPrice(String time);

    BigDecimal selectBusiness(String time);

    //-------------------------------------------

    BigDecimal selectAllIncome2(String time);

    BigDecimal selectAccessories2(String time);

    BigDecimal selectRepair2(String time);

    BigDecimal selectBusiness2(String time);

    List<CdIndentEntity> selectByType2(@Param("type") Integer type, @Param("text") String text, @Param("pagesize") Integer pagesize,@Param("time") String time);

    List<CdIndentEntity> selectByType3(@Param("pagesize") Integer pagesize,@Param("time") String time);

}

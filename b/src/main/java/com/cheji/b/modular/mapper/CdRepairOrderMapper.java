package com.cheji.b.modular.mapper;

import com.cheji.b.modular.domain.CdPartsDetailsEntity;
import com.cheji.b.modular.domain.CdRepairOrderEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * <p>
 * 工单表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2022-01-20
 */
public interface CdRepairOrderMapper extends BaseMapper<CdRepairOrderEntity> {

    CdRepairOrderEntity selectIndentByType(@Param("indentId") Integer indentId,@Param("type") Integer type);

    Integer findSaleFood(Integer indentId);

    BigDecimal findOrderMoney(@Param("indentId")Integer indentId, @Param("i")int i);

    BigDecimal selectWorkPrice(Integer indentId);
}

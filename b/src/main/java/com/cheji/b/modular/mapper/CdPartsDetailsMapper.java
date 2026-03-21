package com.cheji.b.modular.mapper;

import com.cheji.b.modular.domain.CdPartsDetailsEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 配件明细 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2022-01-19
 */
public interface CdPartsDetailsMapper extends BaseMapper<CdPartsDetailsEntity> {

    List<CdPartsDetailsEntity> addProcurementAndType(@Param("indentId") Integer indentId, @Param("type")int type);
}

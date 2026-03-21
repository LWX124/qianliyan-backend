package com.cheji.b.modular.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.b.modular.domain.CdOrderEntity;

import java.util.List;

/**
 * <p>
 * 车电订单表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2022-01-12
 */
public interface CdOrderMapper extends BaseMapper<CdOrderEntity> {

    CdOrderEntity selectIsRepaid(String plate);

    List<CdOrderEntity> selectByType(Integer type, String text, Integer pagesize,String time ,Object o);

    Integer findNewIndent(int i);
}

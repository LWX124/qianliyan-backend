package com.cheji.b.modular.mapper;

import com.cheji.b.modular.domain.CleanPriceDetailEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.b.modular.dto.PriceDetailsDto;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-12-11
 */
public interface CleanPriceDetailMapper extends BaseMapper<CleanPriceDetailEntity> {

    List<PriceDetailsDto> selectMesg(Integer userBId);
}

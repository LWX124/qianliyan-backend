package com.cheji.b.modular.mapper;

import com.cheji.b.modular.domain.BizAccident;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.b.modular.dto.ListDetailsDto;

/**
 * <p>
 * 事故上报信息表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-10-30
 */
public interface BizAccidentMapper extends BaseMapper<BizAccident> {

    ListDetailsDto findBizAccident(String accid);

    BizAccident findById(Integer accid);
}

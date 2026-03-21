package com.cheji.web.modular.mapper;

import com.cheji.web.modular.domain.BizAccidentEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 * 事故上报信息表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2021-01-12
 */
public interface BizAccidentMapper extends BaseMapper<BizAccidentEntity> {

    BizAccidentEntity findById(Integer accid);
}

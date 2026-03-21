package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.domain.LableEntity;

/**
 * <p>
 * 标签表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-08-20
 */
public interface LableMapper extends BaseMapper<LableEntity> {


    LableEntity findLable(Integer lableId);


}

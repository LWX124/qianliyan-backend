package com.cheji.b.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.b.modular.domain.AppVersionEntity;

/**
 * <p>
 * 版本 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-10-10
 */
public interface AppVersionMapper extends BaseMapper<AppVersionEntity> {


    AppVersionEntity selectNewsData();

    AppVersionEntity selectNewsCheData();

}

package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.domain.AppJgPushEntity;
import com.cheji.web.modular.domain.AppUserCarInfoEntity;

/**
 * @author Ashes
 * @since 2019-11-13
 */
public interface AppUserCarInfoMapper extends BaseMapper<AppUserCarInfoEntity> {

    AppUserCarInfoEntity selectByUserId(Integer id);

    AppUserCarInfoEntity selectByCarNumber(AppUserCarInfoEntity entity);
}

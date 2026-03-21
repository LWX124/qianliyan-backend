package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.domain.AppUserPlusEntity;

/**
 * <p>
 * 视频评论表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-08-13
 */
public interface AppUserPlusMapper extends BaseMapper<AppUserPlusEntity> {

    AppUserPlusEntity findOlusUserByid(String id);

}

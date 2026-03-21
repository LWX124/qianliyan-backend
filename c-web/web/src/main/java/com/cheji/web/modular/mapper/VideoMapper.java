package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.cwork.WorkList;
import com.cheji.web.modular.domain.VideoEntity;

/**
 * <p>
 * 视频信息表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-09-17
 */
public interface VideoMapper extends BaseMapper<VideoEntity> {

    WorkList findVideoById(String member);
}

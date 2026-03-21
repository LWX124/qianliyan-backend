package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.domain.VideoThumbsEntity;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 视频点赞表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-08-19
 */
public interface VideoThumbsMapper extends BaseMapper<VideoThumbsEntity> {

    VideoThumbsEntity findByUserIdAndVideoId(@Param("videoId")String videoId, @Param("userId")String userId);
}

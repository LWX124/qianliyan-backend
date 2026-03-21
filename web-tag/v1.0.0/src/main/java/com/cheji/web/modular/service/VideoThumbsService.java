package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.constant.RedisConstant;
import com.cheji.web.modular.domain.VideoThumbsEntity;
import com.cheji.web.modular.mapper.VideoThumbsMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 视频点赞表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-08-19
 */
@Service
public class VideoThumbsService extends ServiceImpl<VideoThumbsMapper, VideoThumbsEntity> implements IService<VideoThumbsEntity> {

    @Resource
    private RedisComService redisComService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public void save(String videoId, String userId) {
        //视频点赞操作
        redisComService.addUserLikeVideos(userId,videoId);

        double currentTime = new Long(new Date().getTime()).doubleValue();
        stringRedisTemplate.opsForZSet().add(RedisConstant.SET_VIDEO_THUMBS + videoId, userId, currentTime);
    }

    public void cancel(String videoId, String userId) {
        redisComService.jianUserLikeVideos(userId,videoId);
        stringRedisTemplate.opsForZSet().remove(RedisConstant.SET_VIDEO_THUMBS + videoId, userId);
    }



}

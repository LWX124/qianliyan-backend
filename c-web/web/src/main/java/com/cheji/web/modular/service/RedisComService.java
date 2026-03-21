package com.cheji.web.modular.service;

import com.cheji.web.constant.RedisConstant;
import com.cheji.web.modular.cwork.ThumbsCounts;
import com.cheji.web.modular.domain.VideoCommontsThumbsEntity;
import com.cheji.web.modular.domain.VideoThumbsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RedisComService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    //用户点赞过的视频
    public void addUserLikeVideos(String userId, String videoId) {
        double currentTime = new Long(new Date().getTime()).doubleValue();
        redisTemplate.opsForZSet().add(RedisConstant.LIKE_USER_THUMBS_VIDEO + userId,videoId,currentTime);
    }

    //取消视频的点赞
    public void jianUserLikeVideos(String userId, String videoId) {
        redisTemplate.opsForZSet().remove(RedisConstant.LIKE_USER_THUMBS_VIDEO + userId,videoId);
    }

    public List<VideoCommontsThumbsEntity> getLikedDataFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisConstant.MAP_KEY_USER_LIKED, ScanOptions.NONE);
        List<VideoCommontsThumbsEntity> list = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> entry = cursor.next();
            String key = (String) entry.getKey();
            //分离出 videoCommontsId，userId
            String[] split = key.split("::");
            String videoCommontsId = split[0];
            String userId = split[1];
            Object value = entry.getValue();
            Integer statu = Integer.parseInt(String.valueOf(value));

            //组装成 VideoCommontsThumbsEntity 对象
            VideoCommontsThumbsEntity VideoCommontsThumbsEntity = new VideoCommontsThumbsEntity(videoCommontsId, userId, statu);
            list.add(VideoCommontsThumbsEntity);

            //存到 list 后从 Redis 中删除
            redisTemplate.opsForHash().delete(RedisConstant.MAP_KEY_USER_LIKED, key);
        }

        return list;
    }

    public List<VideoThumbsEntity> getVideoDataFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisConstant.MAP_KEY_VIDEO_LIKED, ScanOptions.NONE);
        List<VideoThumbsEntity> list = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> entry = cursor.next();
            String key = (String) entry.getKey();
            //分离出 videoCommontsId，userId
            String[] split = key.split("::");
            String videoId = split[0];
            String userId = split[1];
            Object value = entry.getValue();
            Integer statu = Integer.parseInt(String.valueOf(value));

            //组装成 VideoCommontsThumbsEntity 对象
            VideoThumbsEntity VideoThumbsEntity = new VideoThumbsEntity(videoId, userId, statu);
            list.add(VideoThumbsEntity);

            //存到 list 后从 Redis 中删除
            redisTemplate.opsForHash().delete(RedisConstant.MAP_KEY_VIDEO_LIKED, key);
        }
        return list;
    }

    public List<ThumbsCounts> getLikedCountFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisConstant.MAP_KEY_VIDEO_LIKED_COUNT, ScanOptions.NONE);
        List<ThumbsCounts> list = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> map = cursor.next();
            //将点赞数量存储在 LikedCountDT
            String key = (String) map.getKey();
            ThumbsCounts dto = new ThumbsCounts(key, (Integer)map.getValue());
            list.add(dto);
            //从Redis中删除这条记录
            redisTemplate.opsForHash().delete(RedisConstant.MAP_KEY_VIDEO_LIKED_COUNT, key);
        }
        return list;
    }


}
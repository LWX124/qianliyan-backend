package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.constant.RedisConstant;
import com.cheji.web.constant.StatusEnum;
import com.cheji.web.modular.cwork.ThumbsCounts;
import com.cheji.web.modular.domain.VideoCommontsThumbsEntity;
import com.cheji.web.modular.domain.VideoEntity;
import com.cheji.web.modular.mapper.VideoCommontsThumbsMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户点赞表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-08-14
 */
@Service
public class VideoCommontsThumbsService extends ServiceImpl<VideoCommontsThumbsMapper, VideoCommontsThumbsEntity> implements IService<VideoCommontsThumbsEntity> {

    @Resource
    private VideoCommontsThumbsMapper thumbsMapper;

    @Resource
    private VideoCommentsService videoCommentsService;

    @Resource
    private RedisComService redisComService;

    @Resource
    private VideoService videoService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    //视频评论点赞
    public String save(String videoCommontsId, String userId) {
        //先判断有无数据
        VideoCommontsThumbsEntity byUserIdAndComId = thumbsMapper.findByUserIdAndComId(videoCommontsId, userId);
        if (byUserIdAndComId!=null){
            return "不能重复点赞";
        }
        //数据库保存数据
        //点赞保存表
        VideoCommontsThumbsEntity videoCommontsThumbs = new VideoCommontsThumbsEntity();
        videoCommontsThumbs.setVideoCommontsId(videoCommontsId);
        videoCommontsThumbs.setUserId(userId);
        videoCommontsThumbs.setStatus(1);
        videoCommontsThumbs.setCreateTime(new Date());
        insert(videoCommontsThumbs);
        //获取treecode
        //从redis中拿到数据
        String s1 = stringRedisTemplate.opsForValue().get(RedisConstant.COMMENT_VIDEO_THUMBS_COUNT + videoCommontsId);
        if (StringUtils.isNotEmpty(s1)){
            //判断有无数据，如果有就加一
            stringRedisTemplate.boundValueOps(RedisConstant.COMMENT_VIDEO_THUMBS_COUNT + videoCommontsId).increment(1);//val +1
        }

        return null;
    }

    public String  cancel(String videoCommontsId, String userId) {
        //数据库修改状态
        //根据评论id和用户id来查询到数据修改状态
        VideoCommontsThumbsEntity byUserIdAndComId = thumbsMapper.findByUserIdAndComId(videoCommontsId, userId);
        if (null==byUserIdAndComId){
            return "没有点赞";
        }
        if(byUserIdAndComId.getStatus()==0){
            return "不能重复取消";
        }
        byUserIdAndComId.setStatus(0);
        updateById(byUserIdAndComId);
        String s1 = stringRedisTemplate.opsForValue().get(RedisConstant.COMMENT_VIDEO_THUMBS_COUNT + videoCommontsId);
        if (StringUtils.isNotEmpty(s1)){
            //判断有无数据，如果有就加一
            stringRedisTemplate.boundValueOps(RedisConstant.COMMENT_VIDEO_THUMBS_COUNT + videoCommontsId).increment(-1);//val +1
        }
        return null;
    }

    /**
     * 根据被点赞评论的id查询点赞列表（即查询谁给这个评论点赞过）
     * @return
     */
    public Integer getUserlistByVideoId(String videoCommontsId) {
        return thumbsMapper.findByVideoIdAndStatus(videoCommontsId, StatusEnum.LIKE.getCode());
    }
    /**
     * 根据点赞人的id查询点赞列表（即查询这个人都给哪些评论点赞过）
     */
    public List<VideoCommontsThumbsEntity> getVideoComByUserList(String userId) {
        return thumbsMapper.findByUserIdAndStatus(userId, StatusEnum.LIKE.getCode());
    }
    /**
     * 通过被点赞评论和点赞人id查询是否存在点赞记录
     */
    public VideoCommontsThumbsEntity getByVideoComIdAndUserId(String videoCommontsId, String userId) {
        return thumbsMapper.findByUserIdAndComId(videoCommontsId, userId);
    }
    /**
     * 将Redis里的评论点赞数据存入数据库中
     */
    public void transLikedFromRedis2DB() {
        List<VideoCommontsThumbsEntity> list = redisComService.getLikedDataFromRedis();
        for (VideoCommontsThumbsEntity like : list) {
            System.out.println(like);
            VideoCommontsThumbsEntity ul = getByVideoComIdAndUserId(like.getVideoCommontsId(), like.getUserId());
            if (ul == null){
                //没有记录，直接存入
                insert(like);
            }else{
                //有记录，需要更新
                ul.setStatus(like.getStatus());
                updateById(ul);
            }
        }
    }

    public void transLikedCountFromRedis2DB() {
        List<ThumbsCounts> list = redisComService.getLikedCountFromRedis();
        for (ThumbsCounts dto : list) {
            VideoEntity video = videoService.selectById(dto.getVideoId());
            //点赞数量属于无关紧要的操作，出错无需抛异常
            if (video != null){
                Integer likeNum = video.getCount() + (Integer) dto.getThumbsCounts();
                video.setCount(likeNum);
                //更新点赞数量
                videoService.updateById(video);
            }
        }
    }

    public Long findCommentThumbsCount(String treeCode) {
        return thumbsMapper.findCommentThumbsCount(treeCode);
    }

    public VideoCommontsThumbsEntity findIsThumbs(Integer userId, String treeCode) {
        return thumbsMapper.findIsThumbs(userId,treeCode);
    }
}

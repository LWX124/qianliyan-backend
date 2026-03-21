package com.jeesite.modules.job;

import com.jeesite.modules.app.entity.AppVideoThumbs;
import com.jeesite.modules.app.entity.ThumbsCounts;
import com.jeesite.modules.app.service.AppVideoService;
import com.jeesite.modules.app.service.AppVideoThumbsService;
import com.jeesite.modules.constant2.AppConstants;
import com.jeesite.modules.constant2.RedisKeyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;


//定时器操作
@Component
@Configuration
@EnableScheduling
public class APPVideoRedisJob {
    private final static Logger logger = LoggerFactory.getLogger(APPVideoRedisJob.class);

    @Autowired
    private AppVideoService appVideoService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private AppVideoThumbsService appVideoThumbsService;

    /*
     * 要调度的具体任务
     */


    //每天凌晨12点 删除七天之前的数据  ZREMRANGEBYSCORE key min max
//    @Scheduled(cron = "0 0 12 * * ?")
//    public void del() {
//        logger.info("删除三十天之前的数据##"+"当前时间:"+new Date());
//        //获取到晚上十二点的时间
//        long nowTime = System.currentTimeMillis();
//        long todayStartTime = nowTime - (nowTime + TimeZone.getDefault().getRawOffset()) % (1000 * 3600 * 24);
//        //todayStartTime:1564934400000
//
//        //获取到七天前的时间
//        long DAY_IN_MS = 1000 * 60 * 60 * 24;
//        Date date1 = new Date(todayStartTime - (30 * DAY_IN_MS));
//        System.out.println(date1);
//        //三十天前的晚上十二点
//        long min = date1.getTime();
//        //四十天之前的时间
//        long DAY_IN_MS2 = 1000 * 60 * 60 * 24;
//        Date date2 = new Date(todayStartTime - (40 * DAY_IN_MS2));
//        long max = date2.getTime();
//
//       // Long removeRangeByScore(K key, double min, double max);
//       // 根据指定的score值得范围来移除成员
//        redisTemplate.opsForZSet().removeRangeByScore(AppConstants.APP_VIDEO_LIST, min, max);
//
//    }

//    //每天晚上12点删除商户访问量数据
//    @Scheduled(cron = "0 0 12 * * ?")
//    public void deleteMerchantsCount(){
//        logger.info("删除商户访问数据##"+"当前时间:"+new Date());
//        List<AppMerchantsTree> merchantsTrees = appMerchantsTreeService.findall();
//
//        //删除商户访问数据，每天晚上12点
//        for (AppMerchantsTree merchantsTree : merchantsTrees) {
//            //移除商户点赞数据
//            redisTemplate.opsForZSet().removeRange(AppConstants.MERCHANTS_VISIT_COUNT +merchantsTree.getMerchantsCode() , 0,-1 );
//        }
//    }


    //保存redis中的点赞数据到数据库
   /* @Scheduled(fixedDelay = 1000 * 10000)
    public void transVideoFromRedis2DB() {
        List<AppVideoThumbs> list = getVideoDataFromRedis();
        for (AppVideoThumbs videoThumbs : list) {
            System.out.println("videoThumbsEntity============"+videoThumbs);
            AppVideoThumbs ul = appVideoThumbsService.getByVideoIdAndUserId(videoThumbs.getVideoId(), videoThumbs.getUserId());
            if (ul == null) {
                //没有记录，直接存入
                appVideoThumbsService.insert(videoThumbs);
            } else {
                ul.setStatus(videoThumbs.getStatus());
                appVideoThumbsService.update(ul);
            }
        }
    }
*/
    //点赞总数
   /* @Scheduled(fixedDelay = 1000 * 10000)
    public void transLikedCountFromRedis2DB() {
        List<ThumbsCounts> list = getLikedCountFromRedis();
        for (ThumbsCounts dto : list) {
            AppVideo video = appVideoService.get(dto.getVideoId());
            if (video != null){
                String thumbsCounts = dto.getThumbsCounts();
                Integer thumbsC = Integer.valueOf(thumbsCounts);
                Integer count = video.getCount();
                Integer likeNum = count + thumbsC;
                video.setCount(likeNum);
                System.out.println("video=============="+video);
                //更新点赞数量
                appVideoService.update(video);
            }
        }
    }*/


   /* @Scheduled(fixedDelay = 1000 * 10000)
    public void work() {
        logger.info("五分钟更新一次缓存数据##"+"当前时间:"+new Date());

        //设置缓存数据
        List<AppVideo> findredis = appVideoService.findredis();
        for (AppVideo findredi : findredis) {
            Date creatTime = findredi.getCreatTime();
            long time = creatTime.getTime();
            JSONObject in = new JSONObject();
            in.put("url", findredi.getUrl());
            in.put("userId", findredi.getUserId());
            in.put("count", findredi.getCount());
            in.put("share", findredi.getShare());
            in.put("appViewCounts", findredi.getAppViewCounts());
            in.put("accidentId", findredi.getAccidentId());
            in.put("creatTime", findredi.getCreatTime());
            in.put("name", findredi.getName());
            //根据是否展示到app筛选数据
            if (findredi.getAppShowFalg()==0){
                continue;
            }
            System.out.println(findredi);
            //                                    key                         数据          时间分数
            redisTemplate.opsForZSet().add(AppConstants.APP_VIDEO_LIST, in.toJSONString(),time);
        }
    }*/




    private List<AppVideoThumbs> getVideoDataFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtils.MAP_KEY_VIDEO_LIKED, ScanOptions.NONE);
        List<AppVideoThumbs> list = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> entry = cursor.next();
            String key = (String) entry.getKey();
            //分离出 videoCommontsId，userId
            String[] split = key.split("::");
            String videoId = split[0];
            String userId = split[1];
            Object value = entry.getValue();
            String statu = String.valueOf(value);

            //组装成 VideoCommontsThumbsEntity 对象
            AppVideoThumbs VideoThumbs = new AppVideoThumbs(videoId, userId, statu);
            list.add(VideoThumbs);

            //存到 list 后从 Redis 中删除
            redisTemplate.opsForHash().delete(RedisKeyUtils.MAP_KEY_VIDEO_LIKED, key);
        }
        return list;
    }

    //保存点赞数据
    public List<ThumbsCounts> getLikedCountFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, ScanOptions.NONE);
        List<ThumbsCounts> list = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> map = cursor.next();
            //将点赞数量存储在 LikedCountDT
            String key = (String) map.getKey();
            String value =(String) map.getValue();
            ThumbsCounts dto = new ThumbsCounts(key, value);
            System.out.println("dto==========="+dto);
            list.add(dto);
            //从Redis中删除这条记录
            redisTemplate.opsForHash().delete(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, key);
        }
        return list;
    }
}
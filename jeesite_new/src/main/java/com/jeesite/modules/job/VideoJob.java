package com.jeesite.modules.job;

import com.jeesite.modules.app.dao.AppVideoDao;
import com.jeesite.modules.app.entity.AppVideo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * c端播放视频最低分
 */
@Component
@Configuration
@EnableScheduling
public class VideoJob {

    private final static Logger logger = LoggerFactory.getLogger(VideoJob.class);

    @Resource
    private AppVideoDao appVideoDao;

//    @Scheduled(fixedDelay = 1000 * 60 * 1)
    public void start() {
        int code = 5;
        logger.info("###视频播放  当前时间###{}", LocalDateTime.now());
        AppVideo appVideo = appVideoDao.select30DayMinScore();

    }
}

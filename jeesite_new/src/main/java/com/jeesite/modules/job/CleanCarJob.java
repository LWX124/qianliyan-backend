package com.jeesite.modules.job;

import com.jeesite.modules.app.dao.AppCleanIndetDao;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 洗车定时任务
 */
@Component
@Configuration
@EnableScheduling
public class CleanCarJob {

    @Resource
    private AppCleanIndetDao appCleanIndetDao;

//    /**
//     * 修改过期订单状态
//     */
//    //每30分钟执行一次
//    @Scheduled(cron = "0 0/30 * * * ?")
//    public void updateIndentStatus() {
//        //默认洗车订单七天有效期  超过七天的订单标记为已过期
//        appCleanIndetDao.updateStatus4SevenDay();
//    }
}

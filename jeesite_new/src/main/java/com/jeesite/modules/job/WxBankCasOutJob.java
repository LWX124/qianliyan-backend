package com.jeesite.modules.job;

import com.jeesite.modules.app.dao.AppUserDao;
import com.jeesite.modules.app.dao.AppWxBankInterfaceDao;
import com.jeesite.modules.app.dao.AppWxCashOutRecordDao;
import com.jeesite.modules.app.entity.AppWxCashOutRecord;
import com.jeesite.modules.app.service.WxPayBankService;
import com.jeesite.modules.job.service.BankArrivalAccountService;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 微信
 * 企业付款到银行卡定时任务
 */
@Component
@Configuration
@EnableScheduling
public class WxBankCasOutJob {

    private final static Logger logger = LoggerFactory.getLogger(WxBankCasOutJob.class);

    @Resource
    private BankArrivalAccountService bankArrivalAccountService;

    @Resource
    private AppWxCashOutRecordDao appWxCashOutRecordDao;

    @Resource
    private AppWxBankInterfaceDao appWxBankInterfaceDao;

    @Resource
    private AppUserDao appUserDao;

    @Resource
    private WxPayBankService wxPayBankService;

    /**
     * 每天4点  8点 12点  16点 20点  执行一次操作  避开所有可能发布的时间
     */
    @Scheduled(cron = "0 0 4 * * ?")
    public void work4() {
        start();
    }

    @Scheduled(cron = "0 0 8 * * ?")
    public void work8() {
        start();
    }

    @Scheduled(cron = "0 0 12 * * ?")
    public void work12() {
        start();
    }

    @Scheduled(cron = "0 0 16 * * ?")
    public void work16() {
        start();
    }

    @Scheduled(cron = "0 0 20 * * ?")
    public void work20() {
        start();
    }


    @Value("${proFileName}")
    private String proFileName;

    //    @Scheduled(fixedDelay = 1000 * 60 * 5)
//    @Scheduled(fixedDelay = 1000 * 60 * 60 * 2)
    public void start() {
        logger.info("###执行企业付款到银行卡定时任务  当前时间###{}", LocalDateTime.now());
        if (proFileName.equals("dev") || proFileName.equals("test")) {
            return;
        }
        //查询提现记录表
        List<AppWxCashOutRecord> list = appWxCashOutRecordDao.selectForJob();
        for (AppWxCashOutRecord appWxCashOutRecord : list) {
            bankArrivalAccountService.payToBank(appWxCashOutRecord);
        }
    }

//    @Scheduled(fixedDelay = 1000 * 30)
////    @Scheduled(fixedDelay = 1000 * 60 * 60 * 2)
//    public void play() {
//        logger.info("###尝试操作  当前时间###{}", LocalDateTime.now());
////        if (proFileName.equals("dev")) {
////            return;
////        }
//        //查询提现记录表
//    }


    /**
     * 查询订单是否到账
     */
    @Scheduled(fixedDelay = 1000 * 60 * 60 * 1)
    // @Scheduled(cron="0 10 16 * * ?")
    public void findOrderResult() {
        if (proFileName.equals("dev")) {
            return;
        }
        List<Map> list = appWxCashOutRecordDao.selectForResult();
        for (Map map : list) {
            try {
                bankArrivalAccountService.findOrderResult(map);
                Thread.sleep(100);//接口调用频率限制	30/s
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

}

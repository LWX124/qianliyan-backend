package com.jeesite.modules.job;

import cn.hutool.core.io.resource.ResourceUtil;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.app.dao.AppAuctionPayLogDao;
import com.jeesite.modules.app.dao.AppAuctionWithdrawcashDao;
import com.jeesite.modules.app.entity.AppAccidentRecord;
import com.jeesite.modules.app.entity.AppAuctionPayLog;
import com.jeesite.modules.app.entity.AppAuctionWithdrawcash;
import com.jeesite.modules.app.entity.BizAccident;
import com.jeesite.modules.app.service.AppAccidentRecordService;
import com.jeesite.modules.app.service.AppAuctionBailRefundLogService;
import com.jeesite.modules.app.service.BizAccidentService;
import com.jeesite.modules.constant2.RedisKeyUtils;
import com.jeesite.modules.util2.COrderNoUtil;
import com.jeesite.modules.util2.WxPayV2Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@Configuration
@EnableScheduling
public class AccidentJob {
    private final static Logger logger = LoggerFactory.getLogger(AccidentJob.class);

    @Resource
    private AppAccidentRecordService appAccidentRecordService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private BizAccidentService bizAccidentService;
    @Resource
    private AppAuctionWithdrawcashDao appAuctionWithdrawcashDao;
    @Resource
    private AppAuctionPayLogDao appAuctionPayLogDao;

    @Resource
    private WxPayV2Util wxPayV2Util;

    @Autowired
    private AppAuctionBailRefundLogService bailRefundLogService;

    @Value("${wx.appId}")
    private String appId;
    @Value("${wx.mchId}")
    private String mchId;
    @Value("${wx.mchKey}")
    private String mchKey;
    @Value("${wx.certPath}")
    private String certPath;

    //vip开始保证金退款
    @Scheduled(fixedDelay = 1000 * 60 * 5) //5分钟执行一次
    public void moneyBack() {
        logger.info("###vip开始保证金退款######");
        bailRefundLogService.moneyBack();
    }

    //查询vip开始保证金退款
    @Scheduled(fixedDelay = 1000 * 60 * 5) //5分钟执行一次
    public void queryMoneyBack() {
        logger.info("###查询vip保证金退款######");
        bailRefundLogService.queryMoneyBack();
    }

    //每五分钟执行一次
    @Scheduled(cron = "0 0/5 * * * ?")
    public void deletGeoAccident() {
        // logger.info("五分钟执行一次删除事故geo任务");
        //查询没有从Geo中移除的数据
        List<AppAccidentRecord> accidentRecords = appAccidentRecordService.selectNotDelGeo();

        List<BizAccident> bizAccidents = bizAccidentService.selectNoDel();
        //logger.error("### 当前geo中事故条数 # size={}", accidentRecords.size() + bizAccidents.size());
        if (!bizAccidents.isEmpty()) {
            for (BizAccident bizAccident : bizAccidents) {
                Date createtime = bizAccident.getCreatetime();
                Calendar dateOne = Calendar.getInstance();
                Calendar dateTwo = Calendar.getInstance();
                dateOne.setTime(new Date());
                dateTwo.setTime(createtime);
                long timeOne = dateOne.getTimeInMillis();
                long timeTwo = dateTwo.getTimeInMillis();
                long minute = (timeOne - timeTwo) / (1000 * 60);
                if (minute > 30) {
                    if (StringUtils.isNotEmpty(bizAccident.getGeoredis())) {
                        redisTemplate.opsForGeo().remove(RedisKeyUtils.ACCIDENT_GEO, bizAccident.getGeoredis());
                    }
                    bizAccident.setDelgeo("1");
                    bizAccidentService.update(bizAccident);
                }
            }
            //logger.info("删除小程序上传geo成功");
        } else {
            //logger.info("五分钟执行一次删除事故geo任务,小程序上传暂无数据");
        }

        if (!accidentRecords.isEmpty()) {
            //如果创建时间大于三十分钟就从geo中移除数据
            for (AppAccidentRecord accidentRecord : accidentRecords) {
                Date time = accidentRecord.getCreateTime();
                Calendar dateOne = Calendar.getInstance();
                Calendar dateTwo = Calendar.getInstance();
                dateOne.setTime(new Date());
                dateTwo.setTime(time);
                long timeOne = dateOne.getTimeInMillis();
                long timeTwo = dateTwo.getTimeInMillis();
                long minute = (timeOne - timeTwo) / (1000 * 60);
                if (minute > 30) {
                    //从geo中移除数据
                    if (StringUtils.isNotEmpty(accidentRecord.getGeoredis())) {
                        redisTemplate.opsForGeo().remove(RedisKeyUtils.ACCIDENT_GEO, accidentRecord.getGeoredis());
                    }
                    accidentRecord.setDelgeo("1");
                    appAccidentRecordService.update(accidentRecord);
                }
            }
            //logger.info("删除app上传geo成功");
        } else {
            // logger.info("五分钟执行一次删除事故geo任务,app上传暂无数据");
        }
    }
}

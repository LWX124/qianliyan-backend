package com.jeesite.modules.job;

import com.alibaba.fastjson.JSONObject;
import com.ctc.wstx.util.StringUtil;
import com.jeesite.modules.app.dao.AppBUserDao;
import com.jeesite.modules.app.entity.*;
import com.jeesite.modules.app.service.*;
import com.jeesite.modules.constant2.JgTokenEnum;
import com.jeesite.modules.constant2.RedisKeyUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@Configuration
@EnableScheduling
public class EveryDayMesJob {
    private final static Logger logger = LoggerFactory.getLogger(EveryDayMesJob.class);

    @Resource
    private AppEveryMesgService appEveryMesgService;

    @Resource
    private AppBUserService appBUserService;

    @Resource
    private AppUserService appUserService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private AppRescueIndentService appRescueIndentService;

    @Resource
    private AppBUserDao appBUserDao;

    @Resource
    private AppUserAccountRecordService appUserAccountRecordService;

    @Resource
    private AppIndentService appIndentService;

    @Resource
    private AppClaimTeacherService appClaimTeacherService;


    //查询每天的数据，放到库里面  每天1点执行
    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional(readOnly = false)
    public void everyMesJob() {
        logger.info("开始执行添加每日数据任务");
        //检查前一天的数据是否生成
        Date time1 = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar instance = Calendar.getInstance();
        instance.setTime(time1);
        instance.add(Calendar.DAY_OF_MONTH, -2);
        //获取到前一天的数据
        String format = formatter.format(instance.getTime());
        System.out.println(format);
        //查询到前两天的数据
        AppEveryMesg appEveryMesg = appEveryMesgService.selectTwoAgo(format);
        if (appEveryMesg == null) {
            //查询到哪天的数据
//            private String todayAccident;		//今日事故
            addTodayMesg(format);
        }

        //有数据就添加今天的数据
        Date time2 = new Date();
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
        Calendar instance2 = Calendar.getInstance();
        instance2.setTime(time2);
        instance2.add(Calendar.DAY_OF_MONTH, -1);
        //获取到前一天的数据
        String format2 = formatter2.format(instance2.getTime());
        System.out.println(format2);
        AppEveryMesg todayEveryMesg = appEveryMesgService.selectTwoAgo(format2);
        //查询到今天的数据
        if (todayEveryMesg == null) {
            addTodayMesg(format2);
        } else {
            logger.info("数据已生成");

        }
        logger.info("执行完成");
    }


    private void addTodayMesg(String format) {
        AppAccidentRecord mesg = appEveryMesgService.selectPayAmountAndCount(format);
        String envelopeAmount = mesg.getEnvelopeAmount();//app红包金额
        String redEnvelopes = mesg.getRedEnvelopes();   //app红包个数

        BigDecimal wxAmount = appEveryMesgService.selectWxAmount(format); //微信红包金额
        String wxCount = appEveryMesgService.selectWxCount(format); //微信红包个数
        AppAccidentRecord accidentCount = appEveryMesgService.selecTodayAccident(format);
        String todayAppup = accidentCount.getTodayAppup();  //今日app上传事故
        AppAccidentRecord plus = appEveryMesgService.selectPlusPayAmountCount(format);
        String plusEnevlopeAmount = plus.getPlusEnevlopeAmount();//plus会员提成金额
        String plusEnevlopeCount = plus.getPlusEnevlopeCount();//plus会员提成个数
        AppAccidentRecord push = appEveryMesgService.selectPassAccidnt(format);
        String passAccident = push.getPassAccident();  //今日通过事故个数
        AppAccidentRecord wxUp = appEveryMesgService.selectWxUp(format);
        String todayWxup = wxUp.getTodayWxup();         //今日小程序上传
        String todayAccident = String.valueOf(Integer.valueOf(todayWxup) + Integer.valueOf(todayAppup));  //今日事故总数

        AppEveryMesg appEveryMesg1 = new AppEveryMesg();
        appEveryMesg1.setCreateTime(new Date());
        BigDecimal add = wxAmount.add(new BigDecimal(envelopeAmount));
        appEveryMesg1.setEnvelopeAmount(add);   //今日红包总金额
        //今日红包总数
        appEveryMesg1.setRedEnvelopes(String.valueOf(Integer.valueOf(redEnvelopes) + Integer.valueOf(wxCount)));
        //plus会员红包金额
        appEveryMesg1.setPlusEnevlopeAmount(new BigDecimal(plusEnevlopeAmount));
        //plus会员总数
        appEveryMesg1.setPlusEnevlopeCount(plusEnevlopeCount);
        //今日事故总数
        appEveryMesg1.setTodayAccident(todayAccident);
        //今日小程序上传
        appEveryMesg1.setTodayWxup(todayWxup);
        //今日app上传
        appEveryMesg1.setTodayAppup(todayAppup);
        //今日推送个数
        appEveryMesg1.setPassAccident(passAccident);
        appEveryMesg1.setCreateTime(new Date());
        appEveryMesg1.setTodayTime(format);
        appEveryMesgService.insert(appEveryMesg1);

        //设置信息费总数
        //信息费总数为空
        String s = (String) redisTemplate.opsForValue().get(RedisKeyUtils.MESSAGE_AMOUNT);
        if (StringUtils.isEmpty(s)) {
            redisTemplate.opsForValue().set(RedisKeyUtils.MESSAGE_AMOUNT,new BigDecimal("0").toString());
        }
        //减去今天的信息费
        BigDecimal subtract = new BigDecimal(s).subtract(add);
        redisTemplate.opsForValue().set(RedisKeyUtils.MESSAGE_AMOUNT,subtract.toString());


    }


    //删除每日商户访问数据
    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional(readOnly = false)
    public void deleteMerchantsVisit() {
        logger.info("开始执行删除每日商户数据任务");
        List<Integer> userbid = appBUserService.findAllMerchants();
        for (Integer integer : userbid) {
            redisTemplate.opsForZSet().removeRange(RedisKeyUtils.MERCHANTS_VISIT_COUNT + integer, 0, -1);
        }
        logger.info("执行删除每日商户成功");
    }


    //查询救援订单，找到状态为2且创建时间大于三天的所有数据，创建时间大于三天就结算发消息
    @Scheduled(cron = "0 */30 * * * ?")
    @Transactional(readOnly = false)
    public void settleOrder() {
        List<AppRescueIndent> appRescueIndents = appRescueIndentService.findOverThreeIndent();
        if (appRescueIndents.isEmpty()) {
            //List为空，数据为空，
            logger.info("三日之前无未结算订单");
        } else {
            for (AppRescueIndent appRescueIndent : appRescueIndents) {
                //结算订单，修改订单状态，发钱。推送通知。
                AppBUser appBUser = appBUserDao.updateBalance(appRescueIndent.getUserBId().intValue());
                if (appBUser == null) {
                    logger.info("没有查询到改商户:" + appRescueIndent.getUserBId());
                } else {
                    BigDecimal price = BigDecimal.valueOf(appRescueIndent.getPrice());
                    BigDecimal mulitply = price.multiply(new BigDecimal(0.8).setScale(2, BigDecimal.ROUND_HALF_UP));
                    appBUser.setBalance(appBUser.getBalance().add(mulitply));

                    AppUserAccountRecord appUserAccount = new AppUserAccountRecord();
                    appUserAccount.setMomey(mulitply);
                    appUserAccount.setUserId(appRescueIndent.getUserBId());
                    appUserAccount.setType(15);
                    appUserAccount.setCreateTime(new Date());
                    appUserAccount.setAddFlag(1);
                    appUserAccount.setSource(2);
                    appUserAccount.setBusinessId(appRescueIndent.getRescueNumber());
                    appUserAccountRecordService.insert(appUserAccount);

                    AppUserAccountRecord chejiBalance = new AppUserAccountRecord();
                    chejiBalance.setMomey(price.subtract(mulitply));
                    chejiBalance.setUserId(appRescueIndent.getUserBId());
                    chejiBalance.setType(-1);
                    chejiBalance.setCreateTime(new Date());
                    chejiBalance.setAddFlag(1);
                    chejiBalance.setSource(1);
                    chejiBalance.setBusinessId(appRescueIndent.getRescueNumber());
                    appUserAccountRecordService.insert(chejiBalance);

                    appRescueIndent.setState(6);
                    appRescueIndentService.update(appRescueIndent);
                    //推送通知
                    AppUser appUser = appUserService.get(String.valueOf(appRescueIndent.getUserId()));
                    String username = appUser.getUsername();
                    String substring = username.substring(username.length() - 4);
                    String str = "收到尾号" + substring + "用户的救援订单结算";
                    JPushService.jiguangPush(appBUser.getUsername(), str, JgTokenEnum.B, "7");
                }
            }
        }
    }


    //超过七天的对账数据就变成未对账
    //一点半执行  找到所有的对账时间超过7天的数据修改为未对账
    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional(readOnly = false)
    public void updatacolleatTime() {
        List<AppIndent> indentList = appIndentService.findAllNoColleatIndent();
        for (AppIndent appIndent : indentList) {
            appIndent.setIsRec(0);
            appIndent.setCollateTime(new Date());
            appIndentService.update(appIndent);
        }
        logger.info("执行修改7天前对账订单成功");
    }


    //每小时执行一次
     @Scheduled(cron = "0 0 */1 * * ?")
//    @Scheduled(fixedDelay = 1000 * 60)
    @Transactional(readOnly = false)
    public void updateClaTeacher() {
        //修改理赔老师事故
        //删除所有的理赔老师，重新添加
        //del redis
        redisTemplate.delete(RedisKeyUtils.CLAIM_TEACHER_ADD);
        //查询到理赔老师
        List<AppClaimTeacher> claimTeachers = appClaimTeacherService.findClaTeacher();
        //根据添加过的数据重新添加
        for (AppClaimTeacher claimTeacher : claimTeachers) {
            String currentPosition = claimTeacher.getCurrentPosition();
            //转换成json重新添加到redis
//                {"merchantsName":"Atorax","lng":104.08962,"id":55,"type":7,"lat":30.591076}
            JSONObject object = JSONObject.parseObject(currentPosition);
            Double lng = object.getDouble("lng");
            Double lat = object.getDouble("lat");
            redisTemplate.opsForGeo().add(RedisKeyUtils.CLAIM_TEACHER_ADD, new Point(lng, lat), currentPosition);
        }
        //更新成功
        logger.info("执行更新理赔老师位置成功");
    }
}

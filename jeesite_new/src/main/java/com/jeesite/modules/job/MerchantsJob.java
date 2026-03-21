package com.jeesite.modules.job;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.app.entity.AppClaimTeacher;
import com.jeesite.modules.app.entity.AppSubstituteDrivingIndent;
import com.jeesite.modules.app.entity.AppUpMerchants;
import com.jeesite.modules.app.entity.AppUser;
import com.jeesite.modules.app.service.AppClaimTeacherService;
import com.jeesite.modules.app.service.AppSubstituteDrivingIndentService;
import com.jeesite.modules.app.service.AppUpMerchantsService;
import com.jeesite.modules.app.service.AppUserService;
import com.jeesite.modules.constant2.RedisKeyUtils;
import com.jeesite.modules.util2.GaoDeUtils;
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
import java.util.Calendar;
import java.util.List;

@Component
@Configuration
@EnableScheduling
public class MerchantsJob {
    private final static Logger logger = LoggerFactory.getLogger(MerchantsJob.class);


    @Resource
    private AppUserService appUserService;

    @Resource
    private AppSubstituteDrivingIndentService appSubstituteDrivingIndentService;

    @Resource
    private AppClaimTeacherService appClaimTeacherService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private AppUpMerchantsService appUpMerchantsService;


    //0 0 0 * * ?
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional(readOnly = false)
    public void putBuserInblack() {
        logger.info("开始执行定时任务，B端注册了的商户C端拉黑");
        appUserService.updateBlackName();
    }

    //每天晚上1点确认超过两天的代驾订单
    //查询到状态为5，并且时间超过两天
    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional(readOnly = false)
    public void confirmSubDriIndent() {
        logger.info("开始执行定时任务，确认超过两天的代驾订单");
        List<AppSubstituteDrivingIndent> list = appSubstituteDrivingIndentService.findNoCondirmIndent();
        for (AppSubstituteDrivingIndent appSubstituteDrivingIndent : list) {
            appSubstituteDrivingIndent.setIndentState(6);
            appSubstituteDrivingIndentService.update(appSubstituteDrivingIndent);
        }
        logger.info("执行定时任务结束，订单条数" + list.size());
    }


    //    每小时查询一次
    @Scheduled(cron = "0 0 */1 * * ?")
    @Transactional(readOnly = false)
    public void AddMerchants() {
        //查询到有地址没有添加到redis的商户地址
        List<AppClaimTeacher> list = appClaimTeacherService.findMerchants();
        if (list.isEmpty()) {
            logger.info("执行定时添加商户任务，没有未添加商户");
            return;
        } else {
            for (AppClaimTeacher appClaimTeacher : list) {
                //根据地址查询到经纬度
                JSONObject positionObj = GaoDeUtils.getLngAndLat(appClaimTeacher.getAddress());
                String longitude = positionObj.getString("longitude");
                String latitude = positionObj.getString("latitude");
                //添加到修理厂中
                //先删
                Integer userId = appClaimTeacher.getUserId();
                AppUser appUser = appUserService.get(userId.toString());
                JSONObject in = new JSONObject();
                in.put("id", appClaimTeacher.getUserId());
                in.put("merchantsName", appUser.getName());
                in.put("lat", new BigDecimal(latitude));
                in.put("lng", new BigDecimal(longitude));
                in.put("address", appClaimTeacher.getAddress());
                if (appClaimTeacher.getType() == 3) {
                    //修理厂
                    in.put("type", 1);
                    if (appClaimTeacher.getCurrentPosition() != null) {
                        try {
                            redisTemplate.opsForGeo().remove(RedisKeyUtils.MERCHANTS_GEO + 2, appClaimTeacher.getCurrentPosition());
                        } catch (Exception e) {
                            logger.error("执行定时添加商户任务，删除geo失败 id={}", appClaimTeacher.getId());
                        }
                    }
                    redisTemplate.opsForGeo().add(RedisKeyUtils.MERCHANTS_GEO + 2, new Point(Double.parseDouble(longitude), Double.parseDouble(latitude)), in.toJSONString());
                    appClaimTeacher.setCurrentPosition(in.toJSONString());
                } else if (appClaimTeacher.getType() == 4) {
                    //运营商，实行扣费标准
                    in.put("type", 4);
                    if (appClaimTeacher.getCurrentPosition() != null) {
                        try {
                            redisTemplate.opsForGeo().remove(RedisKeyUtils.CLAIM_TEACHER_MER, appClaimTeacher.getCurrentPosition());
                        } catch (Exception e) {
                            logger.error("执行定时添加商户任务，删除geo失败 id={}", appClaimTeacher.getId());
                        }
                    }
                    redisTemplate.opsForGeo().add(RedisKeyUtils.CLAIM_TEACHER_MER, new Point(Double.parseDouble(longitude), Double.parseDouble(latitude)), in.toJSONString());
                    appClaimTeacher.setCurrentPosition(in.toJSONString());
                } else {
                    //4s店
                    //拿到品牌字段，根据逗号遍历，添加到对应品牌下面去
                    String brand = appClaimTeacher.getBrand();
                    if (StringUtils.isNotEmpty(brand)) {
                        in.put("type", brand);
                        //不为空
                        //按照逗号分开
                        //添加到对应的品牌下面去
                        if (appClaimTeacher.getCurrentPosition() != null) {
                            try {                               //RedisKeyUtils.CLAIM_TEACHER_MER
                                redisTemplate.opsForGeo().remove(RedisKeyUtils.MERCHANTS_GEO + brand, appClaimTeacher.getCurrentPosition());
                            } catch (Exception e) {
                                logger.error("执行定时添加商户任务，删除geo失败 id={}", appClaimTeacher.getId());
                            }
                        }
                        redisTemplate.opsForGeo().add(RedisKeyUtils.MERCHANTS_GEO + brand, new Point(Double.parseDouble(longitude), Double.parseDouble(latitude)), in.toJSONString());
                        appClaimTeacher.setCurrentPosition(in.toJSONString());
                    } else {
                        logger.error("执行定时添加商户任务，品牌为空 id={}", appClaimTeacher.getId());
                        continue;
                    }
                }
                //修改数据
                appClaimTeacher.setIsAdd(1);
                appClaimTeacherService.update(appClaimTeacher);
            }
        }
        logger.info("执行定时添加商户任务，添加成功");
    }


    //    每周星期天凌晨1点实行一次："0 0 1 ? * L"

    //查询到所有的理赔老师，查询对应数据，不满足就降低
    //每周天晚上11点执行的降低任务
    @Scheduled(cron = "0 0 23 ? * 1")
    @Transactional(readOnly = false)
    public void lowerLevel() {
        //查询到所有理赔老师
        List<AppClaimTeacher> appClaimTeachers = appClaimTeacherService.findAllClaimTeacher();
        //判断所有理赔老师的  接单率，签到率，完单率，成交率，产值
        for (AppClaimTeacher appClaimTeacher : appClaimTeachers) {
            //查询接单率
            //接单数量
            Integer userId = appClaimTeacher.getUserId();
            Integer goMess = appClaimTeacherService.findGoMess(userId);
            //所有线索
            Integer allMessCount = appClaimTeacherService.findAllMessCount(userId);
            //接单率
            if (allMessCount == 0 || goMess == 0) {
                updateLevel(appClaimTeacher);
                continue;
            }
            BigDecimal orderRatio = new BigDecimal(goMess.toString()).divide(new BigDecimal(allMessCount.toString()), 2, BigDecimal.ROUND_HALF_UP);
            if(orderRatio.compareTo(new BigDecimal("0.95")) == -1){
                updateLevel(appClaimTeacher);
                continue;
            }
            //签到率大于95
            //签到数量
            Integer checkMess = appClaimTeacherService.findCheckMessage(userId);
            BigDecimal checkRatio = new BigDecimal(checkMess.toString()).divide(new BigDecimal(goMess.toString()), 2, BigDecimal.ROUND_HALF_UP);
            if(checkRatio.compareTo(new BigDecimal("0.95")) == -1){
                updateLevel(appClaimTeacher);
                continue;
            }
            //完单率大于95%
            Integer findMess = appClaimTeacherService.findFinshMess(userId);
            BigDecimal completeRatio = new BigDecimal(findMess.toString()).divide(new BigDecimal(goMess.toString()), 2, BigDecimal.ROUND_HALF_UP);
            if(completeRatio.compareTo(new BigDecimal("0.95")) == -1){
                updateLevel(appClaimTeacher);
                continue;
            }

            //成交率大于30%   成交台词/接单数量
            Integer dealCount = appClaimTeacherService.findDealCount(userId);
            BigDecimal closing = new BigDecimal(dealCount.toString()).divide(new BigDecimal(goMess.toString()), 2, BigDecimal.ROUND_HALF_UP);
            if(closing.compareTo(new BigDecimal("0.3")) == -1){
                updateLevel(appClaimTeacher);
//                continue;
            }
            //成交产值 查询这个月有没有超过30w
//            BigDecimal dealOutput = appClaimTeacherService.findDealOutput(userId);
//            if(dealOutput.compareTo(new BigDecimal("300000")) == -1){
//                updateLevel(appClaimTeacher);
//            }
        }

    }

    //每个月增加一次等级全部满足才加等级，并且查询产值是否掉级
//    每月最后一天23点执行一次："0 0 23 L * ?"
    @Scheduled(cron = "0 0 23 28-31 * ?")
    @Transactional(readOnly = false)
    public void upLeve() {
        final Calendar c = Calendar.getInstance();
        if (c.get(Calendar.DATE) == c.getActualMaximum(Calendar.DATE)) {
            //是最后一天
            //查询到所有理赔老师
            List<AppClaimTeacher> appClaimTeachers = appClaimTeacherService.findAllClaimTeacher();

            for (AppClaimTeacher appClaimTeacher : appClaimTeachers) {
                //查询三十天数据
                //查询接单率
                //接单数量
                Integer userId = appClaimTeacher.getUserId();
                Integer goMess = appClaimTeacherService.findGoMessMonth(userId);
                //所有线索
                Integer allMessCount = appClaimTeacherService.findAllMessCountMonth(userId);
                //接单率
                if (allMessCount == 0 || goMess == 0) {
                    continue;
                }
                BigDecimal orderRatio = new BigDecimal(goMess.toString()).divide(new BigDecimal(allMessCount.toString()), 2, BigDecimal.ROUND_HALF_UP);
                if(orderRatio.compareTo(new BigDecimal("0.95")) == -1){
                    continue;
                }
                //签到率大于95
                //签到数量
                Integer checkMess = appClaimTeacherService.findCheckMessageMonth(userId);
                BigDecimal checkRatio = new BigDecimal(checkMess.toString()).divide(new BigDecimal(goMess.toString()), 2, BigDecimal.ROUND_HALF_UP);
                if(checkRatio.compareTo(new BigDecimal("0.95")) == -1){
                    continue;
                }
                //完单率大于95%
                Integer findMess = appClaimTeacherService.findFinshMessMonth(userId);
                BigDecimal completeRatio = new BigDecimal(findMess.toString()).divide(new BigDecimal(goMess.toString()), 2, BigDecimal.ROUND_HALF_UP);
                if(completeRatio.compareTo(new BigDecimal("0.95")) == -1){  //小于
                    continue;
                }

                //成交率大于30%   成交台词/接单数量
                Integer dealCount = appClaimTeacherService.findDealCountMonth(userId);
                BigDecimal closing = new BigDecimal(dealCount.toString()).divide(new BigDecimal(goMess.toString()), 2, BigDecimal.ROUND_HALF_UP);
                if(closing.compareTo(new BigDecimal("0.3")) == -1){
                    continue;
                }

                //成交产值 查询这个月有没有超过30w
                BigDecimal dealOutput = appClaimTeacherService.findDealOutput(userId);
                if(dealOutput.compareTo(new BigDecimal("300000")) == -1){
                    continue;
                }
                String level = appClaimTeacher.getLevel();
                if (level.equals("D")) {
                    appClaimTeacher.setLevel("C");
                } else if (level.equals("C")) {
                    appClaimTeacher.setLevel("B");
                }
                appClaimTeacherService.update(appClaimTeacher);

            }
        }
    }


    private void updateLevel(AppClaimTeacher appClaimTeacher){
        String level = appClaimTeacher.getLevel();
        if (level.equals("B")) {
            appClaimTeacher.setLevel("C");
        } else if (level.equals("C")) {
            appClaimTeacher.setLevel("D");
        }
        appClaimTeacherService.update(appClaimTeacher);
    }

    //给 商户up_merchants 注册环信账号
    //执行一次
    //每月1号凌晨1点执行一次："0 0 1 1 * ?"
//    @Scheduled(cron = "30 50 9 * * ?")
//    @Scheduled(fixedDelay = 1000 * 30)
//    @Transactional(readOnly = false)
//    public void addMerchantsHuanxin() {
//        logger.info("商户up_merchants 注册环信账号");
//        //添加huanxin
//        //查询huanxinid为空的上架商户
//        List<AppUpMerchants> appUpMerchantsList = appUpMerchantsService.findNoHuanxin();
//        //注册环信
//        for (AppUpMerchants appUpMerchants : appUpMerchantsList) {
//            double rand = Math.random();
////将随机数转换为字符串
//            String str = String.valueOf(rand).replace("0.", "");
////截取字符串
//            String newStr = str.substring(0, 11);
//
//            //userService,registerHuanxin()
//            appUpMerchantsService.registerHuanxin(appUpMerchants,newStr);
//        }
//
//    }

}

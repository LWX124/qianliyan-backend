package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cheji.web.constant.ConsEnum;
import com.cheji.web.constant.RedisConstant;
import com.cheji.web.modular.cwork.UserPlus;
import com.cheji.web.modular.domain.AppUserPlusEntity;
import com.cheji.web.modular.domain.UserEntity;
import com.cheji.web.modular.mapper.AppUserAccountRecordMapper;
import com.cheji.web.modular.mapper.AppUserPlusMapper;
import com.cheji.web.util.ShareCodeUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class AppUserPlusService {

    @Resource
    private AppUserPlusMapper appUserPlusMapper;

    @Resource
    private AppWxService appWxService;


    @Resource
    private UserService userService;

    @Resource
    private PayAmountRecordService payAmountRecordService;

    @Resource
    private SettlePlusService settlePlusService;

    @Resource
    private UserPlusRoyaltyService userPlusRoyaltyService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private AppUserAccountRecordMapper appUserAccountRecordMapper;

    @Transactional
    public void insetPlus(String outTradeNo, int userId) {
        AppUserPlusEntity appUserPlusEntityParamer = new AppUserPlusEntity();
        appUserPlusEntityParamer.setUserId(userId);
        AppUserPlusEntity appUserPlusEntity = appUserPlusMapper.selectOne(appUserPlusEntityParamer);

        //计算一年后的今天
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        Date time = calendar.getTime();

        if (appUserPlusEntity != null) {
            //修改过期时间
            appUserPlusEntity.setInvalidTimeStart(new Date());
            appUserPlusEntity.setInvalidTimeEnd(time);
            appUserPlusMapper.updateById(appUserPlusEntity);
        } else {
            //为用户开通plus会员
            AppUserPlusEntity appUserPlus = new AppUserPlusEntity();
            appUserPlus.setCreateTime(new Date());
            appUserPlus.setUserId(userId);
            appUserPlus.setInvalidTimeStart(new Date());
            appUserPlus.setInvalidTimeEnd(time);//设置过期时间
            appUserPlus.setInviteCode(ShareCodeUtil.toSerialCode(userId));

            appUserPlusMapper.insert(appUserPlus);
        }

        //修改订单状态
        appWxService.updateOrderStatus(outTradeNo, ConsEnum.WX_PAY_ORDER_SUCCESS.getCode());
    }


    public UserPlus findMes(AppUserPlusEntity userPlusEntity) {
        Integer id = userPlusEntity.getUserId();
        //查询到用户信息
        UserEntity userEntity = userService.selectById(id);
        UserPlus userPlus = new UserPlus();
        userPlus.setName(userEntity.getName());
        userPlus.setAvatar(userEntity.getAvatar());
        //获取到plus会员等级和邀请码
        userPlus.setInviteCode(userPlusEntity.getInviteCode());
        EntityWrapper<UserEntity> userWrapper = new EntityWrapper<>();
        userWrapper.eq("parent_id", id);
        List<UserEntity> userEntities = userService.selectList(userWrapper);
        int i = userEntities.size();

        //得到团队人数
//        if (i <= 300) {
//            userPlus.setUserLevel("初遇车己");
//        } else if (i <= 800) {
//            userPlus.setUserLevel("理赔顾问");
//        } else if (i <= 1500) {
//            userPlus.setUserLevel("理赔经理");
//        } else if (i <= 2200) {
//            userPlus.setUserLevel("区域经理");
//        } else {
//            userPlus.setUserLevel("城市经理");
//        }

        if (i <= 10) {
            userPlus.setUserLevel("热心群众");
        } else if (i <= 50) {
            userPlus.setUserLevel("初遇车己");
        } else {
            userPlus.setUserLevel("车己达人");
        }

        String levelRe = "基础奖励5元;升级成为初遇车己,每条奖励6元;升级成为车己达人,每条奖励8元";
        userPlus.setLevelReward(levelRe);

        //查询账户信息
        //今日奖励从三个方面来查询
        //查询到订单数据
        //查询plus会员的邀请数据
        //查询今日的红包数据和订单数据，还有plus会员邀请数据
        BigDecimal todayPayAmount = payAmountRecordService.findtodayPayAmount(id);
        BigDecimal todaySettle = settlePlusService.findtodaySettle(id);
        BigDecimal todayRoyalty = userPlusRoyaltyService.findtodayRoyalty(id);
        userPlus.setTodayReward(todayPayAmount.add(todaySettle).add(todayRoyalty));
        //本月
        BigDecimal monthPayAmount = payAmountRecordService.findMonthPayAMount(id);
        BigDecimal MonthSettle = settlePlusService.findMonthSettle(id);
        BigDecimal monthRoyalty = userPlusRoyaltyService.findMonthRoyalty(id);
        userPlus.setMonthReward(monthPayAmount.add(MonthSettle).add(monthRoyalty));
        //所有

        userPlus.setBalance(userEntity.getBalance());
        userPlus.setInvitationPeople(i);

        //plus会员原价
        String s = stringRedisTemplate.opsForValue().get(RedisConstant.PLUS_USER_ORIGINAL_PRICE);
        userPlus.setOriginalPrice(s);
        //plus会员优惠后价
        String s1 = stringRedisTemplate.opsForValue().get(RedisConstant.PLUS_USER_PREFERENTIAL_PRICE);
        userPlus.setPreferentialPrice(s1);
        //plus会员营销代理权
        String s2 = stringRedisTemplate.opsForValue().get(RedisConstant.PLUS_USER_MARKETING_AGENCY);
        userPlus.setMarketingAgency(s2);
        //plus会员养车专区
        String s3 = stringRedisTemplate.opsForValue().get(RedisConstant.PLUS_USER_MAINTAIN_ZONE);
        userPlus.setMaintainZone(s3);


        //查询到事故奖励总金额，查询到推广奖励
        //先查询事故奖励总金额。发红包的奖励
        BigDecimal allAccidentReward = payAmountRecordService.findPayAmount(id);
        userPlus.setAccidReward(allAccidentReward);

        //查询到推广奖励
        BigDecimal pushReward = appUserAccountRecordMapper.findAllPushReward(id);
        userPlus.setPromtionReward(pushReward);

        userPlus.setAllReward(allAccidentReward.add(pushReward));

        userPlus.setWarmPrompt("车己等级由车己事故和粉丝数量决定，对应关系如下：\n" +
                "热心群众：基本等级；\n" +
                "初遇车己：自身通过10条视频审核，粉丝数达到5人并且粉丝通过2条视频审核；\n" +
                "车己达人：自身通过50条视频审核，粉丝数达到15人并且粉丝通过2条视频审核；\n" +
                "只要邀请的粉丝通过2条视频审核就可以获得10元现金奖励哦！\n");

        return userPlus;
    }

    public AppUserPlusEntity findPlusUserByid(String id) {
        return appUserPlusMapper.findOlusUserByid(id);
    }

    public UserPlus findOtherMes() {
        UserPlus userPlus = new UserPlus();
        String s = stringRedisTemplate.opsForValue().get(RedisConstant.PLUS_USER_ORIGINAL_PRICE);
        userPlus.setOriginalPrice(s);
        //plus会员优惠后价
        String s1 = stringRedisTemplate.opsForValue().get(RedisConstant.PLUS_USER_PREFERENTIAL_PRICE);
        userPlus.setPreferentialPrice(s1);
        //plus会员营销代理权
        String s2 = stringRedisTemplate.opsForValue().get(RedisConstant.PLUS_USER_MARKETING_AGENCY);
        userPlus.setMarketingAgency(s2);
        //plus会员养车专区
        String s3 = stringRedisTemplate.opsForValue().get(RedisConstant.PLUS_USER_MAINTAIN_ZONE);
        userPlus.setMaintainZone(s3);
        //温馨提示
        String s4 = stringRedisTemplate.opsForValue().get(RedisConstant.PLUS_USER_WARM_PROMPT);
        userPlus.setWarmPrompt(s4);
        return userPlus;
    }
}

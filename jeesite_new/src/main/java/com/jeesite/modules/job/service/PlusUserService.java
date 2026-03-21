package com.jeesite.modules.job.service;

import com.jeesite.modules.app.dao.AppUserAccountRecordDao;
import com.jeesite.modules.app.dao.AppUserDao;
import com.jeesite.modules.app.dao.AppUserPlusRoyaltyDao;
import com.jeesite.modules.app.dao.AppWxpayOrderDao;
import com.jeesite.modules.app.entity.AppUser;
import com.jeesite.modules.app.entity.AppUserAccountRecord;
import com.jeesite.modules.app.entity.AppUserPlusRoyalty;
import com.jeesite.modules.constant2.AppConstants;
import com.jeesite.modules.constant2.ConsEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Service
public class PlusUserService {

    private final static Logger logger = LoggerFactory.getLogger(PlusUserService.class);

    @Resource
    private AppUserDao appUserDao;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private AppWxpayOrderDao appWxpayOrderDao;

    @Resource
    private AppUserPlusRoyaltyDao appUserPlusRoyaltyDao;

    @Resource
    private AppUserAccountRecordDao appUserAccountRecordDao;

    @Transactional
    public void addPlusAmount(Map map) {
        Object parent_id = map.get("parent_id");//找到邀请人进行分成
        Integer parentId = Integer.parseInt(String.valueOf(parent_id));

        //获取邀请开通plus会员 分成金额
        Object o = stringRedisTemplate.opsForValue().get(AppConstants.PLUS_MEMBER_AMOUNT);

        //修改订单处理状态
        appWxpayOrderDao.updateBusinessStatus(String.valueOf(map.get("out_trade_no")), ConsEnum.WX_PAY_ORDER_BUSINESS_STATUS_DEFAULT.getCode());

        if (parentId == null || parentId == 0) {
            return;//不用分成
        }

        //增加记录
        AppUserPlusRoyalty appUserPlusRoyalty = new AppUserPlusRoyalty();
        appUserPlusRoyalty.setAmount(Integer.parseInt(String.valueOf(o)));
        appUserPlusRoyalty.setRoyaltyUserId(Long.parseLong(String.valueOf(parent_id)));
        appUserPlusRoyalty.setPlusUserId(Long.parseLong(String.valueOf(map.get("user_id"))));
        appUserPlusRoyalty.setCreateTime(new Date());
        appUserPlusRoyaltyDao.insert(appUserPlusRoyalty);

        //为邀请人增余额
        AppUser appUser = appUserDao.selectFotUpdate(parentId);
        if (appUser == null) {
            logger.error("### plus会员定时任务###  appUser为空  map={}", map);
            return;
        }
        appUser.setBalance(appUser.getBalance().add(new BigDecimal(String.valueOf(o))));
        appUserDao.update(appUser);

        //增加账户加钱记录
        AppUserAccountRecord appUserAccountRecordEntity = new AppUserAccountRecord();
        appUserAccountRecordEntity.setCreateTime(new Date());
        appUserAccountRecordEntity.setAddFlag(1);
        appUserAccountRecordEntity.setMomey(new BigDecimal(String.valueOf(o)));
        appUserAccountRecordEntity.setSource(1);
        appUserAccountRecordEntity.setType(10);
        appUserAccountRecordEntity.setUserId(Long.valueOf(String.valueOf(parentId)));
        appUserAccountRecordEntity.setBusinessId(appUserPlusRoyalty.getId());
        appUserAccountRecordDao.insertCus(appUserAccountRecordEntity);

    }
}

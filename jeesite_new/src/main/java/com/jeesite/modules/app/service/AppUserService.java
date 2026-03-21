/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.dao.AppUserAccountRecordDao;
import com.jeesite.modules.app.dao.AppUserDao;
import com.jeesite.modules.app.entity.*;
import com.jeesite.modules.constant2.RedisKeyUtils;
import com.jeesite.modules.util2.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户信息表Service
 *
 * @author zcq
 * @version 2019-08-08
 */
@Service
@Transactional(readOnly = true)
public class AppUserService extends CrudService<AppUserDao, AppUser> {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private AppUserPlusService appUserPlusService;


    @Resource
    private AppSettlePlusService appSettlePlusService;

    @Resource
    private AppBUserService appBUserService;

    @Resource
    private AppUserDao appUserDao;

    @Resource
    private AppIndentService appIndentService;

    @Resource
    private AppUserAccountRecordDao appUserAccountRecordDao;

    /**
     * 获取单条数据
     *
     * @param appUser
     * @return
     */
    @Override
    public AppUser get(AppUser appUser) {
        return super.get(appUser);
    }

    /**
     * 查询分页数据
     *
     * @param appUser      查询条件
     * @param appUser.page 分页对象
     * @return
     */
    @Override
    public Page<AppUser> findPage(AppUser appUser) {
        return super.findPage(appUser);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param appUser
     */
    @Override
    @Transactional(readOnly = false)
    public void save(AppUser appUser) {
        super.save(appUser);
    }

    /**
     * 更新状态
     *
     * @param appUser
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(AppUser appUser) {
        super.updateStatus(appUser);
    }

    /**
     * 删除数据
     *
     * @param appUser
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(AppUser appUser) {
        super.delete(appUser);
    }

    @Transactional
    public AppUser findUserById(Long userId) {
        return appUserDao.findUserById(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    public AppUser addAmount(Long userId, BigDecimal userAmount, String id, int accountRecordType) {
        //获取用户数据，添加行锁
        AppUser userById = findUserById(userId);
        //添加用户余额
        BigDecimal balance = userById.getBalance();
        balance = balance.add(userAmount);
        userById.setBalance(balance);
        update(userById);

        AppUserAccountRecord appUser = new AppUserAccountRecord();
		/*private Integer momey;		// 金额
			private Long userId;		// 用户id
			private Integer type;		// 操作类型 1：提现  2：提现失败回滚 3:发送红包 4:订单结算
			private Date createTime;		// 发生时间
			private Integer addFlag;		// 1加钱  2 减钱
			private Integer source;		// 1:c端  2：b端
			private Long bussinessId;		// 业务id*/
        appUser.setMomey(userAmount);
        appUser.setUserId(userId);
        appUser.setType(accountRecordType);
        appUser.setCreateTime(new Date());
        appUser.setAddFlag(1);
        appUser.setSource(1);
        appUser.setBusinessId(id);
        appUserAccountRecordDao.insertCus(appUser);
        return userById;
    }

    @Transactional(rollbackFor = Exception.class)                 //减去一级得比例，还需要减去平台
    public void addAmount4User(Integer userId, BigDecimal userAmount ,String id, AppIndent appIndent) {
        //没得父id，直接给用户添加数据
        this.addAmount(Long.valueOf(userId), userAmount, id, 4);
        //修改订单状态
        appIndent.setState(6);
        appIndent.setSendBack(1);
        appIndent.setUpdateTime(new Date());
        //修改商户余额，新增收入数据
        appBUserService.updateBalance(appIndent);
//        appIndent.setFixlossUser(userAmount);
       // appIndent.setBookValue(userAmount);
        appIndentService.update(appIndent);
        //记录商户订单扣钱
        AppUserAccountRecord appUser = new AppUserAccountRecord();
        appUser.setMomey(userAmount);
        appUser.setUserId(Long.valueOf(appIndent.getUserBId()));
        appUser.setType(6);
        appUser.setCreateTime(new Date());
        appUser.setAddFlag(2);
        appUser.setSource(2);
        appUser.setBusinessId(id);
        appUserAccountRecordDao.insertCus(appUser);

    }

    @Transactional(rollbackFor = Exception.class)
    public void addAmount4Parent(Integer userId, BigDecimal userAmount,
                                 Integer parentId, String id, AppIndent appIndent) {
        //从redis获取到金额数据里面的数据
        //获取一级的金额比例    1%
        String first = (String) redisTemplate.opsForValue().get(RedisKeyUtils.AMOUNT_FIRST_AMOUNT_PROPORTION);
        BigDecimal bigDecimal = new BigDecimal(first);
        BigDecimal firstProp = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        //通过plus会员id，找到plus会员表，看时间是否过期
        AppUserPlus appUserPlus = appUserPlusService.findPlusUserByUserId(parentId);
        Date invalidTimeEnd = appUserPlus.getInvalidTimeEnd();
        //plus会员维修金额得百分之一
        BigDecimal amount = appIndent.getSettleAccounts().multiply(firstProp);
        Date date = new Date();

        if (invalidTimeEnd.before(date)) {
            //过期了
            AppSettlePlus appSettlePlus1 = new AppSettlePlus();
            appSettlePlus1.setSettlementAmount(appIndent.getSettleAccounts());
            appSettlePlus1.setProprotion(firstProp);
            appSettlePlus1.setSettlePlusAmount(amount);
            appSettlePlus1.setPlusId(Long.valueOf(parentId));
            appSettlePlus1.setIndentId(Long.valueOf(id));
            appSettlePlus1.setInform("fail");
            appSettlePlus1.setCreateTime(date);
            appSettlePlus1.setUpdateTime(date);
            appSettlePlusService.insert(appSettlePlus1);
        } else {
            AppSettlePlus appSettlePlus1 = new AppSettlePlus();
            appSettlePlus1.setSettlementAmount(appIndent.getSettleAccounts());
            appSettlePlus1.setProprotion(firstProp);
            appSettlePlus1.setSettlePlusAmount(amount);
            appSettlePlus1.setPlusId(Long.valueOf(parentId));
            appSettlePlus1.setIndentId(Long.valueOf(id));
            appSettlePlus1.setInform("success");
            appSettlePlus1.setCreateTime(date);
            appSettlePlus1.setUpdateTime(date);
            appSettlePlusService.insert(appSettlePlus1);
            //给父id发钱
            //获取到给上一级plus会员的金额
            this.addAmount(Long.valueOf(parentId), amount, id, 7);
            //判断上一级的plus会员还有没上一级
            //获取到plus会员user信息
            AppUser secondPlusUser = this.get(String.valueOf(parentId));
            Integer parentId2 = secondPlusUser.getParentId();
            //二级发钱开关
//            String str = (String) redisTemplate.opsForValue().get(RedisKeyUtils.MERCHANTS_PAY_SWITCH);
//            if (StringUtils.isEmpty(str)){
//                str="0";
//            }
//            if (str.equals("1")) {
//                //不为空就是有，就判断然后发钱
//                if (parentId2 != 0) {
//                    //获取到id然后判断plus会员过期没有
//                    AppUserPlus secondplusUser = appUserPlusService.findPlusUserByUserId(parentId2);
//                    String second = (String) redisTemplate.opsForValue().get(RedisKeyUtils.AMOUNT_SECOND_AMOUNT_PROPORTION);
//                    BigDecimal bigDecimal2 = new BigDecimal(second);
//                    BigDecimal secondProp = bigDecimal2.setScale(2, BigDecimal.ROUND_HALF_UP);
//                    //从redis获取到金额数据里面的数据
//                    //获取2级的金额比例
//                    //结算二级金额
//                    BigDecimal secondAmount = amount.multiply(secondProp);
//                    //获取到结束时间
//                    Date invalidTimeEnd1 = secondplusUser.getInvalidTimeEnd();
//                    if (invalidTimeEnd1.before(date)) {
//                        //已过期
//                        AppSettlePlus appSettlePlus2 = new AppSettlePlus();
//                        appSettlePlus2.setSettlementAmount(userAmount);
//                        appSettlePlus2.setProprotion(bigDecimal2);
//                        appSettlePlus2.setSettlePlusAmount(secondAmount);
//                        appSettlePlus2.setPlusId(Long.valueOf(parentId2));
//                        appSettlePlus2.setIndentId(Long.valueOf(id));
//                        appSettlePlus2.setInform("fail");
//                        appSettlePlus2.setCreateTime(date);
//                        appSettlePlus2.setUpdateTime(date);
//                        appSettlePlusService.insert(appSettlePlus2);
//                    } else {
//                        AppSettlePlus appSettlePlus2 = new AppSettlePlus();
//                        appSettlePlus2.setSettlementAmount(userAmount);
//                        appSettlePlus2.setProprotion(bigDecimal2);
//                        appSettlePlus2.setSettlePlusAmount(secondAmount);
//                        appSettlePlus2.setPlusId(Long.valueOf(parentId2));
//                        appSettlePlus2.setIndentId(Long.valueOf(id));
//                        appSettlePlus2.setInform("success");
//                        appSettlePlus2.setCreateTime(date);
//                        appSettlePlus2.setUpdateTime(date);
//                        appSettlePlusService.insert(appSettlePlus2);
//                        //给2级提成
//                        this.addAmount(Long.valueOf(parentId2), secondAmount, id, 8);
//                    }
//                }
//            }
        }
            //给用户发钱，用户金额
        this.addAmount4User(userId, userAmount, id, appIndent);
    }

    public void updateBlackName() {
        appUserDao.updateBlackName();
    }
}
/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.dao.AppBUserDao;
import com.jeesite.modules.app.dao.AppPushBillDao;
import com.jeesite.modules.app.dao.AppUserAccountRecordDao;
import com.jeesite.modules.app.dao.AppUserDao;
import com.jeesite.modules.app.entity.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 用户信息表Service
 *
 * @author zcq
 * @version 2019-10-16
 */
@Service
@Transactional(readOnly = true)
public class AppBUserService extends CrudService<AppBUserDao, AppBUser> {

    @Resource
    private AppBUserDao appBUserDao;

    @Resource
    private AppPushBillDao appPushBillDao;

    @Resource
    private AppUserDao appUserDao;

    @Resource
    private AppUserAccountRecordDao appUserAccountRecordDao;

    /**
     * 获取单条数据
     *
     * @param appBUser
     * @return
     */
    @Override
    public AppBUser get(AppBUser appBUser) {
        return super.get(appBUser);
    }

    /**
     * 查询分页数据
     *
     * @param appBUser      查询条件
     * @return
     */
    @Override
    public Page<AppBUser> findPage(AppBUser appBUser) {
        return super.findPage(appBUser);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param appBUser
     */
    @Override
    @Transactional(readOnly = false)
    public void save(AppBUser appBUser) {
        super.save(appBUser);
    }

    /**
     * 更新状态
     *
     * @param appBUser
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(AppBUser appBUser) {
        super.updateStatus(appBUser);
    }

    /**
     * 删除数据
     *
     * @param appBUser
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(AppBUser appBUser) {
        super.delete(appBUser);
    }

    public List<String> findBrand(String id) {
        return appBUserDao.findBrand(id);
    }

    @Transactional(readOnly = false)
    public void langlock(Integer id, String amount, String accId, String userId, Integer type, String brand) {
//        AppBUser appBUser = appBUserDao.updateBalance(id);
        AppUser appUser = appUserDao.findUserById(id.longValue());
        BigDecimal balance = appUser.getBalance();
//        BigDecimal balance = appBUser.getBalance();
        appUser.setBalance(balance.subtract(new BigDecimal(amount)));
//        appBUser.setBalance(balance.subtract(new BigDecimal(amount)));
        appUserDao.update(appUser);

        //新增账单
        AppPushBill appPushBill = new AppPushBill();
        appPushBill.setAccid(Long.valueOf(accId));
        appPushBill.setDeduction(Double.valueOf(amount));
        appPushBill.setUserId(Long.valueOf(userId));
        appPushBill.setCreateTime(new Date());
        appPushBill.setUpdateTime(new Date());
        appPushBill.setIsClaim(1);
//        if (brand.equals("7")){
//            appPushBill.setIsClaim(1);
//        }else {
//            appPushBill.setIsClaim(0);
//        }

        if (type == 3) {
            appPushBill.setSource(2);
        } else {
            appPushBill.setSource(1);
        }
        appPushBillDao.insert(appPushBill);

        AppUserAccountRecord appUserAccountRecord = new AppUserAccountRecord();
		/*private Integer momey;		// 金额
			private Long userId;		// 用户id
			private Integer type;		// 操作类型 1：提现  2：提现失败回滚 3:发送红包 4:订单结算
			private Date createTime;		// 发生时间
			private Integer addFlag;		// 1加钱  2 减钱
			private Integer source;		// 1:c端  2：b端
			private Long bussinessId;		// 业务id*/
        appUserAccountRecord.setMomey(new BigDecimal(amount));
        appUserAccountRecord.setUserId(id.longValue());
        appUserAccountRecord.setType(20);           //c端信息扣费
        appUserAccountRecord.setCreateTime(new Date());
        appUserAccountRecord.setAddFlag(2);
        appUserAccountRecord.setSource(1);
        appUserAccountRecord.setBusinessId(accId);
        appUserAccountRecordDao.insertCus(appUserAccountRecord);

    }

    @Transactional
    public void updateBalance(AppIndent appIndent) {
        //修改商户余额，新增收入数据
        //上行锁
        AppBUser appBUser = appBUserDao.updateBalance(appIndent.getUserBId());
        //修改数据
        BigDecimal balance = appBUser.getBalance();
        appBUser.setBalance(balance.subtract(appIndent.getFixlossUser()));
//        appBUser.setBalance(balance.subtract(appIndent.getSettleAccounts().multiply(appIndent.getCommissionRate())));
        appBUserDao.update(appBUser);
        //添加新得数据
		/*  private Long indentId;		// 订单id
			private Long userBId;		// 商户id
			private Long userId;		// C用户id
			private Double rebates;		// 比例
			private Double fixlossUser;		// 结算给用户金额
			private Double amount;		// 金额
			private String orderNumber;		// 订单号
			private Date createTime;		// create_time
			private Date updateTime;		// update_time*/
		/*AppOurIndentAmount appOurIndentAmount = new AppOurIndentAmount();
		appOurIndentAmount.setIndentId(Long.valueOf(appIndent.getId()));
		appOurIndentAmount.setUserBId(Long.valueOf(appIndent.getUserBId()));
		appOurIndentAmount.setUserId(Long.valueOf(appIndent.getUserId()));
		appOurIndentAmount.setRebates(appIndent.getCommissionRate());
		appOurIndentAmount.setFixlossUser(appIndent.getFixlossUser());
		appOurIndentAmount.setAmount(fixamount.subtract(appIndent.getFixlossUser()));
		appOurIndentAmount.setOrderNumber(appIndent.getOrderNumber());
		appOurIndentAmount.setCreateTime(new Date());
		appOurIndentAmount.setUpdateTime(new Date());
        appOurIndentAmountService.insert(appOurIndentAmount);*/

    }

    public List<Integer> findAllMerchants() {
        return appBUserDao.findAllMerchants();
    }

    public List<String> findUpMerchants() {
        return appBUserDao.findUpMerchants();
    }

    public List<AppBUser> findIsCompany() {
        return appBUserDao.findIsCompany();
    }

    public AppBUser findBByPhone(String phone) {
        return appBUserDao.findBByPhone(phone);
    }

    public Integer selectBUserMessage(Integer id) {
        return appBUserDao.selectBUserMessage(id);
    }

    public String findNewUrl(String uploadId) {
        return appBUserDao.findNewUrl(uploadId);
    }

    public List<String> findNewUrl1(String uploadId) {
        return appBUserDao.findNewUrl1(uploadId);
    }

    public Integer findShelvesCount() {
        return appBUserDao.findShelvesCount();
    }

    public Integer findOutCount() {
        return appBUserDao.findOutCount();
    }

    public Integer findAllCount() {
        return appBUserDao.findAllCount();
    }

    public List<String> findMerchantsInsert(String id) {
        return appBUserDao.findMerchantsInsert(id);
    }


    public AppIndent findNumberAndMoney(String id) {
        return appBUserDao.findNumberAndMoney(id);
    }

    public String getCity(String id) {
        return appBUserDao.getCity(id);
    }

    public AppBUser finUserByUpId(String id1) {
        return appBUserDao.finUserByUpId(id1);
    }

    public List<String> findImgsByAuctionId(Long upId) {
        return appBUserDao.findImgsByAuctionId(upId);
    }
}
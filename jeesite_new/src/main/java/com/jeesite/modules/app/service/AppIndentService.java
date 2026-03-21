/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.dao.AppIndentDao;
import com.jeesite.modules.app.entity.AppExcel;
import com.jeesite.modules.app.entity.AppIndent;
import com.jeesite.modules.app.entity.AppLeague;
import com.jeesite.modules.app.entity.ClaimsTeacher;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单信息表Service
 *
 * @author zcq
 * @version 2019-08-05
 */
@Service
@Transactional(readOnly = true)
public class AppIndentService extends CrudService<AppIndentDao, AppIndent> {
    @Autowired
    private AppIndentDao appIndentDao;

    /**
     * 获取单条数据
     *
     * @param appIndent
     * @return
     */
    @Override
    public AppIndent get(AppIndent appIndent) {
        return super.get(appIndent);
    }

    /**
     * 查询分页数据
     *
     * @param appIndent      查询条件
     * @param appIndent.page 分页对象
     * @return
     */
    @Override
    public Page<AppIndent> findPage(AppIndent appIndent) {
        Page<AppIndent> page = super.findPage(appIndent);
        return page;
    }


    public Page<AppIndent> findprofessionalPage(AppIndent appIndent) {
        Page a = appIndent.getPage();
        int pageNo = a.getPageNo();//页数
        int pageSize = a.getPageSize();//条数
        int pageoffset = (pageNo - 1) * pageSize;
        long count = a.getCount();
        if (StringUtils.isNotEmpty(appIndent.getUsername())||StringUtils.isNotEmpty(appIndent.getPlan())){
            String username = appIndent.getUsername();
            String plan = appIndent.getPlan();
            List<AppIndent> workList = appIndentDao.findProfessionalList(pageoffset,username,plan);
            count = appIndentDao.findProfessionalListCount(username,plan);
            a.setList(workList);
            a.setCount(count);
        }else {
            a.setList(null);
            a.setCount(0);
        }
        return a;
    }


    /**
     * 保存数据（插入或更新）
     *
     * @param appIndent
     */
    @Override
    @Transactional(readOnly = false)
    public void save(AppIndent appIndent) {
        super.save(appIndent);
    }

    /**
     * 更新状态
     *
     * @param appIndent
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(AppIndent appIndent) {
        super.updateStatus(appIndent);
    }

    /**
     * 删除数据
     *
     * @param appIndent
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(AppIndent appIndent) {
        super.delete(appIndent);
    }


    public JSONObject countData() {
        JSONObject object = new JSONObject();
        //今日开单
        object.put("openOrder", appIndentDao.selectCountOpenOrder(1));
        //今日已接车
        object.put("receipt", appIndentDao.selectCountOpenOrder(3));
        Object o = appIndentDao.selectCountMoney();
        if (o == null) {
            o = 0;
        }
        //今日金额
        object.put("money", o);
        //今日交车
        object.put("crosstown", appIndentDao.selectCountOpenOrder(4));
        //今日结算
        object.put("settlement", appIndentDao.selectCountOpenOrder(5));

        return object;
    }

    public List<String> selectJpush() {
        return appIndentDao.selectJpush();
    }

    public List<AppExcel> findExcelMesg( String beginTime, String lastTime, String dealTime, String messageSource, String sendPeople, String sendUnit, String state,String account,String sendBack,String orderNumber,String licensePlate, Integer userBId,String lastUpdateTime,String beginUpdateTime) {
        return appIndentDao.findExcelMesg(beginTime,lastTime,dealTime,messageSource,sendPeople,sendUnit,state,account,sendBack,orderNumber,licensePlate,userBId,lastUpdateTime,beginUpdateTime);
    }

    public List<AppLeague> findClaimsTeacher() {
        return appIndentDao.findClaimsTeacher();
    }

    public List<String> findIndentImgs(String indentId) {
        return appIndentDao.findIndentImgs(indentId);
    }

    public BigDecimal findAllFix(String username, String plan) {
        return appIndentDao.findAllFix(username,plan);
    }

    public Integer findinewIndentNumber() {
        return appIndentDao.findNewIndetNumber();
    }

    public Integer findInServiceNumer() {
        return appIndentDao.findInServiceNumber();
    }

    public Integer findforwardNumber() {
        return appIndentDao.findforwardNumber();
    }

    public Integer findsettleNumer() {
        return appIndentDao.findsettleNumber();
    }

    public Integer findfinishNumber() {
        return appIndentDao.findfinishNumber();
    }

    public BigDecimal findEstimatedAmountINew() {
        return appIndentDao.findEstimatedAmountINew();
    }

    public BigDecimal findEstimatedAmountInSer() {
        return appIndentDao.findEstimatedAmountInSer();
    }

    public BigDecimal findAllSettleAmount() {
        return appIndentDao.findAllSettleAmount();
    }

    public List<AppIndent> findAllNoColleatIndent() {
        return appIndentDao.findAllNoColleatIndent();
    }

    public Integer findThisMonCount(String id1) {
        return appIndentDao.findThisMonCount(id1);
    }

    public BigDecimal findThisMonAmount(String id1) {
        return appIndentDao.findThisMonAmount(id1);
    }

    public Integer findByState(String upId, String userBId, int i) {
        return appIndentDao.findByState(upId,userBId,i);
    }

    public BigDecimal findByFixuser(String upId, String userBId, int i) {
        return appIndentDao.findByFixUser(upId,userBId,i);
    }

    public BigDecimal findByUser(String upId, String userBId, int i) {
        return appIndentDao.findByUser(upId,userBId,i);
    }

    public String findMerchantsName(String upId) {
        return appIndentDao.findMerchantsName(upId);
    }

    public String appIndentUsBName(String userBId) {
        return appIndentDao.appIndentUsBName(userBId);
    }

    public Page<AppIndent> findRemake(AppIndent appIndent) {
        Page a = appIndent.getPage();
        int pageNo = a.getPageNo();//页数
        int pageSize = a.getPageSize();//条数
        int pageoffset = (pageNo - 1) * pageSize;

        List<AppIndent> remake = appIndentDao.findRemake(appIndent.getRemake(), pageoffset);

        a.setList(remake);
        return a;
    }


    //根据订单id来修改信息
	/*@Transactional
	public void update(String indentId) {
		//根据id查询到佣金比例和结算金额
		AppIndent appIndent = get(indentId);
			//获取到佣金比例和结算金额
		BigDecimal commissionRate = appIndent.getCommissionRate();
		BigDecimal settleAccounts = appIndent.getSettleAccounts();
		Integer userId = appIndent.getUserId();
		//计算结算到用户金额和用户，添加到订单
		BigDecimal fixlossUser = appIndent.getFixlossUser();
		fixlossUser = commissionRate.multiply(settleAccounts);
		//查询用户信息，根据结算金额添加到余额
		AppUser userById = appUserService.findUserById(Long.valueOf(userId));
		BigDecimal balance = userById.getBalance();
		BigDecimal add1 = balance.add(fixlossUser);
		userById.setBalance(add1);
		//根据用户查询是否有plus会员
		Integer parentId = userById.getParentId();
		//有plus会员就根据金额计算出结算到plus会员金额
		if (null==parentId||0==parentId){
			//没有puls会员就结束
			return;
		}
		//有puls会员就判断添加
		//添加数据到订单表
		appIndent.setPlusUserid(parentId);
		BigDecimal a = BigDecimal.valueOf(0.05);
		//结算到plus会员金额
		BigDecimal plusaccount = fixlossUser.multiply(a);
		appIndent.setPlusAccounts(plusaccount);
		//根据plus会员id查询到用户信息，添加结算到plus会员到金额
		AppUser plusId = appUserService.findUserById(Long.valueOf(parentId));
		BigDecimal balance1 = plusId.getBalance();
		BigDecimal add = balance1.add(plusaccount);
		//修改用户余额
		plusId.setBalance(add);
	}*/
}
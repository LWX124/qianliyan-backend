/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.jeesite.modules.app.dao.AppUserAccountRecordDao;
import com.jeesite.modules.app.entity.AppUser;
import com.jeesite.modules.app.entity.AppUserAccountRecord;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.entity.AppPayAmountRecord;
import com.jeesite.modules.app.dao.AppPayAmountRecordDao;

import javax.annotation.Resource;

/**
 * 红包金额记录表Service
 *
 * @author zcq
 * @version 2019-08-30
 */
@Service
@Transactional(readOnly = true)
public class AppPayAmountRecordService extends CrudService<AppPayAmountRecordDao, AppPayAmountRecord> {

    @Autowired
    private AppUserService appUserService;

    @Resource
    private AppPayAmountRecordDao appPayAmountRecordDao;

    @Resource
    private AppUserAccountRecordDao appUserAccountRecordDao;

    /**
     * 获取单条数据
     *
     * @param appPayAmountRecord
     * @return
     */
    @Override
    public AppPayAmountRecord get(AppPayAmountRecord appPayAmountRecord) {
        return super.get(appPayAmountRecord);
    }

    /**
     * 查询分页数据
     *
     * @param appPayAmountRecord 查询条件
     * @param appPayAmountRecord 分页对象
     * @return
     */
    @Override
    public Page<AppPayAmountRecord> findPage(AppPayAmountRecord appPayAmountRecord) {
        return super.findPage(appPayAmountRecord);
    }

    /**
     * 保存红包支付数据（插入或更新）
     *
     * @param appPayAmountRecord
     */
    @Override
    @Transactional(readOnly = false)
    public void save(AppPayAmountRecord appPayAmountRecord) {
        super.save(appPayAmountRecord);
    }

    /**
     * 更新状态
     *
     * @param appPayAmountRecord
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(AppPayAmountRecord appPayAmountRecord) {
        super.updateStatus(appPayAmountRecord);
    }

    /**
     * 删除数据
     *
     * @param appPayAmountRecord
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(AppPayAmountRecord appPayAmountRecord) {
        super.delete(appPayAmountRecord);
    }


    @Transactional(readOnly = false)
    public void addPayAmount(AppPayAmountRecord appPayAmountRecord, String id, Integer type) {
        //先查询到用户余额，添加行锁
        AppUser user = appUserService.findUserById(appPayAmountRecord.getUserId());
        //添加用户余额
        BigDecimal balance = user.getBalance();
        //添加余额
        user.setBalance(balance.add(BigDecimal.valueOf(appPayAmountRecord.getPayAmount())));
        //保存数据
        appUserService.update(user);
        //给红包表添加数据
        if (type != 19) {
            insert(appPayAmountRecord);
        }
        AppUserAccountRecord appUser = new AppUserAccountRecord();
		/*private Integer momey;		// 金额
			private Long userId;		// 用户id
			private Integer type;		// 操作类型 1：提现  2：提现失败回滚 3:发送红包
			private Date createTime;		// 发生时间
			private Integer addFlag;		// 1加钱  2 减钱
			private Integer source;		// 1:c端  2：b端
			private Long bussinessId;		// 业务id*/
        Double payAmount = appPayAmountRecord.getPayAmount();
        appUser.setMomey(BigDecimal.valueOf(payAmount));
        appUser.setUserId(appPayAmountRecord.getUserId());
        appUser.setType(type);
        appUser.setCreateTime(new Date());
        appUser.setAddFlag(1);
        appUser.setSource(1);
        appUser.setBusinessId(id);
        appUserAccountRecordDao.insertCus(appUser);
    }

    public List<AppPayAmountRecord> findRecord(String id, String userId) {
        return appPayAmountRecordDao.findRecord(id, userId);
    }

    public BigDecimal findAllPayAmount(String id) {
        //查询到app的红包金额
        return appPayAmountRecordDao.findAllPayAmount(id);
    }
}
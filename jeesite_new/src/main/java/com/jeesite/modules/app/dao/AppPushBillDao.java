/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppPushBill;

import java.util.List;

/**
 * 用户扣费记录表DAO接口
 * @author zcq
 * @version 2019-10-24
 */
@MyBatisDao
public interface AppPushBillDao extends CrudDao<AppPushBill> {

    List<AppPushBill> findisPush(String s, String accId);

    List<String> selectNoPush();

    List<Integer> selectPushMerchants(String id, Integer type);

    List<AppPushBill> selectMerchantsPush(String id);

    List<AppPushBill> findpushBill(String accId, String type);

    List<String> findPbImg(String accId,Integer type);

    List<AppPushBill> findBySourceAccid(String id, Integer type);
}
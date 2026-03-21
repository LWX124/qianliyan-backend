/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppInsuranceMerchants;

/**
 * 保险和商户关联表DAO接口
 * @author zcq
 * @version 2020-08-11
 */
@MyBatisDao
public interface AppInsuranceMerchantsDao extends CrudDao<AppInsuranceMerchants> {

    Integer findAgoInsurance(String insId, String userBId);

    void insertInsurance(AppInsuranceMerchants insuranceMerchants);
}
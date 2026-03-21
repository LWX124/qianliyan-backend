/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppWxBankInterface;

import java.util.List;

/**
 * app_wx_bank_interfaceDAO接口
 * @author dh
 * @version 2019-08-30
 */
@MyBatisDao
public interface AppWxBankInterfaceDao extends CrudDao<AppWxBankInterface> {

    List<AppWxBankInterface> selectForFail();

    void updateStatusByTradeNo(String partnerTradeNo);
}
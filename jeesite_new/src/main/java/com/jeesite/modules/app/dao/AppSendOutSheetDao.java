/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppSendOutSheet;
import com.jeesite.modules.app.entity.AppUser;
import com.jeesite.modules.app.entity.EmployeeClaim;

import java.util.List;

/**
 * web派单记录表DAO接口
 * @author zcq
 * @version 2020-09-24
 */
@MyBatisDao
public interface AppSendOutSheetDao extends CrudDao<AppSendOutSheet> {

    List<AppUser> findNowClaimAdjusters();


    Integer addSendSheet(AppSendOutSheet appSendOutSheet);

    Integer findSendUserbid(Integer id);

    String findByLngLat(String lat, String lng);

    List<String> findCheckImg(String indentId);

}
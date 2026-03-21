/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppClaimTeacher;

import java.math.BigDecimal;
import java.util.List;

/**
 * 理赔老师表DAO接口
 * @author zcq
 * @version 2021-05-17
 */
@MyBatisDao
public interface AppClaimTeacherDao extends CrudDao<AppClaimTeacher> {

    List<AppClaimTeacher> findMerchants();


    List<AppClaimTeacher> findClaTeacher();

    List<AppClaimTeacher> findAllClaimTeacher();


    Integer findGoMess(Integer userId);

    Integer findAllMessCount(Integer userId);

    Integer findCheckMessage(Integer userId);

    Integer findFinshMess(Integer userId);

    Integer findDealCount(Integer userId);

    BigDecimal findDealOutPut(Integer userId);


    Integer findGoMessMonth(Integer userId);

    Integer findAllMessCountMonth(Integer userId);

    Integer findCheckMessageMonth(Integer userId);

    Integer findFinshMessMonth(Integer userId);

    Integer findDealCountMonth(Integer userId);

    AppClaimTeacher findClaimByUserId(Integer userId);
}
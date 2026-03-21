/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppExcel;
import com.jeesite.modules.app.entity.AppIndent;
import com.jeesite.modules.app.entity.AppLeague;
import com.jeesite.modules.app.entity.ClaimsTeacher;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单信息表DAO接口
 * @author zcq
 * @version 2019-08-05
 */
@MyBatisDao
public interface AppIndentDao extends CrudDao<AppIndent> {

    Object selectCountOpenOrder(int i);

    Object selectCountMoney();

    List<String> selectJpush();

    List<AppExcel> findExcelMesg(String beginTime, String lastTime, String dealTime, String messageSource, String sendPeople, String sendUnit, String state,String account,String sendBack, String orderNumber, String licensePlate, Integer userBId, String lastUpdateTime, String beginUpdateTime);

    List<AppLeague> findClaimsTeacher();

    List<String> findIndentImgs(String indentId);

    List<AppIndent> findProfessionalList(Integer pageoffset, String username, String plan);

    Long findProfessionalListCount(String username, String plan);

    BigDecimal findAllFix(String username, String plan);

    Integer findNewIndetNumber();


    Integer findInServiceNumber();


    Integer findforwardNumber();

    Integer findsettleNumber();


    Integer findfinishNumber();


    BigDecimal findEstimatedAmountINew();


    BigDecimal findEstimatedAmountInSer();


    BigDecimal findAllSettleAmount();


    List<AppIndent> findAllNoColleatIndent();

    Integer findThisMonCount(String id1);

    BigDecimal findThisMonAmount(String id1);

    Integer findByState(String upId, String userBId, int i);

    BigDecimal findByFixUser(String upId, String userBId, int i);

    BigDecimal findByUser(String upId, String userBId, int i);

    String findMerchantsName(String upId);

    String appIndentUsBName(String userBId);

    List<AppIndent> findRemake(String remake, Integer pageoffset);
}
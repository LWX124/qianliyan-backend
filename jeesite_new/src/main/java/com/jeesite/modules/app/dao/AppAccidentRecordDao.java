/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppAccidentRecord;

import java.math.BigDecimal;
import java.util.List;

/**
 * app上报事故信息表DAO接口
 * @author zcq
 * @version 2019-08-29
 */
@MyBatisDao
public interface AppAccidentRecordDao extends CrudDao<AppAccidentRecord> {

    List<AppAccidentRecord> findAllList();

//    List<AppAccidentRecord> findOtherList(Integer pageoffset);
//
//    Integer findallCount();
//
//    List<AppAccidentRecord> findrealness(Integer pageoffset, Integer realness);
//
//    Long findrealnesscount( Integer realness);
//
//    List<AppAccidentRecord> findstatuse(Integer pageoffset, Integer statuse);
//
//    Long findstatseCount( Integer statuse);
//
//    List<AppAccidentRecord> findcreatime(Integer pageoffset, String createTime);
//
//    Long findCreatetimeCount(String createTime);
//
//    List<AppAccidentRecord> findstatseandrealness(Integer pageoffset, Integer statuse, Integer realness);
//
//    Long findstatuserealnessCount( Integer statuse, Integer realness);
//
//    List<AppAccidentRecord> findStatuseAndCreatetime(Integer pageoffset, Integer statuse, String createTime);
//
//    Long findStatuseAndeCreatimeCOunt(Integer statuse, String createTime);
//
//    List<AppAccidentRecord> findRealnessAndCreatetime(Integer pageoffset, Integer realness, String createTime);
//
//    Long findRealnessAndCreatimeCount(Integer realness, String createTime);
//
//    List<AppAccidentRecord> findStatuseRealnessCreatetime(Integer pageoffset, Integer statuse, Integer realness, String createTime);
//
//    Long findStatuseRealnessCreatietimeCOunt(Integer statuse, Integer realness, String createTime);

    List<AppAccidentRecord> findmessage(Integer pageoffset, Integer statuse, Integer realness, String createTime, String phone);

    Long findcount();

    Integer findCommonappCount();

    Integer findWxCommonCount();

    BigDecimal findCommonAppAmount();

    BigDecimal findWxAmount();

    BigDecimal findPlusAmount();

    Integer findPlusCount();

    Integer findTodayCount();

    Integer findTodayWxCount();

    Integer findTodayAppCount();

    Integer findTodayAppPushCount();

    List<AppAccidentRecord> selectNotDelGeo();

    List<AppAccidentRecord> selectRepart(String accId, String type);

    Integer findBlackNumber();

    List<AppAccidentRecord> findAccbyUserid(String id);

    Integer findGreenNumber();


    Integer findTowRecord(String id);

    List<String> findgoingIndent(String id);

    void insertNewAcc(AppAccidentRecord appAccidentRecord);

    List<AppAccidentRecord> findTwomessage(Integer pageoffset, Integer statuse, Integer realness, String createTime, String phone);

    Integer findAudit();

    Integer findDayNumber();

    Integer findNightNumber();

}
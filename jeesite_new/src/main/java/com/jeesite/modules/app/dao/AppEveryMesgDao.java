/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppAccidentRecord;
import com.jeesite.modules.app.entity.AppEveryMesg;

import java.math.BigDecimal;

/**
 * 每日数据DAO接口
 * @author zcq
 * @version 2019-12-02
 */
@MyBatisDao
public interface AppEveryMesgDao extends CrudDao<AppEveryMesg> {

    AppEveryMesg selectTwoAgo(String format);

    AppAccidentRecord selectPayAmountAndCount(String format);

    AppAccidentRecord selectTodatAccident(String format);

    AppAccidentRecord selectPlusPayAmountCount(String format);

    AppAccidentRecord selectPassAccident(String format);

    AppAccidentRecord selectWxUp(String format);

    BigDecimal selectWxAomunt(String format);

    String selectWxcount(String format);
}
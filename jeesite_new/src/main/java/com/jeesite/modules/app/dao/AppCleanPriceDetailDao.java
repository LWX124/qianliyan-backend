/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppCleanPriceDetail;
import com.jeesite.modules.app.entity.AppLableDetailsReviewTree;

import java.util.List;

/**
 * 商户清洗价格明细表DAO接口
 * @author zcq
 * @version 2019-12-11
 */
@MyBatisDao
public interface AppCleanPriceDetailDao extends CrudDao<AppCleanPriceDetail> {

    List<AppCleanPriceDetail> finduserB(String userBId);

    AppCleanPriceDetail findProject(Integer userBId, Integer carType, Integer cleanType);

    AppLableDetailsReviewTree selectLable(Integer userBId, int lableId, int show);

    AppCleanPriceDetail selectForUpdate(String bussinessId);

}

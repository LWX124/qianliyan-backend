/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppMerchantsLable;

import java.math.BigDecimal;

/**
 * 商户和标签关系表DAO接口
 * @author zcq
 * @version 2019-08-20
 */
@MyBatisDao
public interface AppMerchantsLableDao extends CrudDao<AppMerchantsLable> {
	//查询是否有数据
    AppMerchantsLable selectBylable(Long merchantsId,Long lableId);

}
/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppMerchantsBrand;

/**
 * 品牌和商户关联表DAO接口
 * @author dh
 * @version 2019-10-19
 */
@MyBatisDao
public interface AppMerchantsBrandDao extends CrudDao<AppMerchantsBrand> {
	
}
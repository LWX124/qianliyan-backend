/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.CarInfor;

/**
 * car_inforDAO接口
 * @author y
 * @version 2022-10-09
 */
@MyBatisDao
public interface CarInforDao extends CrudDao<CarInfor> {
	
}
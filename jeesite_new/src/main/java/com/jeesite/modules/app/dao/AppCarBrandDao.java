/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppCarBrand;

import java.util.ArrayList;
import java.util.List;

/**
 * app_car_brandDAO接口
 * @author zcq
 * @version 2019-10-15
 */
@MyBatisDao
public interface AppCarBrandDao extends CrudDao<AppCarBrand> {

    List<AppCarBrand> findList4Like(String name);
    List<AppCarBrand> findCarBrandsById(ArrayList<Integer> brandIds);
}
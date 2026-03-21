/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppSubstituteDrivingIndent;
import com.jeesite.modules.app.entity.AppYearCheckIndent;

import java.util.List;

/**
 * 代驾订单表DAO接口
 * @author zcq
 * @version 2020-06-23
 */
@MyBatisDao
public interface AppSubstituteDrivingIndentDao extends CrudDao<AppSubstituteDrivingIndent> {

    AppSubstituteDrivingIndent findBySubDri(String subDriNumber);

    List<AppSubstituteDrivingIndent> findNoCondirmIndent();



}
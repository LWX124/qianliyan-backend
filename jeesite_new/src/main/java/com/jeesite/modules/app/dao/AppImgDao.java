/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppImg;
import com.jeesite.modules.app.entity.AppMerchants;

import java.util.List;

/**
 * 图片表DAO接口
 * @author zcq
 * @version 2019-08-06
 */
@MyBatisDao
public interface AppImgDao extends CrudDao<AppImg> {

    List<AppImg> findImg(Integer type, String  id);

    List<Integer> findAllImg(String id);


    void insertNew(AppImg appImg);

}
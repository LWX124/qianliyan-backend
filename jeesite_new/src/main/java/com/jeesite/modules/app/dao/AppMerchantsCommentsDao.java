/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppMerchantsComments;

import java.util.List;

/**
 * 商户评论表DAO接口
 * @author zcq
 * @version 2019-08-07
 */
@MyBatisDao
public interface AppMerchantsCommentsDao extends CrudDao<AppMerchantsComments> {

     List<AppMerchantsComments> findAll(String id);


     AppMerchantsComments findByCount(String content);
}
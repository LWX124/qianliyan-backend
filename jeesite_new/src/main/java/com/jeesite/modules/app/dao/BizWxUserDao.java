/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.BizWxUser;

/**
 * 微信用户信息表DAO接口
 * @author zcq
 * @version 2019-09-24
 */
@MyBatisDao
public interface BizWxUserDao extends CrudDao<BizWxUser> {

    BizWxUser findByOpenid(String openid);
}
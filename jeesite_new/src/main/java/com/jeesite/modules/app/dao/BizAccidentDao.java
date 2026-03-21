/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.BizAccident;

import java.util.List;

/**
 * 事故上报信息表DAO接口
 * @author zcq
 * @version 2019-09-24
 */
@MyBatisDao
public interface BizAccidentDao extends CrudDao<BizAccident> {

    List<BizAccident> selectNoDel();

    List<BizAccident> selectRepart(String accId, String type);

    String findIsFaId(String openid);

    void insertNewBiz(BizAccident newBizacc);

    BizAccident findOnePay(String faOpenId, String video);
}
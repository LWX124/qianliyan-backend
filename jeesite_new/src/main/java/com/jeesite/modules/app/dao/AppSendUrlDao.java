/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppSendOutSheet;
import com.jeesite.modules.app.entity.AppSendUrl;

import java.util.List;

/**
 * 派单记录图片表DAO接口
 * @author zcq
 * @version 2020-10-15
 */
@MyBatisDao
public interface AppSendUrlDao extends CrudDao<AppSendUrl> {

    void insertSendSheet(AppSendUrl appSendUrl);

    List<String> findImg(String pushBillId, Integer source, Integer type);

    List<String> findcarImg(String pushBillId, Integer source, Integer type);

}
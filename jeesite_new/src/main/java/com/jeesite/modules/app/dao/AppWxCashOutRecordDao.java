/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.app.entity.AppWxCashOutRecord;

import java.util.List;
import java.util.Map;

/**
 * 提现记录表DAO接口
 * @author dh
 * @version 2019-08-30
 */
@MyBatisDao
public interface AppWxCashOutRecordDao extends CrudDao<AppWxCashOutRecord> {

    List<AppWxCashOutRecord> selectForJob();

    List<AppWxCashOutRecord> selectForErrCode();

    void updateStatusById(AppWxCashOutRecord appWxCashOutRecord);

    List<Map> selectForResult();

    void updateResult(AppWxCashOutRecord appWxCashOutRecord);

    void updateContent(AppWxCashOutRecord appWxCashOutRecord);
}
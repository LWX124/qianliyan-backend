/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.constant2.ConsEnum;
import com.jeesite.modules.app.entity.AppWxpayOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * app_wxpay_orderDAO接口
 * @author dh
 * @version 2019-09-09
 */
@MyBatisDao
public interface AppWxpayOrderDao extends CrudDao<AppWxpayOrder> {

    List<Map> selectForPlusMem();

    void updateBusinessStatus(@Param("out_trade_no") String out_trade_no, @Param("wxPayOrderBusinessStatusOps") int wxPayOrderBusinessStatusOps);

    AppWxpayOrder findByPayNumber(String payNumber);
}

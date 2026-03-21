package com.stylefeng.guns.modular.system.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.stylefeng.guns.core.datascope.DataScope;
import com.stylefeng.guns.modular.system.model.BizWxpayOrder;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kosans
 * @since 2018-09-10
 */
public interface IBizWxpayOrderService extends IService<BizWxpayOrder> {
    boolean dealNotifyBusiness(Map<String,String> notifyMap);

    /**
     * 根据条件查询事故列表  分页
     */
    List<Map<String, Object>> selectList(@Param("page") Page<BizWxpayOrder> page, @Param("dataScope") DataScope dataScope, @Param("condition") String condition, @Param("outTradeNo") String outTradeNo, @Param("createStartTime") String createStartTime, @Param("createEndTime") String createEndTime, @Param("status") Integer status, @Param("orderByField") String orderByField, @Param("isAsc") boolean isAsc);

    boolean updateUserBalance(BizWxpayOrder bizWxpayOrder1, BigDecimal totalFee, String openId, String outTradeNo);

    BizWxpayOrder selectByOutTradeNo(@Param("out_trade_no") String out_trade_no);
}

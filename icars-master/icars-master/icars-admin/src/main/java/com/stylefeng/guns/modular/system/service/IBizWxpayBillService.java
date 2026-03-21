package com.stylefeng.guns.modular.system.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.modular.system.model.BizWxpayBill;
import com.baomidou.mybatisplus.service.IService;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 微信企业付款到零钱红包支付主表 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2018-08-21
 */
public interface IBizWxpayBillService extends IService<BizWxpayBill> {
    List<Map<String, Object>> selectListForPage(@Param("page") Page<BizWxpayBill> page, @Param("payStatus") Integer payStatus, @Param("openid") String  openid, @Param("startTime") String startTime, @Param("endTime") String endTime);

    List<Map<String, Object>> selectListForPage(@Param("page") Page<BizWxpayBill> page, @Param("openid") String openid);

    List<Map<String, Object>> selectFansListForPage(@Param("page") Page<BizWxpayBill> page, @Param("openid") String openid, @Param("startTime") String startTime, @Param("endTime") String endTime);

    Integer add(BizWxpayBill bizWxpayBill);

    BizWxpayBill selectOneByAccid(Integer accid);

    BizWxpayBill selectForRePay(Integer accid);

    /**
     * 定时任务 对支付失败的记录发起重新支付
     */
    void wxRePay();

    String getWxUnifiedOrderNo();

    Map<String, String> unifiedOrder(Map<String, String> reqData) throws Exception;
}

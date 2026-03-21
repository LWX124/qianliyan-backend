package com.stylefeng.guns.modular.system.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.modular.system.model.BizAlipayBill;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 支付宝营销红包支付主表 服务类
 * </p>
 *
 * @author kosan
 * @since 2018-08-01
 */
public interface IBizAlipayBillService extends IService<BizAlipayBill> {
    List<Map<String, Object>> selectListForPage(@Param("page") Page<BizAlipayBill> page, @Param("payStatus") Integer payStatus, @Param("startTime") String  alipayAccount, @Param("startTime") String startTime, @Param("endTime") String endTime);

    List<Map<String, Object>> selectListForPage(@Param("page") Page<BizAlipayBill> page, @Param("openid") String openid);

    Integer add(BizAlipayBill bizAlipayBill);

    BizAlipayBill selectOneByAccid(Integer accid);

    /**
     * 定时任务 对支付失败的记录发起重新支付
     */
    void rePay();
}

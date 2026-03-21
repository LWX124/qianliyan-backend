package com.stylefeng.guns.modular.system.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.modular.system.model.BizAlipayBill;
import com.stylefeng.guns.modular.system.model.BizWxpayBill;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 微信企业付款到零钱红包支付主表 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2018-08-21
 */
public interface BizWxpayBillMapper extends BaseMapper<BizWxpayBill> {
    /**
     * 根据条件查询事故列表 分页
     */
    List<Map<String, Object>> selectListForPage(@Param("page") Page<BizWxpayBill> page, @Param("payStatus") Integer payStatus, @Param("openid") String openid, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 根据openid查询红包记录
     */
    List<Map<String, Object>> selectListForPageByOpenid(@Param("page") Page<BizWxpayBill> page, @Param("openid") String openid);


    List<Map<String, Object>> selectFansListForPage(@Param("page") Page<BizWxpayBill> page, @Param("openid") String openid,  @Param("startTime") String startTime, @Param("endTime") String endTime);


    Integer add(@Param("bizWxpayBill") BizWxpayBill bizWxpayBill);

    /**
     * 根据支付状态查询红包支付记录
     */
    List<BizWxpayBill> selectListForRePay(@Param("status") Integer status);

    /**
     * 根据事故id查询红包支付记录
     */
    BizWxpayBill selectForRePay(@Param("accid") Integer accid);

    String getWxUnifiedOrderNo();
}

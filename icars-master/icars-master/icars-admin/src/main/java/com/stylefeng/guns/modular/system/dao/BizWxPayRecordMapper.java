package com.stylefeng.guns.modular.system.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.modular.system.model.BizWxPayRecord;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.stylefeng.guns.modular.system.model.BizWxpayBill;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 微信企业付款到零钱红包支付明细表 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2018-08-21
 */
public interface BizWxPayRecordMapper extends BaseMapper<BizWxPayRecord> {
    BigDecimal selectSumRedPack(@Param("openid") String openid, @Param("startTime") String startTime, @Param("endTime") String endTime);

    BigDecimal getRedPackPercentageCounts(@Param("openid") String openid, @Param("startTime") String startTime, @Param("endTime") String endTime);

    List<Map<String, Object>> getRedPackPercentage(@Param("page") Page<BizWxpayBill> page, String openid);

    BigDecimal getAllAmount(@Param("openid")String openId);
}

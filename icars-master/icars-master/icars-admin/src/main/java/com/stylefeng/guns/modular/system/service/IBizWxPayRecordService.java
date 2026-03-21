package com.stylefeng.guns.modular.system.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.modular.system.model.BizWxPayRecord;
import com.baomidou.mybatisplus.service.IService;
import com.stylefeng.guns.modular.system.model.BizWxpayBill;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 微信企业付款到零钱红包支付明细表 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2018-08-21
 */
public interface IBizWxPayRecordService extends IService<BizWxPayRecord> {
    BigDecimal selectSumRedPack(String openid, String startTime, String endTime);

    BigDecimal getRedPackPercentageCounts(String openid, String startTime, String endTime);

    List<Map<String, Object>> getRedPackPercentage(@Param("page") Page<BizWxpayBill> page, String openid);
}

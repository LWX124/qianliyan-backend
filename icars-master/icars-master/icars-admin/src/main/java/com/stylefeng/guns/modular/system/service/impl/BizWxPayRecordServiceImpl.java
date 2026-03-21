package com.stylefeng.guns.modular.system.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.modular.system.model.BizWxPayRecord;
import com.stylefeng.guns.modular.system.dao.BizWxPayRecordMapper;
import com.stylefeng.guns.modular.system.model.BizWxpayBill;
import com.stylefeng.guns.modular.system.service.IBizWxPayRecordService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 微信企业付款到零钱红包支付明细表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-08-21
 */
@Service
public class BizWxPayRecordServiceImpl extends ServiceImpl<BizWxPayRecordMapper, BizWxPayRecord> implements IBizWxPayRecordService {

    @Override
    public BigDecimal selectSumRedPack(String openid, String startTime, String endTime) {
        return this.baseMapper.selectSumRedPack(openid,startTime,endTime);
    }

    @Override
    public BigDecimal getRedPackPercentageCounts(String openid, String startTime, String endTime) {
        return this.baseMapper.getRedPackPercentageCounts(openid, startTime, endTime);
    }

    @Override
    public List<Map<String, Object>> getRedPackPercentage(Page<BizWxpayBill> page, String openid) {
        return this.baseMapper.getRedPackPercentage(page, openid);
    }
}

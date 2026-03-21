package com.stylefeng.guns.modular.system.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.modular.system.model.BizAlipayBill;
import com.stylefeng.guns.modular.system.model.BizWxpayBill;
import com.stylefeng.guns.modular.system.dao.BizWxpayBillMapper;
import com.stylefeng.guns.modular.system.service.IBizWxpayBillService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.stylefeng.guns.wxpay.IWxPayBizService;
import com.stylefeng.guns.wxpay.WXPayConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 微信企业付款到零钱红包支付主表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-08-21
 */
@Service
public class BizWxpayBillServiceImpl extends ServiceImpl<BizWxpayBillMapper, BizWxpayBill> implements IBizWxpayBillService {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IWxPayBizService wxPayBizService;


    @Override
    public List<Map<String, Object>> selectListForPage(Page<BizWxpayBill> page, Integer payStatus, String openid, String startTime, String endTime) {
        List<Map<String, Object>> result = this.baseMapper.selectListForPage(page, payStatus, openid, startTime, endTime);
        return result;
    }

    @Override
    public List<Map<String, Object>> selectListForPage(Page<BizWxpayBill> page, String openid) {
        List<Map<String, Object>> result = this.baseMapper.selectListForPageByOpenid(page, openid);
        return result;
    }

    @Override
    public List<Map<String, Object>> selectFansListForPage(Page<BizWxpayBill> page, String openid, String startTime, String endTime) {
        return this.baseMapper.selectFansListForPage(page, openid, startTime, endTime);
    }

    @Override
    public Integer add(BizWxpayBill bizWxpayBill) {
        return this.baseMapper.add(bizWxpayBill);
    }

    @Override
    public BizWxpayBill selectOneByAccid(Integer accid) {
        BizWxpayBill bizWxpayBill = new BizWxpayBill();
        bizWxpayBill.setAccid(accid);
        return this.baseMapper.selectOne(bizWxpayBill);
    }

    @Override
    public void wxRePay() {
        List<BizWxpayBill> bizWxpayBills = this.baseMapper.selectListForRePay(1);
        if(bizWxpayBills != null && bizWxpayBills.size() > 0){
            NumberFormat nf = NumberFormat.getInstance();
            nf.setGroupingUsed(false);
            log.info("待支付记录数："+bizWxpayBills.size());
            for (BizWxpayBill var1 : bizWxpayBills){
                if(var1 != null && var1.getAccid() != null){
                    boolean flag = wxPayBizService.autoTrigger(var1.getOpenid(), var1.getAccid(), nf.format(var1.getAmount().multiply(new BigDecimal(100)).doubleValue()));
                    if(flag){
                        log.info("微信定时支付成功，事故id："+var1.getAccid()+",用户微信openid："+var1.getOpenid());
                    } else {
                        log.info("微信定时支付失败，事故id："+var1.getAccid()+",用户微信openid："+var1.getOpenid());
                    }
                }
            }
        }
    }

    @Override
    public BizWxpayBill selectForRePay(Integer accid) {
        return this.baseMapper.selectForRePay(accid);
    }

    @Override
    public String getWxUnifiedOrderNo() {
        return this.baseMapper.getWxUnifiedOrderNo();
    }

    @Override
    public Map<String, String> unifiedOrder(Map<String, String> reqData) throws Exception {
        Map<String, String> res = wxPayBizService.unifiedOrder(reqData);
        return res;
    }
}

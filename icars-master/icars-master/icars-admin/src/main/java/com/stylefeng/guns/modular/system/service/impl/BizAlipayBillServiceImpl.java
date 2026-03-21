package com.stylefeng.guns.modular.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.alipay.IAlipayService;
import com.stylefeng.guns.modular.system.model.BizAlipayBill;
import com.stylefeng.guns.modular.system.dao.BizAlipayBillMapper;
import com.stylefeng.guns.modular.system.service.IBizAlipayBillService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.stylefeng.guns.modular.system.warpper.BizAlipayBillWarpper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 支付宝营销红包支付主表 服务实现类
 * </p>
 *
 * @author kosan
 * @since 2018-08-01
 */
@Service
public class BizAlipayBillServiceImpl extends ServiceImpl<BizAlipayBillMapper, BizAlipayBill> implements IBizAlipayBillService {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IAlipayService alipayService;

    @Override
    public List<Map<String, Object>> selectListForPage(Page<BizAlipayBill> page, Integer payStatus, String alipayAccount, String startTime, String endTime) {
        List<Map<String, Object>> result = this.baseMapper.selectListForPage(page, payStatus, alipayAccount, startTime, endTime);
        return result;
    }

    @Override
    public List<Map<String, Object>> selectListForPage(Page<BizAlipayBill> page, String openid) {
        List<Map<String, Object>> result = this.baseMapper.selectListForPageByOpenid(page, openid);
        return result;
    }

    @Override
    public Integer add(BizAlipayBill bizAlipayBill) {
        return this.baseMapper.add(bizAlipayBill);
    }

    @Override
    public BizAlipayBill selectOneByAccid(Integer accid) {
        BizAlipayBill bizAlipayBill = new BizAlipayBill();
        bizAlipayBill.setAccid(accid);
        return this.baseMapper.selectOne(bizAlipayBill);
    }

    @Override
    public void rePay() {
        List<BizAlipayBill> bizAlipayBills = this.baseMapper.selectListForRePay(1);
        if(bizAlipayBills != null && bizAlipayBills.size() > 0){
            log.info("微信待支付记录数："+bizAlipayBills.size());
            for (BizAlipayBill var1 : bizAlipayBills){
                if(var1 != null && var1.getAccid() != null){
                    boolean flag = alipayService.autoTrigger(var1.getAlipayAccount(), var1.getAccid());
                    if(flag){
                        log.info("定时支付成功，事故id："+var1.getAccid()+",支付宝："+var1.getAlipayAccount());
                    } else {
                        log.info("定时支付失败，事故id："+var1.getAccid()+",支付宝："+var1.getAlipayAccount());
                    }
                }
            }
        }
    }
}

package com.stylefeng.guns.modular.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.stylefeng.guns.core.datascope.DataScope;
import com.stylefeng.guns.modular.system.constant.BizYckCzmxStatus;
import com.stylefeng.guns.modular.system.constant.WxPayOrderStatus;
import com.stylefeng.guns.modular.system.dao.BizWxpayOrderMapper;
import com.stylefeng.guns.modular.system.model.BizWxpayOrder;
import com.stylefeng.guns.modular.system.model.BizYckBalance;
import com.stylefeng.guns.modular.system.model.BizYckCzmx;
import com.stylefeng.guns.modular.system.service.IBizWxpayOrderService;
import com.stylefeng.guns.modular.system.service.IBizYckBalanceService;
import com.stylefeng.guns.modular.system.service.IBizYckCzmxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-10
 */
@Service
@Transactional
public class BizWxpayOrderServiceImpl extends ServiceImpl<BizWxpayOrderMapper, BizWxpayOrder> implements IBizWxpayOrderService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private IBizYckBalanceService bizYckBalanceService;

    @Resource
    private IBizYckCzmxService bizYckCzmxService;

    @Override
    public List<Map<String, Object>> selectList(Page<BizWxpayOrder> page, DataScope dataScope, String condition, String outTradeNo, String createStartTime, String createEndTime, Integer status, String orderByField, boolean isAsc) {
        return this.baseMapper.selectList(page, dataScope, condition, outTradeNo, createStartTime, createEndTime, status, orderByField, isAsc);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean dealNotifyBusiness(Map<String, String> notifyMap) {
        String outTradeNo = notifyMap.get("out_trade_no");
        BigDecimal totalFee = new BigDecimal(notifyMap.get("total_fee"));
        String openId;
        if (notifyMap.containsKey("openid") || !notifyMap.get("openid").equals("")) {
            openId = notifyMap.get("openid");
        } else {
            openId = "1";
        }

        BizWxpayOrder bizWxpayOrder = new BizWxpayOrder();
        bizWxpayOrder.setOutTradeNo(outTradeNo);

        return updateUserBalance(this.baseMapper.selectOne(bizWxpayOrder), totalFee, openId, outTradeNo);
    }

    /**
     * 用户充值成功之后修改用户账户信息
     *
     * @param bizWxpayOrder 支付订单
     * @param totalFee      金额
     * @param openId        用户微信号
     * @param outTradeNo    订单号
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserBalance(BizWxpayOrder bizWxpayOrder, BigDecimal totalFee, String openId, String outTradeNo) {
        if (bizWxpayOrder != null) {
            if (bizWxpayOrder.getAmount().compareTo(totalFee.divide(new BigDecimal(100))) == 0) {
                bizWxpayOrder.setNotifyTime(new Date());
                bizWxpayOrder.setStatus(WxPayOrderStatus.PAY_SUCCESS.getCode());
                //update orders set status=1 where id=? and status=0 这种写法可以防止重复通知。
                boolean effected = this.update(bizWxpayOrder, new EntityWrapper<BizWxpayOrder>().eq("out_trade_no", outTradeNo).eq("status", 0));
                if (effected) {
                    String account = bizWxpayOrder.getAccount();
                    BizYckCzmx bizYckCzmx = new BizYckCzmx();
                    bizYckCzmx.setAmount(totalFee.divide(new BigDecimal(100)));
                    bizYckCzmx.setDetailType(BizYckCzmxStatus.RECHARGE.getCode());
                    bizYckCzmx.setOpenid(openId);
                    bizYckCzmx.setOperator("SYSTEM");
                    bizYckCzmx.setOrderNo(outTradeNo);
                    bizYckCzmx.setCreateTime(new Date());
                    bizYckCzmx.setCreateTime(new Date());
                    bizYckCzmx.setAccount(account);
                    //记录预存款操作明细
                    bizYckCzmxService.insert(bizYckCzmx);

                    //个人账户操作
                    BizYckBalance var2 = bizYckBalanceService.selectOne(new EntityWrapper<BizYckBalance>().eq("account", account));
                    if (var2 == null) {
                        BizYckBalance bizYckBalance = new BizYckBalance();
                        bizYckBalance.setOpenid(openId);
                        bizYckBalance.setAccount(account);
                        bizYckBalance.setBalance(totalFee.divide(new BigDecimal(100)));
                        bizYckBalance.setCreateTime(new Date());
                        bizYckBalance.setModifyTime(new Date());
                        //之前没有预存款账户记录。新增账户记录
                        return bizYckBalanceService.insert(bizYckBalance);
                    } else {
                        //之前存在预存款账户记录。修改增加预存款金额
                        return bizYckBalanceService.addBalance(totalFee.divide(new BigDecimal(100)), openId, new Date(), account);
                    }
                }

            }
        }
        return false;
    }

    public static void main(String[] args) {
        if (new BigDecimal("1").compareTo(new BigDecimal(100).divide(new BigDecimal(100))) == 0) {
            System.out.println("1111111");
        }
    }

    @Override
    public BizWxpayOrder selectByOutTradeNo(String out_trade_no) {
        return this.baseMapper.selectByOutTradeNo(out_trade_no);
    }
}

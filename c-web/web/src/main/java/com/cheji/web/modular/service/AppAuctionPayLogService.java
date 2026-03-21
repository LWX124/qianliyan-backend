package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.constant.ConsEnum;
import com.cheji.web.modular.domain.AppAuctionPayLogEntity;
import com.cheji.web.modular.mapper.AppAuctionPayLogMapper;
import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 支付记录
 * </p>
 *
 * @author yang
 */
@Service
public class AppAuctionPayLogService extends ServiceImpl<AppAuctionPayLogMapper, AppAuctionPayLogEntity> implements IService<AppAuctionPayLogEntity> {

    @Resource
    private AppAuctionPayLogMapper appAuctionPayLogMapper;

    //vip保证金支付记录插入
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void insertLog(WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest, WxPayAppOrderResult wxPayAppOrderResult, Integer userId, String vipLv) {
        AppAuctionPayLogEntity appAuctionPayLogEntity = new AppAuctionPayLogEntity();
        appAuctionPayLogEntity.setCreateTime(new Date());
        appAuctionPayLogEntity.setAmount(new BigDecimal(wxPayUnifiedOrderRequest.getTotalFee()));
        appAuctionPayLogEntity.setOutTradeNo(wxPayUnifiedOrderRequest.getOutTradeNo());
        appAuctionPayLogEntity.setStatus(ConsEnum.WX_PAY_ORDER_DEFAULT.getCode());//初始状态
        appAuctionPayLogEntity.setUserId(userId);
        appAuctionPayLogEntity.setPrepayId(wxPayAppOrderResult.getPrepayId());
        appAuctionPayLogEntity.setType(1);  //微信支付
        appAuctionPayLogEntity.setVipLv(vipLv);

        appAuctionPayLogMapper.insert(appAuctionPayLogEntity);
    }

    public AppAuctionPayLogEntity findByNo(String orderId) {
        return appAuctionPayLogMapper.findByNo(orderId);
    }

}

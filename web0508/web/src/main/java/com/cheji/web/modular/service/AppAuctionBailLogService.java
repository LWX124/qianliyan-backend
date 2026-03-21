package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.constant.ConsEnum;
import com.cheji.web.modular.domain.AppAuctionBailLogEntity;
import com.cheji.web.modular.domain.AppAuctionPayLogEntity;
import com.cheji.web.modular.mapper.AppAuctionBailLogMapper;
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
 *     支付记录
 * </p>
 *
 * @author yang
 */
@Service
public class AppAuctionBailLogService extends ServiceImpl<AppAuctionBailLogMapper, AppAuctionBailLogEntity> implements IService<AppAuctionBailLogEntity> {
    @Resource
    private AppAuctionBailLogMapper appAuctionBailLogMapper;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void insertLog(WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest, WxPayAppOrderResult wxPayAppOrderResult, Integer userId, Long carId) {
        AppAuctionBailLogEntity appAuctionBailLogEntity = new AppAuctionBailLogEntity();
        appAuctionBailLogEntity.setCreateTime(new Date());
        appAuctionBailLogEntity.setAmount(new BigDecimal(wxPayUnifiedOrderRequest.getTotalFee()));
        appAuctionBailLogEntity.setOutTradeNo(wxPayUnifiedOrderRequest.getOutTradeNo());
        appAuctionBailLogEntity.setStatus(ConsEnum.WX_PAY_ORDER_DEFAULT.getCode());//初始状态
        appAuctionBailLogEntity.setUserId(userId);
        appAuctionBailLogEntity.setPrepayId(wxPayAppOrderResult.getPrepayId());
        appAuctionBailLogEntity.setType(1);  //微信支付
        appAuctionBailLogEntity.setCarId(carId);

        appAuctionBailLogMapper.insert(appAuctionBailLogEntity);
    }

    public AppAuctionBailLogEntity findByNo(String orderId) {
        return appAuctionBailLogMapper.findByNo(orderId);
    }
}

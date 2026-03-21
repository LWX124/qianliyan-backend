package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.constant.ConsEnum;
import com.cheji.web.modular.domain.AppAuctionBailLogEntity;
import com.cheji.web.modular.domain.AppAuctionOnePriceCarLogEntity;
import com.cheji.web.modular.mapper.AppAuctionBailLogMapper;
import com.cheji.web.modular.mapper.AppAuctionOnePriceCarLogMapper;
import com.cheji.web.util.COrderNoUtil;
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
public class AppAuctionOnePriceService extends ServiceImpl<AppAuctionOnePriceCarLogMapper, AppAuctionOnePriceCarLogEntity>
        implements IService<AppAuctionOnePriceCarLogEntity> {
    @Resource
    private AppAuctionOnePriceCarLogMapper appAuctionOnePriceCarLogMapper;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void insertLog(WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest, WxPayAppOrderResult wxPayAppOrderResult, Integer userId, Long carId) {
        AppAuctionOnePriceCarLogEntity appAuctionOnePriceCarLogEntity = new AppAuctionOnePriceCarLogEntity();
        appAuctionOnePriceCarLogEntity.setOutTradeNo(wxPayUnifiedOrderRequest.getOutTradeNo());
        appAuctionOnePriceCarLogEntity.setCarId(carId);
        appAuctionOnePriceCarLogEntity.setAmount(wxPayUnifiedOrderRequest.getTotalFee());
        appAuctionOnePriceCarLogEntity.setCreateTime(new Date());
        appAuctionOnePriceCarLogEntity.setUserId(userId);
        appAuctionOnePriceCarLogEntity.setStatus(1);//支付状态：1 初始状态  2 支付成功  3 支付失败 4 不用支付
        appAuctionOnePriceCarLogEntity.setUpdateTime(new Date());

        appAuctionOnePriceCarLogMapper.insert(appAuctionOnePriceCarLogEntity);
    }


}

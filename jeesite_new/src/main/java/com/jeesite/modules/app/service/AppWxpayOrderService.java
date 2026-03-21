/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.app.dao.AppWxpayBackDao;
import com.jeesite.modules.app.dao.AppWxpayOrderDao;
import com.jeesite.modules.app.entity.AppWxpayBack;
import com.jeesite.modules.app.entity.AppWxpayOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * app_wxpay_orderService
 *
 * @author dh
 * @version 2019-12-16
 */
@Service
@Transactional(readOnly = true)
public class AppWxpayOrderService extends CrudService<AppWxpayOrderDao, AppWxpayOrder> {

    @Resource
    private AppWxpayOrderDao appWxpayOrderDao;

    @Resource
    private AppWxpayBackDao appWxpayBackDao;

    @Resource
    private WxPayService wxPayService;

    @Value("${backMoneyNotifyUrl}")
    private String backMoneyNotifyUrl;

    /**
     * 执行退款请求
     * @param appWxpayOrder1  支付订单对象
     * @param type  1洗车  3：救援
     * @param businessId  业务id
     */
    @Transactional
    public void doBack(AppWxpayOrder appWxpayOrder1, Integer type, String businessId) throws WxPayException {
        logger.info("### 退款###   appWxpayOrder1={};type={};businessId={}", appWxpayOrder1, type, businessId);
        WxPayRefundRequest wxPayRefundRequest = new WxPayRefundRequest();
        wxPayRefundRequest.setOutTradeNo(appWxpayOrder1.getOutTradeNo());
        String uniqueOrder = getUniqueOrder();
        wxPayRefundRequest.setAppid("wxfd263ff6e281f881");
        wxPayRefundRequest.setMchId("1487083832");
        wxPayRefundRequest.setOutRefundNo(uniqueOrder);
        wxPayRefundRequest.setOpUserId("0001");
        wxPayRefundRequest.setNotifyUrl(backMoneyNotifyUrl);
        wxPayRefundRequest.setTotalFee(new Double(appWxpayOrder1.getAmount()).intValue());
        wxPayRefundRequest.setRefundFee(new Double(appWxpayOrder1.getAmount()).intValue());

        AppWxpayBack appWxpayBack = new AppWxpayBack();
        appWxpayBack.setOutTradeNo(appWxpayOrder1.getOutTradeNo());
        appWxpayBack.setOutRefundNo(uniqueOrder);
        appWxpayBack.setType(type);
        appWxpayBack.setBusinessId(businessId);
        appWxpayBackDao.insert(appWxpayBack);

        WxPayRefundResult refund = wxPayService.refund(wxPayRefundRequest);
        //todo 判断退款状态
        logger.info("### refund 发起退款  refund={}", refund);
        appWxpayOrder1.setStatus("4");
        appWxpayOrderDao.update(appWxpayOrder1);
    }

    /**
     * 获得退款唯一订单号
     */
    public static String getUniqueOrder() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String format2 = format.format(new Date());
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {
            //有可能是负数
            hashCodeV = -hashCodeV;
        }
        return "tk" + format2 + String.format("%012d", hashCodeV);
    }

    /**
     * 获取单条数据
     *
     * @param appWxpayOrder
     * @return
     */
    @Override
    public AppWxpayOrder get(AppWxpayOrder appWxpayOrder) {
        return super.get(appWxpayOrder);
    }

    /**
     * 查询分页数据
     *
     * @param appWxpayOrder      查询条件
     * @param appWxpayOrder.page 分页对象
     * @return
     */
    @Override
    public Page<AppWxpayOrder> findPage(AppWxpayOrder appWxpayOrder) {
        return super.findPage(appWxpayOrder);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param appWxpayOrder
     */
    @Override
    @Transactional(readOnly = false)
    public void save(AppWxpayOrder appWxpayOrder) {
        super.save(appWxpayOrder);
    }

    /**
     * 更新状态
     *
     * @param appWxpayOrder
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(AppWxpayOrder appWxpayOrder) {
        super.updateStatus(appWxpayOrder);
    }

    /**
     * 删除数据
     *
     * @param appWxpayOrder
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(AppWxpayOrder appWxpayOrder) {
        super.delete(appWxpayOrder);
    }

}

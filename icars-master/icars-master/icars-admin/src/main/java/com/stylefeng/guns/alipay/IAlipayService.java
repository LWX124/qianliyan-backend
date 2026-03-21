package com.stylefeng.guns.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayResponse;

import java.math.BigDecimal;

public interface IAlipayService {
    AlipayResponse alipayMarketingCampaignCashCreate(BigDecimal totalMoney, BigDecimal totalNum, String startTime, String endTime) throws AlipayApiException;

    AlipayResponse alipayMarketingCampaignCashTrigger(String crowdNo, String loginId, String outBizNo) throws AlipayApiException;

    AlipayResponse alipayMarketingCampaignCashDetailQuery(String crowdNo) throws AlipayApiException;

    AlipayResponse alipayMarketingCampaignCashStatusModify(String crowdNo) throws AlipayApiException;

    AlipayResponse alipayMarketingCampaignCashListQuery(String campStatus, int pageSize, int pageIndex) throws AlipayApiException;

    boolean autoTrigger(String loginId, Integer accid);

}

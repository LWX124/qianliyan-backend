package com.stylefeng.guns.alipay;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayResponse;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.*;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

/**
 * 营销活动送红包 抽象业务接口
 */
public abstract class AbStractAlipayService implements IAlipayService{
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private volatile static AlipayClient alipayClient;

    @Value("${alipay.appid}")
    private  String APP_ID;

    @Value("${alipay.publickey}")
    private  String ALIPAY_PUBLIC_KEY;

    @Value("${alipay.privatekey}")
    private  String APP_PRIVATE_KEY;

    @Value("${alipay.gateway}")
    private  String GATEWAY;

    private AlipayClient getInstance(){
        if(alipayClient == null){
            synchronized (AlipayClient.class){
                if(alipayClient == null){
                    alipayClient = new DefaultAlipayClient(GATEWAY, APP_ID, APP_PRIVATE_KEY, "json", Constant.CHARSET_UTF8, ALIPAY_PUBLIC_KEY, "RSA2");
                }
            }
        }
        return alipayClient;
    }

    /**
     * 创建现金活动
     * @param totalMoney 活动总金额
     * @param totalNum 总的红包个数
     * @param startTime 活动有效开始时间
     * @param endTime 活动有效结束时间
     * @return
     * @throws AlipayApiException
     */
    @Override
    public AlipayResponse alipayMarketingCampaignCashCreate(BigDecimal totalMoney, BigDecimal totalNum, String startTime, String endTime) throws AlipayApiException {
        AlipayMarketingCampaignCashCreateRequest request = new AlipayMarketingCampaignCashCreateRequest();
        AlipayMarketingCampaignCashCreateModel model = new AlipayMarketingCampaignCashCreateModel();
        model.setCouponName("爱车士回馈活动");
        model.setPrizeType("fixed");
        model.setTotalMoney(totalMoney.toString());
        model.setTotalNum(totalNum.toString());
        model.setPrizeMsg("信息回馈红包");
        model.setStartTime(startTime + " 00:00:00");
        model.setEndTime(endTime + " 00:00:00");
        model.setSendFreqency("L100");
        request.setBizModel(model);
        log.info("支付宝接口请求参数："+JSON.toJSONString(request));
        AlipayMarketingCampaignCashCreateResponse response = getInstance().execute(request);
        log.info("支付宝接口返回参数："+JSON.toJSONString(response));
        return response;
    }

    /**
     * 触发现金红包活动
     * @param crowdNo 现金活动号
     * @param loginId 红包发放支付宝账号
     * @param outBizNo 外部订单号
     * @return
     * @throws AlipayApiException
     */
    @Override
    public AlipayResponse alipayMarketingCampaignCashTrigger(String crowdNo, String loginId, String outBizNo) throws AlipayApiException {
        AlipayMarketingCampaignCashTriggerRequest request = new AlipayMarketingCampaignCashTriggerRequest();
        AlipayMarketingCampaignCashTriggerModel model = new AlipayMarketingCampaignCashTriggerModel();
        model.setCrowdNo(crowdNo);
        model.setLoginId(loginId);
        model.setOutBizNo(outBizNo);
        request.setBizModel(model);
        log.info("支付宝接口请求参数："+JSON.toJSONString(request));
        AlipayMarketingCampaignCashTriggerResponse response = getInstance().execute(request);
        log.info("支付宝接口请求参数："+JSON.toJSONString(response));
        return response;
    }

    /**
     * 现金活动详情查询
     * @param crowdNo 现金活动号
     * @return
     * @throws AlipayApiException
     */
    @Override
    public AlipayResponse alipayMarketingCampaignCashDetailQuery(String crowdNo) throws AlipayApiException {
        AlipayMarketingCampaignCashDetailQueryRequest request = new AlipayMarketingCampaignCashDetailQueryRequest();
        AlipayMarketingCampaignCashDetailQueryModel model = new AlipayMarketingCampaignCashDetailQueryModel();
        model.setCrowdNo(crowdNo);
        request.setBizModel(model);
        log.info("支付宝接口请求参数："+JSON.toJSONString(request));
        AlipayMarketingCampaignCashDetailQueryResponse response = getInstance().execute(request);
        log.info("支付宝接口请求参数："+JSON.toJSONString(response));
        return response;
    }

    /**
     * 现金活动列表查询
     * @param campStatus 活动状态
     * @param pageSize  分页查询时每页返回的列表大小,最大为50
     * @param pageIndex 分页查询时的页码，从1开始
     * @return
     * @throws AlipayApiException
     */
    @Override
    public AlipayResponse alipayMarketingCampaignCashListQuery(String campStatus, int pageSize, int pageIndex) throws AlipayApiException {
        AlipayMarketingCampaignCashListQueryRequest request = new AlipayMarketingCampaignCashListQueryRequest();
        AlipayMarketingCampaignCashListQueryModel model = new AlipayMarketingCampaignCashListQueryModel();
        model.setCampStatus(campStatus);
        model.setPageIndex(pageIndex+"");
        model.setPageSize(pageSize+"");
        request.setBizModel(model);
        log.info("支付宝接口请求参数："+JSON.toJSONString(request));
        AlipayMarketingCampaignCashListQueryResponse response = getInstance().execute(request);
        log.info("支付宝接口请求参数："+JSON.toJSONString(response));
        return response;
    }



    /**
     * 更改现金活动状态
     * @param crowdNo 现金活动号
     * @return
     * @throws AlipayApiException
     */
    @Override
    public AlipayResponse alipayMarketingCampaignCashStatusModify(String crowdNo) throws AlipayApiException {
        AlipayMarketingCampaignCashStatusModifyRequest request = new AlipayMarketingCampaignCashStatusModifyRequest();
        AlipayMarketingCampaignCashStatusModifyModel model = new AlipayMarketingCampaignCashStatusModifyModel();
        model.setCrowdNo(crowdNo);
        request.setBizModel(model);
        log.info("支付宝接口请求参数："+JSON.toJSONString(request));
        AlipayMarketingCampaignCashStatusModifyResponse response = getInstance().execute(request);
        log.info("支付宝接口请求参数："+JSON.toJSONString(response));
        return response;
    }

}

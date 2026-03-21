package com.stylefeng.guns.modular.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.CashCampaignInfo;
import com.alipay.api.response.AlipayMarketingCampaignCashCreateResponse;
import com.alipay.api.response.AlipayMarketingCampaignCashDetailQueryResponse;
import com.alipay.api.response.AlipayMarketingCampaignCashListQueryResponse;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.alipay.IAlipayService;
import com.stylefeng.guns.core.common.exception.BizExceptionEnum;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.modular.system.model.AlipayActivity;
import com.stylefeng.guns.modular.system.dao.AlipayActivityMapper;
import com.stylefeng.guns.modular.system.service.IAlipayActivityService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 支付宝营销红包活动表 服务实现类
 * </p>
 *
 * @author kosans
 * @since 2018-07-31
 */
@Service
@Transactional
public class AlipayActivityServiceImpl extends ServiceImpl<AlipayActivityMapper, AlipayActivity> implements IAlipayActivityService {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(AlipayActivityServiceImpl.class);

    @Autowired
    @Qualifier("alipayService")
    private IAlipayService alipayService;

    @Override
    public List<Map<String, Object>> selectListForPage(Page<AlipayActivity> page, String startTime, String endTime, String orderByField, boolean isAsc) {
        List<Map<String, Object>> result = this.baseMapper.selectListForPage(page, startTime, endTime, orderByField, isAsc);
        for(Map<String, Object> map : result){
            try {
                AlipayMarketingCampaignCashDetailQueryResponse response = (AlipayMarketingCampaignCashDetailQueryResponse)alipayService.alipayMarketingCampaignCashDetailQuery(map.get("crowd_no").toString());
                if(response.isSuccess()){
                    map.put("originCrowdNo",response.getOriginCrowdNo());
                    map.put("sendAmount",response.getSendAmount());
                    map.put("totalAmount",response.getTotalAmount());
                    map.put("totalCount",response.getTotalCount());
                    map.put("campStatus",response.getCampStatus());
                    map.put("couponName",response.getCouponName());
                }
            } catch (AlipayApiException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> selectListForPageFromApi(int pageSize, int pageIndex, String campStatus) {
        List<Map<String, Object>> maps = null;
        try {
            AlipayMarketingCampaignCashListQueryResponse response = (AlipayMarketingCampaignCashListQueryResponse)alipayService.alipayMarketingCampaignCashListQuery(campStatus, pageSize, pageIndex);
            if(response.isSuccess() && response.getCampList() != null && response.getCampList().size() > 0){
                for(CashCampaignInfo cashCampaignInfo : response.getCampList()){
                    AlipayMarketingCampaignCashDetailQueryResponse detailResponse = (AlipayMarketingCampaignCashDetailQueryResponse)alipayService.alipayMarketingCampaignCashDetailQuery(cashCampaignInfo.getCrowdNo());
                    if(detailResponse.isSuccess()){
                        Map<String, Object> map = new HashMap<>();
                        map.put("crowdNo",detailResponse.getCrowdNo());
                        map.put("startTime",detailResponse.getStartTime());
                        map.put("endTime",detailResponse.getEndTime());
                        map.put("couponName",detailResponse.getCouponName());
                        map.put("originCrowdNo",detailResponse.getOriginCrowdNo());
                        map.put("sendAmount",detailResponse.getSendAmount());
                        map.put("totalAmount",detailResponse.getTotalAmount());
                        map.put("totalCount",detailResponse.getTotalCount());
                        map.put("campStatus",detailResponse.getCampStatus());
                        maps.add(map);
                    }
                }
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return maps;
    }

    @Override
    public void add(AlipayActivity alipayActivity) {
        try {
            AlipayMarketingCampaignCashCreateResponse response = (AlipayMarketingCampaignCashCreateResponse)alipayService.alipayMarketingCampaignCashCreate(alipayActivity.getTotalAmount(), alipayActivity.getTotalCount(), DateUtil.getDay(alipayActivity.getStartTime()), DateUtil.getDay(alipayActivity.getEndTime()));
            if(response.isSuccess()){
                alipayActivity.setCrowdNo(response.getCrowdNo());
                alipayActivity.setPayUrl(URLDecoder.decode(response.getPayUrl(),"utf-8"));
                alipayActivity.setOriginCrowdNo(response.getOriginCrowdNo());
                alipayActivity.setCreateTime(new Date());
                this.baseMapper.insert(alipayActivity);
            } else {
                LOGGER.info("alipay response:"+JSON.toJSONString(response));
                throw new GunsException(BizExceptionEnum.ALIPAY_REQUEST_FAIL);
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }

    @Override
    public AlipayActivity selectMaxNew() {
        return this.baseMapper.selectMaxNew();
    }
}

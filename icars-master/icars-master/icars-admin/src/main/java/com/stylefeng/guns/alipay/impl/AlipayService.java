package com.stylefeng.guns.alipay.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipayMarketingCampaignCashTriggerResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.alipay.AbStractAlipayService;
import com.stylefeng.guns.mail.IMailService;
import com.stylefeng.guns.modular.system.constant.BizAlipayBillStatus;
import com.stylefeng.guns.modular.system.model.AlipayActivity;
import com.stylefeng.guns.modular.system.model.BizAlipayBill;
import com.stylefeng.guns.modular.system.model.BizAlipayPayRecord;
import com.stylefeng.guns.modular.system.model.BizNotifyMail;
import com.stylefeng.guns.modular.system.service.IAlipayActivityService;
import com.stylefeng.guns.modular.system.service.IBizAlipayBillService;
import com.stylefeng.guns.modular.system.service.IBizAlipayPayRecordService;
import com.stylefeng.guns.modular.system.service.IBizNotifyMailService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 营销活动送红包 业务实现类
 */
@Service
@Transactional
public class AlipayService extends AbStractAlipayService{
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IAlipayActivityService alipayActivityService;

    @Autowired
    private IBizAlipayBillService bizAlipayBillService;

    @Autowired
    private IBizAlipayPayRecordService bizAlipayPayRecordService;

    @Autowired
    @Qualifier("textMailService")
    private IMailService mailService;

    @Autowired
    IBizNotifyMailService bizNotifyMailService;

    @Override
    @Transactional
    public boolean autoTrigger(String loginId, Integer accid) {
        BizAlipayBill bizAlipayBill = bizAlipayBillService.selectOneByAccid(accid);
        if(bizAlipayBill != null && bizAlipayBill.getStatus() == 0){
            log.info("支付红包->事故id:"+accid+",已支付，无法重复支付");
            return false;
        }
        AlipayActivity alipayActivity = alipayActivityService.selectMaxNew();

        String outBizNo = System.currentTimeMillis()+""+accid;
        if(alipayActivity != null && StringUtils.isNotEmpty(alipayActivity.getCrowdNo())){
            try {
                AlipayMarketingCampaignCashTriggerResponse response = (AlipayMarketingCampaignCashTriggerResponse)super.alipayMarketingCampaignCashTrigger(alipayActivity.getCrowdNo(), loginId, outBizNo);
                if(response.isSuccess()){
                    //如果已有支付记录，更新状态和支付时间
                    if(bizAlipayBill != null){
                        bizAlipayBill.setPayTime(new Date());
                        bizAlipayBill.setStatus(BizAlipayBillStatus.SUCCESS.getCode());
                        bizAlipayBillService.updateById(bizAlipayBill);
                    } else {
                        bizAlipayBill = new BizAlipayBill();
                        bizAlipayBill.setAccid(accid);
                        bizAlipayBill.setCreateTime(new Date());
                        bizAlipayBill.setPayTime(new Date());
                        bizAlipayBill.setStatus(BizAlipayBillStatus.SUCCESS.getCode());
                        bizAlipayBillService.add(bizAlipayBill);
                    }
                    BizAlipayPayRecord record = bizAlipayPayRecordService.selectOne(new EntityWrapper<BizAlipayPayRecord>().eq("bill_id", bizAlipayBill.getId()));
                    if(record == null){
                        record = new BizAlipayPayRecord();
                        record.setBillId(bizAlipayBill.getId());
                        record.setCreateTime(new Date());
                    }
                    record.setAlipayAccount(loginId);
                    record.setResponseInfo(JSON.toJSONString(response));
                    bizAlipayPayRecordService.insertOrUpdate(record);
                    return true;
                } else {
                    if(bizAlipayBill != null){
                        bizAlipayBill.setPayTime(new Date());
                        bizAlipayBill.setStatus(BizAlipayBillStatus.FAIL.getCode());
                        bizAlipayBillService.updateById(bizAlipayBill);
                    } else {
                        bizAlipayBill = new BizAlipayBill();
                        bizAlipayBill.setAccid(accid);
                        bizAlipayBill.setCreateTime(new Date());
                        bizAlipayBill.setPayTime(new Date());
                        bizAlipayBill.setStatus(BizAlipayBillStatus.FAIL.getCode());
                        bizAlipayBillService.add(bizAlipayBill);
                    }
                    BizAlipayPayRecord record = bizAlipayPayRecordService.selectOne(new EntityWrapper<BizAlipayPayRecord>().eq("bill_id", bizAlipayBill.getId()));
                    if(record == null){
                        record = new BizAlipayPayRecord();
                        record.setBillId(bizAlipayBill.getId());
                        record.setCreateTime(new Date());
                    }
                    record.setAlipayAccount(loginId);
                    record.setResponseInfo(JSON.toJSONString(response));
                    bizAlipayPayRecordService.insertOrUpdate(record);

                    //接口请求失败，发送邮件告知异常信息
                    List<BizNotifyMail> bizNotifyMails = bizNotifyMailService.selectList(new EntityWrapper<BizNotifyMail>().eq("mail_type", 0));
                    if(bizNotifyMails != null && bizNotifyMails.size() > 0){
                        List<String> mails = new ArrayList<>();
                        for(BizNotifyMail var : bizNotifyMails){
                            if(StringUtils.isNotEmpty(var.getEmail())){
                                mails.add(var.getEmail());
                            }
                        }
                        String[] mailArr = new String[mails.size()];
                        mails.toArray(mailArr);
                        if(mailArr.length > 0){
                            try {
                                mailService.sendTextMail(mailArr, "支付宝红包发放异常通知", JSON.toJSONString(response));
                            } catch (MessagingException e) {
                                log.error("发送邮件异常",e);
                            }
                        }
                    }
                    return false;
                }
            } catch (AlipayApiException e) {
                log.error("支付红包->事故id:"+accid+",发生异常",e);
            }
            return false;
        } else {
            if(bizAlipayBill != null){
                bizAlipayBill.setPayTime(new Date());
                bizAlipayBill.setStatus(BizAlipayBillStatus.FAIL.getCode());
                bizAlipayBillService.updateById(bizAlipayBill);
            } else {
                bizAlipayBill = new BizAlipayBill();
                bizAlipayBill.setAccid(accid);
                bizAlipayBill.setCreateTime(new Date());
                bizAlipayBill.setPayTime(new Date());
                bizAlipayBill.setStatus(BizAlipayBillStatus.FAIL.getCode());
                bizAlipayBillService.add(bizAlipayBill);
            }
            BizAlipayPayRecord record = bizAlipayPayRecordService.selectOne(new EntityWrapper<BizAlipayPayRecord>().eq("bill_id", bizAlipayBill.getId()));
            if(record == null){
                record = new BizAlipayPayRecord();
                record.setBillId(bizAlipayBill.getId());
                record.setCreateTime(new Date());
            }
            record.setAlipayAccount(loginId);
            record.setResponseInfo("{\"subCode\":\"BIZ_ACTIVITY_NULL\",\"subMsg\":\"没有创建支付宝活动\"}");
            bizAlipayPayRecordService.insertOrUpdate(record);
        }
        return false;
    }
}

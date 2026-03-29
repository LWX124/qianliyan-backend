package com.stylefeng.guns.wxpay;

import com.stylefeng.guns.config.properties.WxPayV3Properties;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAPublicKeyConfig;
import com.wechat.pay.java.service.transferbatch.TransferBatchService;
import com.wechat.pay.java.service.transferbatch.model.InitiateBatchTransferRequest;
import com.wechat.pay.java.service.transferbatch.model.InitiateBatchTransferResponse;
import com.wechat.pay.java.service.transferbatch.model.TransferDetailInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.Instant;
import java.util.Collections;

@Service
public class WxPayV3TransferService {

    private static final Logger log = LoggerFactory.getLogger(WxPayV3TransferService.class);

    @Resource
    private WxPayV3Properties v3Properties;

    private TransferBatchService transferBatchService;

    @PostConstruct
    public void init() {
        String apiV3Key = v3Properties.getApiV3Key();
        if (apiV3Key == null || apiV3Key.contains("待填") || apiV3Key.length() != 32) {
            log.warn("V3商家转账配置不完整(apiV3Key未配置)，跳过初始化，transferToUser将返回false");
            return;
        }
        try {
            Config config = new RSAPublicKeyConfig.Builder()
                    .merchantId(v3Properties.getMchId())
                    .privateKeyFromPath(v3Properties.getPrivateKeyPath())
                    .publicKeyFromPath(v3Properties.getPublicKeyPath())
                    .publicKeyId(v3Properties.getPublicKeyId())
                    .merchantSerialNumber(v3Properties.getCertSerialNo())
                    .apiV3Key(apiV3Key)
                    .build();
            transferBatchService = new TransferBatchService.Builder().config(config).build();
        } catch (Throwable t) {
            log.error("V3商家转账初始化失败，transferToUser将返回false", t);
        }
    }

    /**
     * 商家转账到用户零钱（V3）
     *
     * @param openid    用户小程序openid
     * @param accid     事故ID（用于生成唯一批次号）
     * @param amountFen 金额，单位：分
     * @return true=成功，false=失败
     */
    public boolean transferToUser(String openid, Integer accid, long amountFen) {
        if (transferBatchService == null) {
            log.warn("V3商家转账未初始化，无法执行转账 accid={}", accid);
            return false;
        }
        try {
            long now = Instant.now().toEpochMilli();
            String batchNo = "accid" + accid + "t" + now;
            String detailNo = "detail" + accid + "t" + now;

            TransferDetailInput detail = new TransferDetailInput();
            detail.setOutDetailNo(detailNo);
            detail.setTransferAmount(amountFen);
            detail.setTransferRemark("事故上报红包奖励");
            detail.setOpenid(openid);

            InitiateBatchTransferRequest request = new InitiateBatchTransferRequest();
            request.setAppid(v3Properties.getAppId());
            request.setOutBatchNo(batchNo);
            request.setBatchName("事故上报奖励");
            request.setBatchRemark("感谢您参加提报事故，为城市做一份贡献");
            request.setTotalAmount(amountFen);
            request.setTotalNum(1);
            request.setTransferDetailList(Collections.singletonList(detail));

            InitiateBatchTransferResponse response = transferBatchService.initiateBatchTransfer(request);
            log.info("V3商家转账成功 accid={} batchId={} batchNo={}",
                    accid, response.getBatchId(), response.getOutBatchNo());
            return true;
        } catch (Exception e) {
            log.error("V3商家转账失败 accid={} openid={} error={}", accid, openid, e.getMessage(), e);
            return false;
        }
    }
}

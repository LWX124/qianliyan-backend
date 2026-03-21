package com.jeesite.test;

import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.jeesite.modules.Application;
import com.jeesite.modules.app.dao.AppAuctionBailRefundLogDao;
import com.jeesite.modules.app.entity.AppAuctionBailRefundLog;
import com.jeesite.modules.app.service.AppAuctionBailRefundLogService;
import com.jeesite.modules.app.service.AppAuctionBidLogService;
import com.jeesite.modules.app.service.AppAuctionService;
import com.jeesite.modules.constant2.AppAuctionRefundConstans;
import com.jeesite.modules.util2.WxPayV2Util;
import javassist.ClassPath;
import jodd.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.io.ResourceUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Slf4j
public class Test {
	@Autowired
	private AppAuctionService ss;
	@Autowired
	private AppAuctionBailRefundLogService bbb;
	@Autowired
	private WxPayV2Util wxPayV2Util;
	@Value("${wx.appId}")
	private String appId;
	@Value("${wx.mchId}")
	private String mchId;
	@Value("${wx.mchKey}")
	private String mchKey;
	@Value("${wx.certPath}")
	private String certPath;


//	@org.junit.Test
//	public void test() throws Exception {
//		String path = ClassLoader.getSystemResource(certPath).getPath();
//		byte[] bytes = FileUtil.readBytes(path);
//		AppAuctionBailRefundLog refundUser = bbb.get("3");
//		Double amount = refundUser.getAmount() / 100;
//		WxPayRefundResult refund = wxPayV2Util.refund(appId, mchId, mchKey, bytes, refundUser.getOutTradeNo()
//				, refundUser.getOutRefundNo(), new BigDecimal(amount.toString()), new BigDecimal(amount.toString()));
////		WxPayRefundNotifyResult result = BaseWxPayResult.fromXML(refund.getXmlString(), WxPayRefundNotifyResult.class);
//		refundUser.setRefundId(refund.getRefundId());
//		refundUser.setBackStatus(refund.getResultCode());
//		refundUser.setNotifyTime(new Date());
//		if (refund.getResultCode().equals("SUCCESS")) {
//			refundUser.setState(AppAuctionRefundConstans.AUCTION_REFUND_SUCCESS);
//		} else {
//			refundUser.setState(AppAuctionRefundConstans.AUCTION_REFUND_FAIL);
//		}
//		bbb.update(refundUser);
//	}


	public static void main(String[] args) throws IOException {
		Double dd = 1.00;
		System.out.println();

	}



}

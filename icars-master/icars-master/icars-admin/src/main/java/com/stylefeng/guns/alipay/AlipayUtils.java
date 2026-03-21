package com.stylefeng.guns.alipay;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayFundCouponOrderAgreementPayModel;
import com.alipay.api.domain.AlipayMarketingCampaignCashCreateModel;
import com.alipay.api.domain.AlipayMarketingCampaignCashDetailQueryModel;
import com.alipay.api.domain.AlipayMarketingCampaignCashTriggerModel;
import com.alipay.api.request.AlipayFundCouponOrderAgreementPayRequest;
import com.alipay.api.request.AlipayMarketingCampaignCashCreateRequest;
import com.alipay.api.request.AlipayMarketingCampaignCashDetailQueryRequest;
import com.alipay.api.request.AlipayMarketingCampaignCashTriggerRequest;
import com.alipay.api.response.AlipayFundCouponOrderAgreementPayResponse;
import com.alipay.api.response.AlipayMarketingCampaignCashCreateResponse;
import com.alipay.api.response.AlipayMarketingCampaignCashDetailQueryResponse;
import com.alipay.api.response.AlipayMarketingCampaignCashTriggerResponse;
import org.springframework.beans.factory.annotation.Value;

public class AlipayUtils {

    @Value("alipay.appid")
    private  static String APP_ID = "2018071960670475";

    @Value("alipay.public_key")
    private  static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtoC3dzpnS0bXjtfmBLUaK/87Q8fOJ5e8oMoHUkZSkro6KeLs9ARcbxsO1uZ3mATe/JQp4XFLxQG2UJLWinrleEfwPImPVvLi/0PqqwWR81I4uG72obQsMGLjy2dBoDmdj55+/QfjPg/0amOFEV2+9GOLs/U2xQpFA28F8SDJm1pKgg4jKIHItSR/M4JK4bZ+HrXOtm9fblY2HsEASBCft5mQ+3FgfPHHMXk7uSXlBbHW1H4MAAgoG+H7zPwu9xtTdbA6Td22TMte666oblniyHApo9STmOxUgY3dDB7agr5rEOtZAP1xF3Eup6g6wmgeq4bqiF6/62+ZcqL4VfuBqQIDAQAB";

    @Value("alipay.private_key")
    private  static String APP_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCYgAmcWH6WmfB7OK/m7TMG+e0CMrU2CkaQOAHIaaVj4vuNUCNnujkEdVl3X2GX6HaYjijqgfsiQNsW85BABn4ZGhOvmKtINhhRY6wTejZGPQjuLcD351ZlpVrvk2BkF0H4i5xi4Cgq7n0blmLAYQNQpUKtn7eKOd7Nvt+z+RTX2RxDrYk3KF3TCTe367U1zqqxVVqPbRqATe8YQABGdPOj6FCZ0qhKx00qR7+XIgZ94l9PjUVDv+ovaZFXt60u9RbeBebxJDwvJ4aQjOiOeVVGLjQ3K7mFQ9lzsMgss+a3wJJtE/P+wo6IvIDYqrWs+shQ3QTed3O4s7yxxKOF9UWRAgMBAAECggEAEkE5ZUq+lH6G5+koPUdllU8zjNDPGlHuknLx27CxyJwyKn9OTatgCeWWORDk2M9N6wMRaMj4a+hBkaTEPrXIQG77rkhjMHixPoTk40v81TmXKRO3BrXdhLtfgFAu+KxEDGGB7eARTtpPhY7h8U9rmyq9KZV13gkBX7Wah6uoUBIP05GqOiuZUvpc6onqVvae4z+1ZwkNRpX2f16Og7SYbs3AMh+xjOWCOrgtPrXmiVK+WUvwvMdX5DMlpeLXdGCewj240V1Z8Gvit1cBM1Rqaamh1hBtqA93FTQAqYyFq6eDKkSIx4/g+dYBHYvzK+oeORhFVbjYWAL1/9xtkC3GNQKBgQDzugPxp2eSJ66A6Dv9BHhZpwN4GefN0Kn30cANae6ibJLLIA8IGKMolqWe4vqLx+JuLCwL3kvsdyBGJsx3YaQVWNZCy138BUyj9bnUX6X5CVYov/GELC5/P+FHmt9buFJ/BO90DWngEqmwANrcWsRiNGtXdjX4aM6LnaZjTXWl/wKBgQCgLfuBcTcJ6bExafHNxD+1D/p5ZD5Qc1xmkroaZzVJb8gok2JTuCCU9GhFnU6QkTbOtbnaLz+P1FDvQ9RbQbaV766cgj+qUtjKzvrfX34qeI852Jfnmh2SLXHJa2Io5kO5nCsOrE7Gv7miKTHR81+slv6iecX/xWThn/dOhsW0bwKBgQC9yZbyoTIUbJQFS1ITN1zkKxqXdky8RwVjtdCAYYHytuskA2XRHyZGH/D+Ja+GE3Nt6TWvVD3499aI6gOD3uOfv9qwjp4z9oh0kI9RId4ja7LM7pKKIsFeU4cAdEgDqyeKQ3SXAS71znKWXEQRHRkZg8omhCWIt0xHCc8cOqAnTwKBgE6UACuR9KQiRlCtXnfI7E/QlNl0ObWRwiR3mmBVfSiXc6OkGbpkO2eXNofgEaiN9EsXjyLfxtiyGTonFkfmRsHrIMeahczyc3OaciueVvBS0EBaZrqxOgx5yrY6vZ6+xz6Iw/6gvMx3RYOtnckXETpDoIroHh/JICInLVUSfG51AoGBAMAt0YQdds6bO4HFM6Fu2RBLFS6pEi5Uze+Xio/6ije3VQJVUEEQKC/WvtSMtIV9R7L1gb/1GxnPPBAujFZkp6CZRSbTqX/F8InzvHLZz301me36AAGmxH06izxBfzns+KEGLh+DP6pdRPbPh2ZpPw1DQtJPqwyza6tkMF46oJod";

    public static void payRed(AlipayRequestEntity entity) throws AlipayApiException {

        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", APP_ID, APP_PRIVATE_KEY, "json", Constant.CHARSET_UTF8, ALIPAY_PUBLIC_KEY, "RSA2");
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.fund.coupon.order.agreement.pay
//        AlipayFundCouponOrderAgreementPayRequest request = new AlipayFundCouponOrderAgreementPayRequest();
//        AlipayFundCouponOrderAgreementPayModel model = new AlipayFundCouponOrderAgreementPayModel();
//        model.setOutOrderNo(entity.getOut_biz_no());
//        model.setOutRequestNo(entity.getOut_biz_no());
//        model.setOrderTitle("红包发送");
//        model.setPayerUserId("2088231020746935");
//        model.setAmount("0.1");
//        model.setPayTimeout("3m");


//         //创建红包活动
//        AlipayMarketingCampaignCashCreateRequest request = new AlipayMarketingCampaignCashCreateRequest();
//        AlipayMarketingCampaignCashCreateModel model = new AlipayMarketingCampaignCashCreateModel();
//        model.setCouponName("信息提供红包");
//        model.setPrizeType("fixed");
//        model.setTotalMoney("2");
//        model.setTotalNum("4");
//        model.setPrizeMsg("爱车士送您信息红包");
//        model.setStartTime("2018-07-31 00:00:00");
//        model.setEndTime("2018-08-02 00:00:00");
//        model.setSendFreqency("L100");

//        AlipayMarketingCampaignCashDetailQueryRequest request = new AlipayMarketingCampaignCashDetailQueryRequest();
//
//        AlipayMarketingCampaignCashDetailQueryModel model  = new AlipayMarketingCampaignCashDetailQueryModel();
//        model.setCrowdNo("iGHvcWDLepfKbSfn67Gt5O7VCBLKynb-Ul-JyL7bZZKsn4ge4wBRFkiLOjq1h1Jd");


        //触发红包接口
        AlipayMarketingCampaignCashTriggerRequest request = new AlipayMarketingCampaignCashTriggerRequest();
        AlipayMarketingCampaignCashTriggerModel model = new AlipayMarketingCampaignCashTriggerModel();
        model.setCrowdNo("6a78mQ1OnDWa96HwCWl1J0VOxMH4VZmMfRqEkZr9eUusn4ge4wBRFkiLOjq1h1Jd");
        model.setLoginId("472460679@qq.com");
        model.setOutBizNo(System.currentTimeMillis()+"");
        //查询红包活动接口
//        AlipayMarketingCampaignCashDetailQueryRequest request = new AlipayMarketingCampaignCashDetailQueryRequest();
//        AlipayMarketingCampaignCashDetailQueryModel model = new AlipayMarketingCampaignCashDetailQueryModel();
//        model.setCrowdNo("iGHvcWDLepfKbSfn67Gt5O7VCBLKynb-Ul-JyL7bZZKsn4ge4wBRFkiLOjq1h1Jd");


        request.setBizModel(model);

//        AlipayMarketingCampaignCashDetailQueryResponse response = alipayClient.execute(request);

        AlipayMarketingCampaignCashTriggerResponse response = alipayClient.execute(request);
//        System.out.print("dfdfdf");
//        AlipayMarketingCampaignCashCreateResponse response = alipayClient.execute(request);
//        AlipayMarketingCampaignCashDetailQueryResponse response = alipayClient.execute(request);
        //调用成功，则处理业务逻辑
        System.out.println(JSON.toJSONString(response));
        if(response.isSuccess()){

        }
    }


//    public static AlipayMarketingCampaignCashCreateResponse create(){
//
//    }

    public static void main(String[] args) {
        AlipayRequestEntity entity = new AlipayRequestEntity();
        entity.setOut_biz_no("8077735255938023");
        try {
            payRed(entity);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
    }
}

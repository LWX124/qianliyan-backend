package com.jeesite.modules.app.service;

import net.sf.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Component
public class PayWxAmountService {
    @Resource
    private RestTemplate restTemplate;

    //审核通过
    private static String pass = "https://chejiqiche.com/api/v1/app/checkSuccess";
    //审核不通过
    private static String failure = "https://chejiqiche.com/api/v1/app/checkFail";

    /*accdId  事故id       accdId
			amount  金额   amount
			reason  固定‘审核通过’  reason*/
    public String  passSta(String accdId, BigDecimal amount) {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("accdId", accdId);
        map.add("amount", amount);
        map.add("reason", "审核通过");
//        pass = "https://chejiqiche.com/api/v1/app/checkSuccess";
//        pass = pass + "?accdId=" + accdId + "&amount=" + amount + "&reason=审核通过";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(pass, request, String.class);
        String body = stringResponseEntity.getBody();
        JSONObject jsonObject = JSONObject.fromObject(body);
        com.alibaba.fastjson.JSONObject js = com.alibaba.fastjson.JSONObject.parseObject(jsonObject.toString());
        return js.getString("code");

    }

    /*审核不通过
        /api/v1/app/checkFail
        accdId
        reason  不通过原因 */
    public String falied(String accdId, String reason) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("accdId", accdId);
        map.add("reason", reason);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(failure, request, String.class);
        String body = stringResponseEntity.getBody();
        JSONObject jsonObject = JSONObject.fromObject(body);
        com.alibaba.fastjson.JSONObject js = com.alibaba.fastjson.JSONObject.parseObject(jsonObject.toString());
        return js.getString("code");
    }

}

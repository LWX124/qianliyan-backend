package com.jeesite.modules.app.service;

import com.jeesite.modules.job.service.PlusUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Component
public class CallPhoneService {
    private final static Logger logger = LoggerFactory.getLogger(CallPhoneService.class);

    @Resource
    private RestTemplate restTemplate;

    private static String url = "https://chejiqiche.com/api/v1/app/testVoice";

    public void callPhone(String name, String phone) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("name", name);
        map.add("phone", phone);
        map.add("paramPhone", null);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url, request, String.class);
        logger.error("打电话回调 #### stringResponseEntity={};name={};phone={}", stringResponseEntity,name,phone);

    }
}

package com.cheji.web.modular.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ViolationServer {

    //普通燃油车
    final String url1 = "https://route.showapi.com/862-1?" +
            "carType=02&" +
            "jgjId=&" +
            "showapi_appid=83879&" +
            "showapi_sign=e633551070b3441fbadc341105578d41&";

    //新能源汽车
    final String url2 = "http://route.showapi.com/862-4?" +
            "carType=52&" +
            "jgjId=&" +
            "showapi_appid=83879&" +
            "showapi_sign=e633551070b3441fbadc341105578d41&";

    public String queryViolation(String carCode, String carEngineCode, String carNumber, int type) {
        RestTemplate restTemplate = new RestTemplate();
        String urlString;
        if (type == 1) {
            urlString = url1 + "carNumber=" + carNumber + "&carCode=" + carCode + "&carEngineCode=" + carEngineCode;
        } else {
            urlString = url2 + "carNumber=" + carNumber + "&carCode=" + carCode + "&carEngineCode=" + carEngineCode;
        }
        return restTemplate.getForObject(urlString, String.class);
    }

}

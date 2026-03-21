package com.stylefeng.guns.modular.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.stylefeng.guns.modular.system.dao.BizClaimMapper;
import com.stylefeng.guns.modular.system.model.BizClaim;
import com.stylefeng.guns.modular.system.service.IBizClaimService;
import com.stylefeng.guns.modular.system.service.IWxService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 腾讯小程序API接口 实现类
 * </p>
 *
 * @author
 * @since 2018-08-27
 */
@Service(value = "wxServiceImpl")
public class WxServiceImpl implements IWxService {

    @Value("${wx.appId}")
    private String appid;

    @Value("${wx.secret}")
    private String secret;

    @Resource
    private RestTemplate restTemplate;


    @Override
    public String getAccessToken() {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+secret;
        ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);
        String result = entity.getBody();
        return  JSON.parseObject(result).get("access_token").toString();
    }

    @Override
    public byte[] getMiniqrQr(String sceneStr) {
        byte[] result;
        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="+getAccessToken();
        Map<String,Object> param = new HashMap<>();
        param.put("scene", sceneStr);
        param.put("page", "pages/index/index");
        param.put("width", 430);
        param.put("auto_color", false);
        Map<String,Object> line_color = new HashMap<>();
        line_color.put("r", 0);
        line_color.put("g", 0);
        line_color.put("b", 0);
        param.put("line_color", line_color);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        HttpEntity requestEntity = new HttpEntity(param, headers);
        ResponseEntity<byte[]> entity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, byte[].class, new Object[0]);
        result = entity.getBody();
        return result;
    }
}

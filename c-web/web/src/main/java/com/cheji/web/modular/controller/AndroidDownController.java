package com.cheji.web.modular.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheji.web.modular.domain.AppVersionEntity;
import com.cheji.web.modular.mapper.AppVersionMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 安卓下载安装包
 */
@RestController
@RequestMapping("/android")
public class AndroidDownController {

    @Resource
    private AppVersionMapper appVersionMapper;

    @ApiOperation(value = "检查版本号")
    @RequestMapping(value = "/checkVersion", method = RequestMethod.GET)
    public JSONObject addVideo() {
        JSONObject result = new JSONObject();
        AppVersionEntity appVersionEntity = appVersionMapper.selectNewsData();
        //版本号  版本名  版本介绍
//        int versionNumMin = 0;
//        int versionNumMax = 1;
//        String versionName = "1.0.0";
//        String info = "";
//        String href = "https://obs-4f4a.obs.cn-southwest-2.myhuaweicloud.com/system/c.apk";
        JSONObject out = new JSONObject();
        out.put("versionNumMin", appVersionEntity.getVersionNumMin());
        out.put("versionNumMax", appVersionEntity.getVersionNumMax());
        out.put("versionName", appVersionEntity.getVersionName());
        out.put("info", appVersionEntity.getInfo());
        out.put("href", appVersionEntity.getHref());

        result.put("code", 200);
        result.put("data", out);
        return result;
    }
}

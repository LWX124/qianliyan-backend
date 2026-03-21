/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.jeesite.modules.app.dao.BizAccidentDao;
import com.jeesite.modules.app.entity.AppVideo;
import com.jeesite.modules.app.entity.BizWxUser;
import com.jeesite.modules.app.service.AppVideoService;
import com.jeesite.modules.app.service.BizWxUserService;
import com.jeesite.modules.constant2.AppConstants;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.app.entity.BizAccident;
import com.jeesite.modules.app.service.BizAccidentService;

import java.util.Date;

/**
 * 事故上报信息表Controller
 *
 * @author zcq
 * @version 2019-09-24
 */
@Controller
@RequestMapping(value = "${adminPath}/app/bizAccident")
public class BizAccidentController extends BaseController {

    @Autowired
    private BizAccidentService bizAccidentService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private AppVideoService appVideoService;

    @Resource
    private BizWxUserService bizWxUserService;

    /**
     * 获取数据
     */
    @ModelAttribute
    public BizAccident get(Long id, boolean isNewRecord) {
        return bizAccidentService.get(String.valueOf(id), isNewRecord);
    }

    /**
     * 查询列表
     */
    @RequiresPermissions("app:bizAccident:view")
    @RequestMapping(value = {"list", ""})
    public String list(BizAccident bizAccident, Model model) {
        model.addAttribute("bizAccident", bizAccident);
        return "modules/app/bizAccidentList";
    }

    /**
     * 查询列表数据
     */
    @RequiresPermissions("app:bizAccident:view")
    @RequestMapping(value = "listData")
    @ResponseBody
    public Page<BizAccident> listData(BizAccident bizAccident, HttpServletRequest request, HttpServletResponse response) {
        bizAccident.setPage(new Page<>(request, response));
        Page<BizAccident> page = bizAccidentService.findPage(bizAccident);
        return page;
    }

    /**
     * 查看编辑表单
     */
    @RequiresPermissions("app:bizAccident:view")
    @RequestMapping(value = "form")
    public String form(BizAccident bizAccident, Model model) {
        model.addAttribute("bizAccident", bizAccident);
        return "modules/app/bizAccidentForm";
    }

    /**
     * 保存事故上报信息表
     */
    @RequiresPermissions("app:bizAccident:edit")
    @PostMapping(value = "save")
    @ResponseBody
    public String save(@Validated BizAccident bizAccident) {
        bizAccidentService.save(bizAccident);
        return renderResult(Global.TRUE, text("保存事故上报信息表成功！"));
    }

    /**
     * 删除事故上报信息表
     */
    @RequiresPermissions("app:bizAccident:edit")
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(BizAccident bizAccident) {
        bizAccidentService.delete(bizAccident);
        return renderResult(Global.TRUE, text("删除事故上报信息表成功！"));
    }

    @RequestMapping(value = "addAppform")
    @ResponseBody
    @Transactional
    public String addAppform(String id) {
        BizAccident bizAccident = bizAccidentService.get(id);
        //判断是否添加到视频表中
        if (null == bizAccident.getIsaddvideo() || 0 == bizAccident.getIsaddvideo()) {
            //先查询有无数据
            String bizAccidentId = bizAccident.getId();
            AppVideo videoByAccid = appVideoService.findVideoByAccid(bizAccidentId);
            //数据为空就是第一次添加
            if (videoByAccid==null){
                AppVideo appVideo =  bizAccidentService.saveApp(id);
                String id1 = appVideo.getId();
                AppVideo appVideo2 = appVideoService.get(id1);
                String userId = appVideo2.getUserId();
                BizWxUser byOpenid = bizWxUserService.findByOpenid(userId);
                String wxname = byOpenid.getWxname();
                String headimg = byOpenid.getHeadimg();
                appVideo2.setAvatar(headimg);
                appVideo2.setName(wxname);
                Date creatTime = appVideo2.getCreatTime();
                long time = creatTime.getTime();
                JSONObject in = new JSONObject();
                in.put("videoId",appVideo2.getId());
                in.put("url", appVideo2.getUrl());
                in.put("userId", appVideo2.getUserId());
                in.put("count", appVideo2.getCount());
                in.put("share", appVideo2.getShare());
                in.put("appViewCounts", appVideo2.getAppViewCounts());
                in.put("accidentId", appVideo2.getAccidentId());
                in.put("creatTime", appVideo2.getCreatTime());
                in.put("name", appVideo2.getName());
                in.put("introduce", appVideo2.getIntroduce());
                in.put("avatar", appVideo2.getAvatar());
                in.put("address", appVideo2.getAddress());
                in.put("isPay", appVideo2.getIsPay());
                redisTemplate.opsForZSet().add(AppConstants.APP_VIDEO_LIST, in.toJSONString(), time);
            }else {
                //不为空就是第三次修改，修改是否展示到app中
                bizAccident.setIsaddvideo(1);
                bizAccidentService.update(bizAccident);
                videoByAccid.setAppShowFalg(1);
                String userId = videoByAccid.getUserId();
                BizWxUser bizWxUser = bizWxUserService.findByOpenid(userId);
                String wxname = bizWxUser.getWxname();
                String headimg = bizWxUser.getHeadimg();
                videoByAccid.setAvatar(headimg);
                videoByAccid.setName(wxname);
                //又添加到缓存中
                Date creatTime = videoByAccid.getCreatTime();
                long time = creatTime.getTime();
                JSONObject in = new JSONObject();
                in.put("videoId", videoByAccid.getId());
                in.put("url", videoByAccid.getUrl());
                in.put("userId", videoByAccid.getUserId());
                in.put("count", videoByAccid.getCount());
                in.put("share", videoByAccid.getShare());
                in.put("appViewCounts", videoByAccid.getAppViewCounts());
                in.put("accidentId", videoByAccid.getAccidentId());
                in.put("creatTime", videoByAccid.getCreatTime());
                in.put("name", videoByAccid.getName());
                in.put("introduce", videoByAccid.getIntroduce());
                in.put("avatar", videoByAccid.getAvatar());
                in.put("address", videoByAccid.getAddress());
                in.put("isPay", videoByAccid.getIsPay());
                //添加到缓存中
                redisTemplate.opsForZSet().add(AppConstants.APP_VIDEO_LIST, in.toJSONString(), time);
            }
            //拿到事故id吧视频放到app表中
        }else {
            //如果一级添加，修改状态，不在app中展示
            //根据事故id查询到视频表中信息
            String bizAccidentId = bizAccident.getId();
            AppVideo videoByAccid = appVideoService.findVideoByAccid(bizAccidentId);
            String userId = videoByAccid.getUserId();
            BizWxUser bizWxUser = bizWxUserService.findByOpenid(userId);

            String wxname = bizWxUser.getWxname();
            if (wxname==null){
                wxname=null;
            }
            String headimg = bizWxUser.getHeadimg();
            if (headimg==null){
                headimg=null;
            }
            videoByAccid.setAvatar(headimg);
            videoByAccid.setName(wxname);
            //在redis里面移除数据
            JSONObject in = new JSONObject();
            in.put("videoId", videoByAccid.getId());
            in.put("url", videoByAccid.getUrl());
            in.put("userId", videoByAccid.getUserId());
            in.put("count", videoByAccid.getCount());
            in.put("share", videoByAccid.getShare());
            in.put("appViewCounts", videoByAccid.getAppViewCounts());
            in.put("accidentId", videoByAccid.getAccidentId());
            in.put("creatTime", videoByAccid.getCreatTime());
            in.put("name", videoByAccid.getName());
            in.put("introduce", videoByAccid.getIntroduce());
            in.put("avatar", videoByAccid.getAvatar());
            in.put("address", videoByAccid.getAddress());
            in.put("isPay", videoByAccid.getIsPay());
            redisTemplate.opsForZSet().remove(AppConstants.APP_VIDEO_LIST, in.toJSONString());

            videoByAccid.setAppShowFalg(0);
            //修改视频表中字段
            appVideoService.update(videoByAccid);
            bizAccident.setIsaddvideo(0);
            bizAccidentService.update(bizAccident);

            return renderResult(Global.TRUE, text(" 取消在app中展示！"));
        }
        return renderResult(Global.TRUE, text("成功添加到app视频中！"));
    }
}
/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.app.entity.AppVideo;
import com.jeesite.modules.app.excep.CusException;
import com.jeesite.modules.app.service.AppVideoService;
import com.jeesite.modules.util2.StringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;

/**
 * 视频信息表Controller
 *
 * @author zcq
 * @version 2019-08-01
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appVideo")
public class AppVideoController extends BaseController {


    @Autowired
    private AppVideoService appVideoService;

    /**
     * 获取数据
     */
    @ModelAttribute
    public AppVideo get(Integer id, boolean isNewRecord) {
        return appVideoService.get(String.valueOf(id), isNewRecord);
    }

    /**
     * 查询列表
     */
    @RequiresPermissions("app:appVideo:view")
    @RequestMapping(value = {"list", ""})
    public String list(AppVideo appVideo, Model model) {
        model.addAttribute("appVideo", appVideo);
        return "modules/app/appVideoList";
    }

    /**
     * 查询列表数据
     */
    @RequiresPermissions("app:appVideo:view")
    @RequestMapping(value = "listData")
    @ResponseBody
    public Page<AppVideo> listData(AppVideo appVideo, HttpServletRequest request, HttpServletResponse response) {
        appVideo.setPage(new Page<>(request, response));
        Page<AppVideo> page = appVideoService.findPage(appVideo);
        return page;
    }

    /**
     * 查看编辑表单
     */
    @RequiresPermissions("app:appVideo:view")
    @RequestMapping(value = "form")
    public String form(AppVideo appVideo, Model model) {
        model.addAttribute("appVideo", appVideo);
        return "modules/app/appVideoForm";
    }

    /**
     * 保存视频信息表
     */
    @RequiresPermissions("app:appVideo:edit")
    @PostMapping(value = "save")
    @ResponseBody
    public String save(@Validated AppVideo appVideo) {
        if (appVideo.getAppShowFalg() == 1) {
            appVideo.setAppShowFalg(0);
        } else if (appVideo.getAppShowFalg() == 0) {
            appVideo.setAppShowFalg(1);
        }
        appVideoService.save(appVideo);
        return renderResult(Global.TRUE, text("保存视频信息表成功！"));
    }

    /**
     * 设置该视频为首页视频
     */
    @RequiresPermissions("app:appVideo:edit")
    @RequestMapping("upvideo")
    public String upVideo(String ids) {
        if (StringUtils.isEmpty(ids)) {
            return renderResult(Global.FALSE, text("id不能为空！"));
        }
        try {
            appVideoService.upVideo(ids);
        } catch (CusException e) {
            return renderResult(Global.FALSE, text("设置该视频为首页视频失败！"));
        }
        return renderResult(Global.TRUE, text("设置该视频为首页视频成功！"));
    }

    /**
     * 删除视频信息表
     */
    @RequiresPermissions("app:appVideo:edit")
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(AppVideo appVideo) {
        appVideoService.delete(appVideo);
        return renderResult(Global.TRUE, text("删除视频信息表成功！"));
    }

}

/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.app.entity.AppVersion;
import com.jeesite.modules.app.service.AppVersionService;

/**
 * app_versionController
 *
 * @author dh
 * @version 2019-11-18
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appVersion")
public class AppVersionController extends BaseController {

    @Autowired
    private AppVersionService appVersionService;

    /**
     * 获取数据
     */
    @ModelAttribute
    public AppVersion get(Long id, boolean isNewRecord) {
        return appVersionService.get(id + "", isNewRecord);
    }

    /**
     * 查询列表
     */
    @RequiresPermissions("app:appVersion:view")
    @RequestMapping(value = {"list", ""})
    public String list(AppVersion appVersion, Model model) {
        model.addAttribute("appVersion", appVersion);
        return "modules/app/appVersionList";
    }

    /**
     * 查询列表数据
     */
    @RequiresPermissions("app:appVersion:view")
    @RequestMapping(value = "listData")
    @ResponseBody
    public Page<AppVersion> listData(AppVersion appVersion, HttpServletRequest request, HttpServletResponse response) {
        appVersion.setPage(new Page<>(request, response));
        Page<AppVersion> page = appVersionService.findPage(appVersion);
        return page;
    }

    /**
     * 查看编辑表单
     */
    @RequiresPermissions("app:appVersion:view")
    @RequestMapping(value = "form")
    public String form(AppVersion appVersion, Model model) {
        model.addAttribute("appVersion", appVersion);
        return "modules/app/appVersionForm";
    }

    /**
     * 保存app_version
     */
    @RequiresPermissions("app:appVersion:edit")
    @PostMapping(value = "save")
    @ResponseBody
    public String save(@Validated AppVersion appVersion) {
        appVersionService.save(appVersion);
        return renderResult(Global.TRUE, text("保存app_version成功！"));
    }

}
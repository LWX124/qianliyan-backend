/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.app.entity.AppUserAccountRecord;
import com.jeesite.modules.app.service.AppUserAccountRecordService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户金额记录表Controller
 *
 * @author dh
 * @version 2019-10-31
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appUserAccountRecord")
public class AppUserAccountRecordController extends BaseController {

    @Autowired
    private AppUserAccountRecordService appUserAccountRecordService;

    /**
     * 获取数据
     */
    @ModelAttribute
    public AppUserAccountRecord get(Long id, boolean isNewRecord) {
        return appUserAccountRecordService.get(id + "", isNewRecord);
    }

    /**
     * 查询列表
     */
    @RequiresPermissions("app:appUserAccountRecord:view")
    @RequestMapping(value = {"list", ""})
    public String list(AppUserAccountRecord appUserAccountRecord, Model model) {
        model.addAttribute("appUserAccountRecord", appUserAccountRecord);
        return "modules/app/appUserAccountRecordList";
    }

    /**
     * 查询列表数据
     */
    @RequiresPermissions("app:appUserAccountRecord:view")
    @RequestMapping(value = "listData")
    @ResponseBody
    public Page<AppUserAccountRecord> listData(AppUserAccountRecord appUserAccountRecord, HttpServletRequest request, HttpServletResponse response) {
        appUserAccountRecord.setPage(new Page<>(request, response));
        Page<AppUserAccountRecord> page = appUserAccountRecordService.findPage(appUserAccountRecord);
        return page;
    }

    /**
     * 查看编辑表单
     */
    @RequiresPermissions("app:appUserAccountRecord:view")
    @RequestMapping(value = "form")
    public String form(AppUserAccountRecord appUserAccountRecord, Model model) {
        model.addAttribute("appUserAccountRecord", appUserAccountRecord);
        return "modules/app/appUserAccountRecordForm";
    }

    /**
     * 保存用户金额记录表
     */
    @RequiresPermissions("app:appUserAccountRecord:edit")
    @PostMapping(value = "save")
    @ResponseBody
    public String save(@Validated AppUserAccountRecord appUserAccountRecord) {
        appUserAccountRecordService.save(appUserAccountRecord);
        return renderResult(Global.TRUE, text("保存用户金额记录表成功！"));
    }

}
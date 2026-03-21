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
import com.jeesite.modules.app.entity.AppWxBank;
import com.jeesite.modules.app.service.AppWxBankService;

/**
 * app_wx_bankController
 *
 * @author dh
 * @version 2019-10-14
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appWxBank")
public class AppWxBankController extends BaseController {

    @Autowired
    private AppWxBankService appWxBankService;

    /**
     * 获取数据
     */
    @ModelAttribute
    public AppWxBank get(Long id, boolean isNewRecord) {
        return appWxBankService.get(id + "", isNewRecord);
    }

    /**
     * 查询列表
     */
    @RequiresPermissions("app:appWxBank:view")
    @RequestMapping(value = {"list", ""})
    public String list(AppWxBank appWxBank, Model model) {
        model.addAttribute("appWxBank", appWxBank);
        return "modules/app/appWxBankList";
    }

    /**
     * 查询列表数据
     */
    @RequiresPermissions("app:appWxBank:view")
    @RequestMapping(value = "listData")
    @ResponseBody
    public Page<AppWxBank> listData(AppWxBank appWxBank, HttpServletRequest request, HttpServletResponse response) {
        appWxBank.setPage(new Page<>(request, response));
        Page<AppWxBank> page = appWxBankService.findPage(appWxBank);
        return page;
    }

    /**
     * 查看编辑表单
     */
    @RequiresPermissions("app:appWxBank:view")
    @RequestMapping(value = "form")
    public String form(AppWxBank appWxBank, Model model) {
        model.addAttribute("appWxBank", appWxBank);
        return "modules/app/appWxBankForm";
    }

    /**
     * 保存微信银行卡编码
     */
    @RequiresPermissions("app:appWxBank:edit")
    @PostMapping(value = "save")
    @ResponseBody
    public String save(@Validated AppWxBank appWxBank) {
        appWxBankService.save(appWxBank);
        return renderResult(Global.TRUE, text("保存微信银行卡编码成功！"));
    }

    /**
     * 删除微信银行卡编码
     */
    @RequiresPermissions("app:appWxBank:edit")
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(AppWxBank appWxBank) {
        appWxBankService.delete(appWxBank);
        return renderResult(Global.TRUE, text("删除微信银行卡编码成功！"));
    }

}
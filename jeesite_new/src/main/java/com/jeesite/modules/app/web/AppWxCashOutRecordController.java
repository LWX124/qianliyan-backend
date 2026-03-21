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
import com.jeesite.modules.app.entity.AppWxCashOutRecord;
import com.jeesite.modules.app.service.AppWxCashOutRecordService;

/**
 * 提现记录表Controller
 *
 * @author dh
 * @version 2019-10-08
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appWxCashOutRecord")
public class AppWxCashOutRecordController extends BaseController {

    @Autowired
    private AppWxCashOutRecordService appWxCashOutRecordService;

    /**
     * 获取数据
     */
    @ModelAttribute
    public AppWxCashOutRecord get(Long id, boolean isNewRecord) {
        return appWxCashOutRecordService.get(id + "", isNewRecord);
    }

    /**
     * 查询列表
     */
    @RequiresPermissions("app:appWxCashOutRecord:view")
    @RequestMapping(value = {"list", ""})
    public String list(AppWxCashOutRecord appWxCashOutRecord, Model model) {
        model.addAttribute("appWxCashOutRecord", appWxCashOutRecord);
        return "modules/app/appWxCashOutRecordList";
    }

    /**
     * 查询列表数据
     */
    @RequiresPermissions("app:appWxCashOutRecord:view")
    @RequestMapping(value = "listData")
    @ResponseBody
    public Page<AppWxCashOutRecord> listData(AppWxCashOutRecord appWxCashOutRecord, HttpServletRequest request, HttpServletResponse response) {
        appWxCashOutRecord.setPage(new Page<>(request, response));
        Page<AppWxCashOutRecord> page = appWxCashOutRecordService.findPage(appWxCashOutRecord);
        return page;
    }

    /**
     * 查看编辑表单
     */
    @RequiresPermissions("app:appWxCashOutRecord:view")
    @RequestMapping(value = "form")
    public String form(AppWxCashOutRecord appWxCashOutRecord, Model model) {
        model.addAttribute("appWxCashOutRecord", appWxCashOutRecord);
        return "modules/app/appWxCashOutRecordForm";
    }

    /**
     * 保存提现记录表
     */
    @RequiresPermissions("app:appWxCashOutRecord:edit")
    @PostMapping(value = "save")
    @ResponseBody
    public String save(@Validated AppWxCashOutRecord appWxCashOutRecord) {
        appWxCashOutRecordService.save(appWxCashOutRecord);
        return renderResult(Global.TRUE, text("保存提现记录表成功！"));
    }

    /**
     * 删除提现记录表
     */
    @RequiresPermissions("app:appWxCashOutRecord:edit")
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(AppWxCashOutRecord appWxCashOutRecord) {
        appWxCashOutRecordService.delete(appWxCashOutRecord);
        return renderResult(Global.TRUE, text("删除提现记录表成功！"));
    }

}
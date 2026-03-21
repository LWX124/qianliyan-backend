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
import com.jeesite.modules.app.entity.AppUserBMessage;
import com.jeesite.modules.app.service.AppUserBMessageService;

/**
 * 用户服务信息表Controller
 * @author zcq
 * @version 2020-06-22
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appUserBMessage")
public class AppUserBMessageController extends BaseController {

	@Autowired
	private AppUserBMessageService appUserBMessageService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public AppUserBMessage get(Long id, boolean isNewRecord) {
		return appUserBMessageService.get(String.valueOf(id), isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("app:appUserBMessage:view")
	@RequestMapping(value = {"list", ""})
	public String list(AppUserBMessage appUserBMessage, Model model) {
		model.addAttribute("appUserBMessage", appUserBMessage);
		return "modules/app/appUserBMessageList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("app:appUserBMessage:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AppUserBMessage> listData(AppUserBMessage appUserBMessage, HttpServletRequest request, HttpServletResponse response) {
		appUserBMessage.setPage(new Page<>(request, response));
		Page<AppUserBMessage> page = appUserBMessageService.findPage(appUserBMessage);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("app:appUserBMessage:view")
	@RequestMapping(value = "form")
	public String form(AppUserBMessage appUserBMessage, Model model) {
		model.addAttribute("appUserBMessage", appUserBMessage);
		return "modules/app/appUserBMessageForm";
	}

	/**
	 * 保存用户服务信息表
	 */
	@RequiresPermissions("app:appUserBMessage:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated AppUserBMessage appUserBMessage) {
		appUserBMessageService.save(appUserBMessage);
		return renderResult(Global.TRUE, text("保存用户服务信息表成功！"));
	}
	
	/**
	 * 删除用户服务信息表
	 */
	@RequiresPermissions("app:appUserBMessage:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(AppUserBMessage appUserBMessage) {
		appUserBMessageService.delete(appUserBMessage);
		return renderResult(Global.TRUE, text("删除用户服务信息表成功！"));
	}
	
}
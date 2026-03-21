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
import com.jeesite.modules.app.entity.AppAuctionMessageIdentify;
import com.jeesite.modules.app.service.AppAuctionMessageIdentifyService;

/**
 * 认证信息Controller
 * @author dh
 * @version 2023-04-22
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appAuctionMessageIdentify")
public class AppAuctionMessageIdentifyController extends BaseController {

	@Autowired
	private AppAuctionMessageIdentifyService appAuctionMessageIdentifyService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public AppAuctionMessageIdentify get(Long id, boolean isNewRecord) {
		return appAuctionMessageIdentifyService.get(id.toString(), isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("app:appAuctionMessageIdentify:view")
	@RequestMapping(value = {"list", ""})
	public String list(AppAuctionMessageIdentify appAuctionMessageIdentify, Model model) {
		model.addAttribute("appAuctionMessageIdentify", appAuctionMessageIdentify);
		return "modules/app/appAuctionMessageIdentifyList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("app:appAuctionMessageIdentify:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AppAuctionMessageIdentify> listData(AppAuctionMessageIdentify appAuctionMessageIdentify, HttpServletRequest request, HttpServletResponse response) {
		appAuctionMessageIdentify.setPage(new Page<>(request, response));
		Page<AppAuctionMessageIdentify> page = appAuctionMessageIdentifyService.findPage(appAuctionMessageIdentify);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("app:appAuctionMessageIdentify:view")
	@RequestMapping(value = "form")
	public String form(AppAuctionMessageIdentify appAuctionMessageIdentify, Model model) {
		model.addAttribute("appAuctionMessageIdentify", appAuctionMessageIdentify);
		return "modules/app/appAuctionMessageIdentifyForm";
	}

	/**
	 * 保存认证信息
	 */
	@RequiresPermissions("app:appAuctionMessageIdentify:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated AppAuctionMessageIdentify appAuctionMessageIdentify) {
		appAuctionMessageIdentifyService.save(appAuctionMessageIdentify);
		return renderResult(Global.TRUE, text("保存认证信息成功！"));
	}
	
	/**
	 * 删除认证信息
	 */
	@RequiresPermissions("app:appAuctionMessageIdentify:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(AppAuctionMessageIdentify appAuctionMessageIdentify) {
		appAuctionMessageIdentifyService.delete(appAuctionMessageIdentify);
		return renderResult(Global.TRUE, text("删除认证信息成功！"));
	}
	
}
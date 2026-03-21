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
import com.jeesite.modules.app.entity.AppAuctionPayLog;
import com.jeesite.modules.app.service.AppAuctionPayLogService;

/**
 * 支付记录 vip充值Controller
 * @author dh
 * @version 2023-04-13
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appAuctionPayLog")
public class AppAuctionPayLogController extends BaseController {

	@Autowired
	private AppAuctionPayLogService appAuctionPayLogService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public AppAuctionPayLog get(String outTradeNo, boolean isNewRecord) {
		return appAuctionPayLogService.get(outTradeNo, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("app:appAuctionPayLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(AppAuctionPayLog appAuctionPayLog, Model model) {
		model.addAttribute("appAuctionPayLog", appAuctionPayLog);
		return "modules/app/appAuctionPayLogList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("app:appAuctionPayLog:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AppAuctionPayLog> listData(AppAuctionPayLog appAuctionPayLog, HttpServletRequest request, HttpServletResponse response) {
		appAuctionPayLog.setPage(new Page<>(request, response));
		Page<AppAuctionPayLog> page = appAuctionPayLogService.findPage(appAuctionPayLog);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("app:appAuctionPayLog:view")
	@RequestMapping(value = "form")
	public String form(AppAuctionPayLog appAuctionPayLog, Model model) {
		model.addAttribute("appAuctionPayLog", appAuctionPayLog);
		return "modules/app/appAuctionPayLogForm";
	}

	/**
	 * 保存支付记录 vip充值
	 */
	@RequiresPermissions("app:appAuctionPayLog:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated AppAuctionPayLog appAuctionPayLog) {
		appAuctionPayLogService.save(appAuctionPayLog);
		return renderResult(Global.TRUE, text("保存支付记录 vip充值成功！"));
	}
	
	/**
	 * 删除支付记录 vip充值
	 */
	@RequiresPermissions("app:appAuctionPayLog:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(AppAuctionPayLog appAuctionPayLog) {
		appAuctionPayLogService.delete(appAuctionPayLog);
		return renderResult(Global.TRUE, text("删除支付记录 vip充值成功！"));
	}
	
}
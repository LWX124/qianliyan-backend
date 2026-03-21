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
import com.jeesite.modules.app.entity.AppAuctionBidLog;
import com.jeesite.modules.app.service.AppAuctionBidLogService;

/**
 * 出价记录Controller
 * @author y
 * @version 2023-05-07
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appAuctionBidLog")
public class AppAuctionBidLogController extends BaseController {

	@Autowired
	private AppAuctionBidLogService appAuctionBidLogService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public AppAuctionBidLog get(Long id, boolean isNewRecord) {
		return appAuctionBidLogService.get(String.valueOf(id), isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("app:appAuctionBidLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(AppAuctionBidLog appAuctionBidLog, Model model) {
		model.addAttribute("appAuctionBidLog", appAuctionBidLog);
		return "modules/app/appAuctionBidLogList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("app:appAuctionBidLog:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AppAuctionBidLog> listData(AppAuctionBidLog appAuctionBidLog, HttpServletRequest request, HttpServletResponse response) {
		appAuctionBidLog.setPage(new Page<>(request, response));
		Page<AppAuctionBidLog> page = appAuctionBidLogService.findPage(appAuctionBidLog);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("app:appAuctionBidLog:view")
	@RequestMapping(value = "form")
	public String form(AppAuctionBidLog appAuctionBidLog, Model model) {
		model.addAttribute("appAuctionBidLog", appAuctionBidLog);
		return "modules/app/appAuctionBidLogForm";
	}

	/**
	 * 保存出价记录
	 */
	@RequiresPermissions("app:appAuctionBidLog:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated AppAuctionBidLog appAuctionBidLog) {
		appAuctionBidLogService.save(appAuctionBidLog);
		return renderResult(Global.TRUE, text("保存出价记录成功！"));
	}
	
	/**
	 * 删除出价记录
	 */
	@RequiresPermissions("app:appAuctionBidLog:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(AppAuctionBidLog appAuctionBidLog) {
		appAuctionBidLogService.delete(appAuctionBidLog);
		return renderResult(Global.TRUE, text("删除出价记录成功！"));
	}
	
}
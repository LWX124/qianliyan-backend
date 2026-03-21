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
import com.jeesite.modules.app.entity.AppAuctionOnePriceCarLog;
import com.jeesite.modules.app.service.AppAuctionOnePriceCarLogService;

/**
 * 一口价车辆支付记录Controller
 * @author dh
 * @version 2023-04-16
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appAuctionOnePriceCarLog")
public class AppAuctionOnePriceCarLogController extends BaseController {

	@Autowired
	private AppAuctionOnePriceCarLogService appAuctionOnePriceCarLogService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public AppAuctionOnePriceCarLog get(Long id, boolean isNewRecord) {
		return appAuctionOnePriceCarLogService.get(String.valueOf(id), isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("app:appAuctionOnePriceCarLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(AppAuctionOnePriceCarLog appAuctionOnePriceCarLog, Model model) {
		model.addAttribute("appAuctionOnePriceCarLog", appAuctionOnePriceCarLog);
		return "modules/app/appAuctionOnePriceCarLogList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("app:appAuctionOnePriceCarLog:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AppAuctionOnePriceCarLog> listData(AppAuctionOnePriceCarLog appAuctionOnePriceCarLog, HttpServletRequest request, HttpServletResponse response) {
		appAuctionOnePriceCarLog.setPage(new Page<>(request, response));
		Page<AppAuctionOnePriceCarLog> page = appAuctionOnePriceCarLogService.findPage(appAuctionOnePriceCarLog);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("app:appAuctionOnePriceCarLog:view")
	@RequestMapping(value = "form")
	public String form(AppAuctionOnePriceCarLog appAuctionOnePriceCarLog, Model model) {
		model.addAttribute("appAuctionOnePriceCarLog", appAuctionOnePriceCarLog);
		return "modules/app/appAuctionOnePriceCarLogForm";
	}

	/**
	 * 保存一口价车辆支付记录
	 */
	@RequiresPermissions("app:appAuctionOnePriceCarLog:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated AppAuctionOnePriceCarLog appAuctionOnePriceCarLog) {
		appAuctionOnePriceCarLogService.save(appAuctionOnePriceCarLog);
		return renderResult(Global.TRUE, text("保存一口价车辆支付记录成功！"));
	}
	
	/**
	 * 删除一口价车辆支付记录
	 */
	@RequiresPermissions("app:appAuctionOnePriceCarLog:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(AppAuctionOnePriceCarLog appAuctionOnePriceCarLog) {
		appAuctionOnePriceCarLogService.delete(appAuctionOnePriceCarLog);
		return renderResult(Global.TRUE, text("删除一口价车辆支付记录成功！"));
	}
	
}
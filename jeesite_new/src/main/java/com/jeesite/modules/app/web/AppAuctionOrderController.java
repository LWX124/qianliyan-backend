/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
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
import com.jeesite.modules.app.entity.AppAuctionOrder;
import com.jeesite.modules.app.service.AppAuctionOrderService;

/**
 * 拍卖订单表Controller
 * @author y
 * @version 2023-03-12
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appAuctionOrder")
public class AppAuctionOrderController extends BaseController {

	@Autowired
	private AppAuctionOrderService appAuctionOrderService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public AppAuctionOrder get(Long id, boolean isNewRecord) {
		return appAuctionOrderService.get(String.valueOf(id), isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("app:appAuctionOrder:view")
	@RequestMapping(value = {"list", ""})
	public String list(AppAuctionOrder appAuctionOrder, Model model) {
		model.addAttribute("appAuctionOrder", appAuctionOrder);
		return "modules/app/appAuctionOrderList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("app:appAuctionOrder:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AppAuctionOrder> listData(AppAuctionOrder appAuctionOrder, HttpServletRequest request, HttpServletResponse response) {
		appAuctionOrder.setPage(new Page<>(request, response));
		Page<AppAuctionOrder> page = appAuctionOrderService.findPage(appAuctionOrder);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("app:appAuctionOrder:view")
	@RequestMapping(value = "form")
	public String form(AppAuctionOrder appAuctionOrder, Model model) {
		model.addAttribute("appAuctionOrder", appAuctionOrder);
		return "modules/app/appAuctionOrderForm";
	}

	/**
	 * 保存拍卖订单表
	 */
	@RequiresPermissions("app:appAuctionOrder:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated AppAuctionOrder appAuctionOrder) {
		appAuctionOrderService.save(appAuctionOrder);
		return renderResult(Global.TRUE, text("保存拍卖订单表成功！"));
	}
	
}
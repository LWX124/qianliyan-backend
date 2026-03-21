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
import com.jeesite.modules.app.entity.AppCleanPriceDetail;
import com.jeesite.modules.app.service.AppCleanPriceDetailService;

/**
 * 商户清洗价格明细表Controller
 * @author zcq
 * @version 2019-12-30
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appCleanPriceDetail")
public class AppCleanPriceDetailController extends BaseController {

	@Autowired
	private AppCleanPriceDetailService appCleanPriceDetailService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public AppCleanPriceDetail get(Long id, boolean isNewRecord) {
		return appCleanPriceDetailService.get(String.valueOf(id), isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("app:appCleanPriceDetail:view")
	@RequestMapping(value = {"list", ""})
	public String list(AppCleanPriceDetail appCleanPriceDetail, Model model) {
		model.addAttribute("appCleanPriceDetail", appCleanPriceDetail);
		return "modules/app/appCleanPriceDetailList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("app:appCleanPriceDetail:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AppCleanPriceDetail> listData(AppCleanPriceDetail appCleanPriceDetail, HttpServletRequest request, HttpServletResponse response) {
		appCleanPriceDetail.setPage(new Page<>(request, response));
		Page<AppCleanPriceDetail> page = appCleanPriceDetailService.findPage(appCleanPriceDetail);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("app:appCleanPriceDetail:view")
	@RequestMapping(value = "form")
	public String form(AppCleanPriceDetail appCleanPriceDetail, Model model) {
		model.addAttribute("appCleanPriceDetail", appCleanPriceDetail);
		return "modules/app/appCleanPriceDetailForm";
	}

	/**
	 * 保存商户清洗价格明细表
	 */
	@RequiresPermissions("app:appCleanPriceDetail:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated AppCleanPriceDetail appCleanPriceDetail) {
		appCleanPriceDetailService.saveprice(appCleanPriceDetail);
		return renderResult(Global.TRUE, text("保存商户清洗价格明细表成功！"));
	}
	
}
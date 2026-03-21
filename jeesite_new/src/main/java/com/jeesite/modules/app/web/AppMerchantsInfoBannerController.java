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
import com.jeesite.modules.app.entity.AppMerchantsInfoBanner;
import com.jeesite.modules.app.service.AppMerchantsInfoBannerService;

/**
 * 商户详情banner图Controller
 * @author zcq
 * @version 2019-07-30
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appMerchantsInfoBanner")
public class AppMerchantsInfoBannerController extends BaseController {

	@Autowired
	private AppMerchantsInfoBannerService appMerchantsInfoBannerService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public AppMerchantsInfoBanner get(Long id, boolean isNewRecord) {
		return appMerchantsInfoBannerService.get(String.valueOf(id), isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("app:appMerchantsInfoBanner:view")
	@RequestMapping(value = {"list", ""})
	public String list(AppMerchantsInfoBanner appMerchantsInfoBanner, Model model) {
		model.addAttribute("appMerchantsInfoBanner", appMerchantsInfoBanner);
		return "modules/app/appMerchantsInfoBannerList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("app:appMerchantsInfoBanner:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AppMerchantsInfoBanner> listData(AppMerchantsInfoBanner appMerchantsInfoBanner, HttpServletRequest request, HttpServletResponse response) {
		appMerchantsInfoBanner.setPage(new Page<>(request, response));
		Page<AppMerchantsInfoBanner> page = appMerchantsInfoBannerService.findPage(appMerchantsInfoBanner);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("app:appMerchantsInfoBanner:view")
	@RequestMapping(value = "form")
	public String form(AppMerchantsInfoBanner appMerchantsInfoBanner, Model model) {
		model.addAttribute("appMerchantsInfoBanner", appMerchantsInfoBanner);
		return "modules/app/appMerchantsInfoBannerForm";
	}

	/**
	 * 保存商户详情banner图
	 */
	@RequiresPermissions("app:appMerchantsInfoBanner:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated AppMerchantsInfoBanner appMerchantsInfoBanner) {
		appMerchantsInfoBannerService.save(appMerchantsInfoBanner);
		return renderResult(Global.TRUE, text("保存商户详情banner图成功！"));
	}
	
	/**
	 * 删除商户详情banner图
	 */
	@RequiresPermissions("app:appMerchantsInfoBanner:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(AppMerchantsInfoBanner appMerchantsInfoBanner) {
		appMerchantsInfoBannerService.delete(appMerchantsInfoBanner);
		return renderResult(Global.TRUE, text("删除商户详情banner图成功！"));
	}
	
}
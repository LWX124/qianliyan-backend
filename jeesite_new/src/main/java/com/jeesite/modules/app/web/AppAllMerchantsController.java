/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.app.entity.AppUpMerchants;
import com.jeesite.modules.app.service.AppAllMerchantsService;
import com.jeesite.modules.app.service.AppBUserService;
import com.jeesite.modules.app.service.AppPhotoMerService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 通过图片上架的4s店表Controller
 * @author zcq
 * @version 2021-03-08
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appAllMerchants")
public class AppAllMerchantsController extends BaseController {

	private final static Logger logger = LoggerFactory.getLogger(AppAllMerchantsController.class);


	@Resource
	private AppAllMerchantsService appAllMerchantsService;

	@Resource
	private AppBUserService appBUserService;


	/**
	 * 获取数据
	 */
	@ModelAttribute
	public AppUpMerchants get(Integer id, boolean isNewRecord) {
		return appAllMerchantsService.get(String.valueOf(id), isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("app:appAllMerchants:view")
	@RequestMapping(value = {"list", ""})
	public String list(AppUpMerchants appUpMerchants, Model model) {
		model.addAttribute("appUpMerchants", appUpMerchants);
		return "modules/app/appAllMerchantsList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("app:appAllMerchants:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AppUpMerchants> listData(AppUpMerchants appUpMerchants, HttpServletRequest request, HttpServletResponse response) {
		appUpMerchants.setPage(new Page<>(request, response));
		Page<AppUpMerchants> page = appAllMerchantsService.findPage(appUpMerchants);
		return page;
	}




	
}
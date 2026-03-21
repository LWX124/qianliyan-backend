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
import com.jeesite.modules.app.entity.AppUserPlus;
import com.jeesite.modules.app.service.AppUserPlusService;

/**
 * plus会员Controller
 * @author dh
 * @version 2019-09-10
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appUserPlus")
public class AppUserPlusController extends BaseController {

	@Autowired
	private AppUserPlusService appUserPlusService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public AppUserPlus get(Long id, boolean isNewRecord) {
		return appUserPlusService.get(id+"", isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("app:appUserPlus:view")
	@RequestMapping(value = {"list", ""})
	public String list(AppUserPlus appUserPlus, Model model) {
		model.addAttribute("appUserPlus", appUserPlus);
		return "modules/app/appUserPlusList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("app:appUserPlus:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AppUserPlus> listData(AppUserPlus appUserPlus, HttpServletRequest request, HttpServletResponse response) {
		appUserPlus.setPage(new Page<>(request, response));
		Page<AppUserPlus> page = appUserPlusService.findPage(appUserPlus);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("app:appUserPlus:view")
	@RequestMapping(value = "form")
	public String form(AppUserPlus appUserPlus, Model model) {
		model.addAttribute("appUserPlus", appUserPlus);
		return "modules/app/appUserPlusForm";
	}

	/**
	 * 保存plus会员
	 */
	@RequiresPermissions("app:appUserPlus:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated AppUserPlus appUserPlus) {
		appUserPlusService.save(appUserPlus);
		return renderResult(Global.TRUE, text("保存plus会员成功！"));
	}
	
}
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
import com.jeesite.modules.app.entity.AppUserBehavior;
import com.jeesite.modules.app.service.AppUserBehaviorService;

/**
 * 用户视频统计个数Controller
 * @author zcq
 * @version 2019-12-26
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appUserBehavior")
public class AppUserBehaviorController extends BaseController {

	@Autowired
	private AppUserBehaviorService appUserBehaviorService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public AppUserBehavior get(Long id, boolean isNewRecord) {
		return appUserBehaviorService.get(String.valueOf(id), isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("app:appUserBehavior:view")
	@RequestMapping(value = {"list", ""})
	public String list(AppUserBehavior appUserBehavior, Model model) {
		model.addAttribute("appUserBehavior", appUserBehavior);
		return "modules/app/appUserBehaviorList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("app:appUserBehavior:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AppUserBehavior> listData(AppUserBehavior appUserBehavior, HttpServletRequest request, HttpServletResponse response) {
		appUserBehavior.setPage(new Page<>(request, response));
		Page<AppUserBehavior> page = appUserBehaviorService.findPage(appUserBehavior);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("app:appUserBehavior:view")
	@RequestMapping(value = "form")
	public String form(AppUserBehavior appUserBehavior, Model model) {
		model.addAttribute("appUserBehavior", appUserBehavior);
		return "modules/app/appUserBehaviorForm";
	}

	/**
	 * 保存用户视频统计个数
	 */
	@RequiresPermissions("app:appUserBehavior:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated AppUserBehavior appUserBehavior) {
		appUserBehaviorService.save(appUserBehavior);
		return renderResult(Global.TRUE, text("保存用户视频统计个数成功！"));
	}
	
}
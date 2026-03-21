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
import com.jeesite.modules.app.entity.AppLable;
import com.jeesite.modules.app.service.AppLableService;

/**
 * 标签表Controller
 * @author zcq
 * @version 2019-08-19
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appLable")
public class AppLableController extends BaseController {

	@Autowired
	private AppLableService appLableService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public AppLable get(Long id, boolean isNewRecord) {
		return appLableService.get(String.valueOf(id), isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("app:appLable:view")
	@RequestMapping(value = {"list", ""})
	public String list(AppLable appLable, Model model) {
		model.addAttribute("appLable", appLable);
		return "modules/app/appLableList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("app:appLable:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AppLable> listData(AppLable appLable, HttpServletRequest request, HttpServletResponse response) {
		appLable.setPage(new Page<>(request, response));
		Page<AppLable> page = appLableService.findPage(appLable);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("app:appLable:view")
	@RequestMapping(value = "form")
	public String form(AppLable appLable, Model model) {
		model.addAttribute("appLable", appLable);
		return "modules/app/appLableForm";
	}

	/**
	 * 保存标签表
	 */
	@RequiresPermissions("app:appLable:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated AppLable appLable) {
		appLableService.save(appLable);
		return renderResult(Global.TRUE, text("保存标签表成功！"));
	}
	
	/**
	 * 删除标签表
	 */
	@RequiresPermissions("app:appLable:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(AppLable appLable) {
		appLableService.delete(appLable);
		return renderResult(Global.TRUE, text("删除标签表成功！"));
	}
	
}
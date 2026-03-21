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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.app.entity.AppEveryMesg;
import com.jeesite.modules.app.service.AppEveryMesgService;

/**
 * 每日数据Controller
 * @author zcq
 * @version 2019-12-03
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appEveryMesg")
public class AppEveryMesgController extends BaseController {

	@Autowired
	private AppEveryMesgService appEveryMesgService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public AppEveryMesg get(Long id, boolean isNewRecord) {
		return appEveryMesgService.get(String.valueOf(id), isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("app:appEveryMesg:view")
	@RequestMapping(value = {"list", ""})
	public String list(AppEveryMesg appEveryMesg, Model model) {
		model.addAttribute("appEveryMesg", appEveryMesg);
		return "modules/app/appEveryMesgList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("app:appEveryMesg:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AppEveryMesg> listData(AppEveryMesg appEveryMesg, HttpServletRequest request, HttpServletResponse response) {
		appEveryMesg.setPage(new Page<>(request, response));
		Page<AppEveryMesg> page = appEveryMesgService.findPage(appEveryMesg);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("app:appEveryMesg:view")
	@RequestMapping(value = "form")
	public String form(AppEveryMesg appEveryMesg, Model model) {
		model.addAttribute("appEveryMesg", appEveryMesg);
		return "modules/app/appEveryMesgForm";
	}
	
}
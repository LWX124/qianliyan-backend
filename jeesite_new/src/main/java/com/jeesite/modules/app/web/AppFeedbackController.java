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
import com.jeesite.modules.app.entity.AppFeedback;
import com.jeesite.modules.app.service.AppFeedbackService;

/**
 * app_feedbackController
 * @author zcq
 * @version 2019-09-28
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appFeedback")
public class AppFeedbackController extends BaseController {

	@Autowired
	private AppFeedbackService appFeedbackService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public AppFeedback get(Long id, boolean isNewRecord) {
		return appFeedbackService.get(String.valueOf(id), isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("app:appFeedback:view")
	@RequestMapping(value = {"list", ""})
	public String list(AppFeedback appFeedback, Model model) {
		model.addAttribute("appFeedback", appFeedback);
		return "modules/app/appFeedbackList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("app:appFeedback:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AppFeedback> listData(AppFeedback appFeedback, HttpServletRequest request, HttpServletResponse response) {
		appFeedback.setPage(new Page<>(request, response));
		Page<AppFeedback> page = appFeedbackService.findPage(appFeedback);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("app:appFeedback:view")
	@RequestMapping(value = "form")
	public String form(AppFeedback appFeedback, Model model) {
		model.addAttribute("appFeedback", appFeedback);
		return "modules/app/appFeedbackForm";
	}
	
}
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
import com.jeesite.modules.app.entity.AppCleanIndet;
import com.jeesite.modules.app.service.AppCleanIndetService;

/**
 * 洗车订单表Controller
 * @author dh
 * @version 2019-12-17
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appCleanIndet")
public class AppCleanIndetController extends BaseController {

	@Autowired
	private AppCleanIndetService appCleanIndetService;

	/**
	 * 获取数据
	 */
	@ModelAttribute
	public AppCleanIndet get(Long id, boolean isNewRecord) {
		return appCleanIndetService.get(id+"", isNewRecord);
	}

	/**
	 * 查询列表
	 */
	@RequiresPermissions("app:appCleanIndet:view")
	@RequestMapping(value = {"list", ""})
	public String list(AppCleanIndet appCleanIndet, Model model) {
		model.addAttribute("appCleanIndet", appCleanIndet);
		return "modules/app/appCleanIndetList";
	}

	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("app:appCleanIndet:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AppCleanIndet> listData(AppCleanIndet appCleanIndet, HttpServletRequest request, HttpServletResponse response) {
		appCleanIndet.setPage(new Page<>(request, response));
		Page<AppCleanIndet> page = appCleanIndetService.findPage(appCleanIndet);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("app:appCleanIndet:view")
	@RequestMapping(value = "form")
	public String form(AppCleanIndet appCleanIndet, Model model) {
		model.addAttribute("appCleanIndet", appCleanIndet);
		return "modules/app/appCleanIndetForm";
	}

	/**
	 * 保存洗车订单表
	 */
	@RequiresPermissions("app:appCleanIndet:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated AppCleanIndet appCleanIndet) {
		appCleanIndetService.save(appCleanIndet);
		return renderResult(Global.TRUE, text("保存洗车订单表成功！"));
	}

	/**
	 * 停用洗车订单表
	 */
	@RequiresPermissions("app:appCleanIndet:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(AppCleanIndet appCleanIndet) {
		appCleanIndet.setStatus(AppCleanIndet.STATUS_DISABLE);
		appCleanIndetService.updateStatus(appCleanIndet);
		return renderResult(Global.TRUE, text("停用洗车订单表成功"));
	}

	/**
	 * 启用洗车订单表
	 */
	@RequiresPermissions("app:appCleanIndet:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(AppCleanIndet appCleanIndet) {
		appCleanIndet.setStatus(AppCleanIndet.STATUS_NORMAL);
		appCleanIndetService.updateStatus(appCleanIndet);
		return renderResult(Global.TRUE, text("启用洗车订单表成功"));
	}

	/**
	 * 删除洗车订单表
	 */
	@RequiresPermissions("app:appCleanIndet:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(AppCleanIndet appCleanIndet) {
		appCleanIndetService.delete(appCleanIndet);
		return renderResult(Global.TRUE, text("删除洗车订单表成功！"));
	}

}

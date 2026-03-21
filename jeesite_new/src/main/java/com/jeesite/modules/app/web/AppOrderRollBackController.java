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
import com.jeesite.modules.app.entity.AppOrderRollBack;
import com.jeesite.modules.app.service.AppOrderRollBackService;

/**
 * 库存回滚辅助表Controller
 * @author dh
 * @version 2019-12-31
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appOrderRollBack")
public class AppOrderRollBackController extends BaseController {

	@Autowired
	private AppOrderRollBackService appOrderRollBackService;

	/**
	 * 获取数据
	 */
	@ModelAttribute
	public AppOrderRollBack get(Long id, boolean isNewRecord) {
		return appOrderRollBackService.get(String.valueOf(id), isNewRecord);
	}

	/**
	 * 查询列表
	 */
	@RequiresPermissions("app:appOrderRollBack:view")
	@RequestMapping(value = {"list", ""})
	public String list(AppOrderRollBack appOrderRollBack, Model model) {
		model.addAttribute("appOrderRollBack", appOrderRollBack);
		return "modules/app/appOrderRollBackList";
	}

	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("app:appOrderRollBack:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AppOrderRollBack> listData(AppOrderRollBack appOrderRollBack, HttpServletRequest request, HttpServletResponse response) {
		appOrderRollBack.setPage(new Page<>(request, response));
		Page<AppOrderRollBack> page = appOrderRollBackService.findPage(appOrderRollBack);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("app:appOrderRollBack:view")
	@RequestMapping(value = "form")
	public String form(AppOrderRollBack appOrderRollBack, Model model) {
		model.addAttribute("appOrderRollBack", appOrderRollBack);
		return "modules/app/appOrderRollBackForm";
	}

	/**
	 * 保存库存回滚
	 */
	@RequiresPermissions("app:appOrderRollBack:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated AppOrderRollBack appOrderRollBack) {
		appOrderRollBackService.save(appOrderRollBack);
		return renderResult(Global.TRUE, text("保存库存回滚成功！"));
	}

}

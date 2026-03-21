/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.app.excep.CusException;
import com.jeesite.modules.util2.StringUtil;
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
import com.jeesite.modules.app.entity.AppRescueIndent;
import com.jeesite.modules.app.service.AppRescueIndentService;

/**
 * 救援表Controller
 * @author dh
 * @version 2020-03-03
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appRescueIndent")
public class AppRescueIndentController extends BaseController {

	@Autowired
	private AppRescueIndentService appRescueIndentService;

	/**
	 * 获取数据
	 */
	@ModelAttribute
	public AppRescueIndent get(Long id, boolean isNewRecord) {
		return appRescueIndentService.get(String.valueOf(id), isNewRecord);
	}

	/**
	 * 查询列表
	 */
	@RequiresPermissions("app:appRescueIndent:view")
	@RequestMapping(value = {"list", ""})
	public String list(AppRescueIndent appRescueIndent, Model model) {
		model.addAttribute("appRescueIndent", appRescueIndent);
		return "modules/app/appRescueIndentList";
	}

	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("app:appRescueIndent:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AppRescueIndent> listData(AppRescueIndent appRescueIndent, HttpServletRequest request, HttpServletResponse response) {
		appRescueIndent.setPage(new Page<>(request, response));
		Page<AppRescueIndent> page = appRescueIndentService.findPage(appRescueIndent);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("app:appRescueIndent:view")
	@RequestMapping(value = "form")
	public String form(AppRescueIndent appRescueIndent, Model model) {
		model.addAttribute("appRescueIndent", appRescueIndent);
		return "modules/app/appRescueIndentForm";
	}

	/**
	 * 退款
	 */
	@RequiresPermissions("app:appRescueIndent:edit")
	@RequestMapping(value = "backMoney")
	public String backMoney(String ids) {
		if (StringUtils.isEmpty(ids)) {
			return renderResult(Global.FALSE, text("id不能为空！"));
		}

		//发起退款
		try {
			appRescueIndentService.backMoney(ids);
		} catch (CusException e) {
			String message = e.getMessage();
			return renderResult(Global.FALSE, text(message));
		}
		return renderResult(Global.TRUE, text("退款发起成功！"));
	}

	/**
	 * 保存救援表
	 */
	@RequiresPermissions("app:appRescueIndent:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated AppRescueIndent appRescueIndent) {
		appRescueIndentService.save(appRescueIndent);
		return renderResult(Global.TRUE, text("保存救援表成功！"));
	}

	/**
	 * 删除救援表
	 */
	@RequiresPermissions("app:appRescueIndent:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(AppRescueIndent appRescueIndent) {
		appRescueIndentService.delete(appRescueIndent);
		return renderResult(Global.TRUE, text("删除救援表成功！"));
	}

}

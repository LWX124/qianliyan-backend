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
import com.jeesite.modules.app.entity.AppOurIndentAmount;
import com.jeesite.modules.app.service.AppOurIndentAmountService;

/**
 * 订单抽成记录表Controller
 * @author zcq
 * @version 2019-10-21
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appOurIndentAmount")
public class AppOurIndentAmountController extends BaseController {

	@Autowired
	private AppOurIndentAmountService appOurIndentAmountService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public AppOurIndentAmount get(Long id, boolean isNewRecord) {
		return appOurIndentAmountService.get(String.valueOf(id), isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("app:appOurIndentAmount:view")
	@RequestMapping(value = {"list", ""})
	public String list(AppOurIndentAmount appOurIndentAmount, Model model) {
		model.addAttribute("appOurIndentAmount", appOurIndentAmount);
		return "modules/app/appOurIndentAmountList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("app:appOurIndentAmount:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AppOurIndentAmount> listData(AppOurIndentAmount appOurIndentAmount, HttpServletRequest request, HttpServletResponse response) {
		appOurIndentAmount.setPage(new Page<>(request, response));
		Page<AppOurIndentAmount> page = appOurIndentAmountService.findPage(appOurIndentAmount);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("app:appOurIndentAmount:view")
	@RequestMapping(value = "form")
	public String form(AppOurIndentAmount appOurIndentAmount, Model model) {
		model.addAttribute("appOurIndentAmount", appOurIndentAmount);
		return "modules/app/appOurIndentAmountForm";
	}

	/**
	 * 保存订单抽成记录表
	 */
	@RequiresPermissions("app:appOurIndentAmount:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated AppOurIndentAmount appOurIndentAmount) {
		appOurIndentAmountService.save(appOurIndentAmount);
		return renderResult(Global.TRUE, text("保存订单抽成记录表成功！"));
	}
	
	/**
	 * 删除订单抽成记录表
	 */
	@RequiresPermissions("app:appOurIndentAmount:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(AppOurIndentAmount appOurIndentAmount) {
		appOurIndentAmountService.delete(appOurIndentAmount);
		return renderResult(Global.TRUE, text("删除订单抽成记录表成功！"));
	}
	
}
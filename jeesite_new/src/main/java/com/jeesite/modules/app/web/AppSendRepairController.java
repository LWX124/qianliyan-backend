/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.app.entity.AppSendRepair;
import com.jeesite.modules.app.service.AppSendRepairService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 事故车送修现场业务表Controller
 * @author y
 * @version 2022-07-18
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appSendRepair")
public class AppSendRepairController extends BaseController {

	@Autowired
	private AppSendRepairService appSendRepairService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public AppSendRepair get(String id, boolean isNewRecord) {
		return appSendRepairService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("app:appSendRepair:view")
	@RequestMapping(value = {"list", ""})
	public String list(AppSendRepair appSendRepair, Model model) {
		model.addAttribute("appSendRepair", appSendRepair);
		return "modules/app/appSendRepairList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("app:appSendRepair:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AppSendRepair> listData(AppSendRepair appSendRepair, HttpServletRequest request, HttpServletResponse response) {
		appSendRepair.setPage(new Page<>(request, response));
		Page<AppSendRepair> page = appSendRepairService.findPage(appSendRepair);
		//loginCode登录账号
		String loginCode = UserUtils.getUser().getLoginCode();
		if("chejiqiche".equals(loginCode)){
			return page;
		}
		if(StringUtils.isNotEmpty(loginCode)){
			List<AppSendRepair> collect = page.getList().stream().filter(s -> loginCode.equals(s.getUserId())).collect(Collectors.toList());
			page.setList(collect);
		}
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("app:appSendRepair:view")
	@RequestMapping(value = "form")
	public String form(AppSendRepair appSendRepair, Model model) {
		model.addAttribute("appSendRepair", appSendRepair);
		return "modules/app/appSendRepairForm";
	}

	/**
	 * 保存事故车送修现场业务表
	 */
	@RequiresPermissions("app:appSendRepair:edit")
	@PostMapping(value = "save2")
	@ResponseBody
	public String save2(AppSendRepair appSendRepair) {
		String loginCode = UserUtils.getUser().getLoginCode();
		appSendRepair.setUserId(loginCode);
		appSendRepairService.update(appSendRepair);
		return renderResult(Global.TRUE, text("保存事故车送修现场业务表成功！"));
	}

	/**
	 * 保存事故车送修现场业务表
	 */
	@RequiresPermissions("app:appSendRepair:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated AppSendRepair appSendRepair) {
		String loginCode = UserUtils.getUser().getLoginCode();
		appSendRepair.setUserId(loginCode);
		appSendRepairService.save(appSendRepair);
		return renderResult(Global.TRUE, text("保存事故车送修现场业务表成功！"));
	}

	/**
	 * 删除事故车送修现场业务表
	 */
	@RequiresPermissions("app:appSendRepair:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(AppSendRepair appSendRepair) {
		appSendRepairService.delete(appSendRepair);
		return renderResult(Global.TRUE, text("删除事故车送修现场业务表成功！"));
	}

}
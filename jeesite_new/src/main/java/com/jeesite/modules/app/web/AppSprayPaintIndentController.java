/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.app.entity.AppSprayPaintIndent;
import com.jeesite.modules.app.service.AppSprayPaintIndentService;

import java.util.List;

/**
 * 喷漆订单表Controller
 * @author zcq
 * @version 2020-03-24
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appSprayPaintIndent")
public class AppSprayPaintIndentController extends BaseController {

	@Autowired
	private AppSprayPaintIndentService appSprayPaintIndentService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public AppSprayPaintIndent get(Long id, boolean isNewRecord) {
		return appSprayPaintIndentService.get(String.valueOf(id), isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("app:appSprayPaintIndent:view")
	@RequestMapping(value = {"list", ""})
	public String list(AppSprayPaintIndent appSprayPaintIndent, Model model) {
		model.addAttribute("appSprayPaintIndent", appSprayPaintIndent);
		return "modules/app/appSprayPaintIndentList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("app:appSprayPaintIndent:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AppSprayPaintIndent> listData(AppSprayPaintIndent appSprayPaintIndent, HttpServletRequest request, HttpServletResponse response) {
		appSprayPaintIndent.setPage(new Page<>(request, response));
		Page<AppSprayPaintIndent> page = appSprayPaintIndentService.findPage(appSprayPaintIndent);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("app:appSprayPaintIndent:view")
	@RequestMapping(value = "form")
	public String form(AppSprayPaintIndent appSprayPaintIndent, Model model) {
		model.addAttribute("appSprayPaintIndent", appSprayPaintIndent);
		return "modules/app/appSprayPaintIndentForm";
	}

	/**
	 * 保存喷漆订单表
	 */
	@RequiresPermissions("app:appSprayPaintIndent:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated AppSprayPaintIndent appSprayPaintIndent) {
		appSprayPaintIndentService.save(appSprayPaintIndent);
		return renderResult(Global.TRUE, text("保存喷漆订单表成功！"));
	}

	@GetMapping(value = "/findImg")
	@ResponseBody
	public JSONObject findImg(String sprayId,Integer type){
		JSONObject result = new JSONObject();
		//根据订单id查询到图片数据
		List<String> imgs = appSprayPaintIndentService.findImgs(sprayId,type);
		result.put("imgList",imgs);
		return result;
	}

	
}
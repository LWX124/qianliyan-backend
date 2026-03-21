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
import com.jeesite.modules.app.entity.CarInfor;
import com.jeesite.modules.app.service.CarInforService;

/**
 * car_inforController
 * @author y
 * @version 2022-10-09
 */
@Controller
@RequestMapping(value = "${adminPath}/app/carInfor")
public class CarInforController extends BaseController {

	@Autowired
	private CarInforService carInforService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public CarInfor get(Long id, boolean isNewRecord) {
		return carInforService.get(String.valueOf(id), isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("app:carInfor:view")
	@RequestMapping(value = {"list", ""})
	public String list(CarInfor carInfor, Model model) {
		model.addAttribute("carInfor", carInfor);
		return "modules/app/carInforList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("app:carInfor:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<CarInfor> listData(CarInfor carInfor, HttpServletRequest request, HttpServletResponse response) {
		carInfor.setPage(new Page<>(request, response));
		Page<CarInfor> page = carInforService.findPage(carInfor);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("app:carInfor:view")
	@RequestMapping(value = "form")
	public String form(CarInfor carInfor, Model model) {
		model.addAttribute("carInfor", carInfor);
		return "modules/app/carInforForm";
	}

	/**
	 * 保存car_infor
	 */
	@RequiresPermissions("app:carInfor:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated CarInfor carInfor) {
		carInforService.save(carInfor);
		return renderResult(Global.TRUE, text("保存car_infor成功！"));
	}
	
	/**
	 * 删除car_infor
	 */
	@RequiresPermissions("app:carInfor:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(CarInfor carInfor) {
		carInforService.delete(carInfor);
		return renderResult(Global.TRUE, text("删除car_infor成功！"));
	}
	
}
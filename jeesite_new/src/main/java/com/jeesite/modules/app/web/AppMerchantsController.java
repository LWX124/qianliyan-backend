/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.app.entity.AppMerchants;
import com.jeesite.modules.app.entity.AppMerchantsInfoBanner;
import com.jeesite.modules.app.service.AppMerchantsInfoBannerService;
import com.jeesite.modules.app.service.AppMerchantsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 商户信息表Controller
 * @author zcq
 * @version 2019-07-29
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appMerchants")
public class AppMerchantsController extends BaseController {

	@Autowired
	private AppMerchantsService appMerchantsService;

	@Autowired
	private AppMerchantsInfoBannerService BannerService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public AppMerchants get(Long id, boolean isNewRecord) {
		return appMerchantsService.get(String.valueOf(id), isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("app:appMerchants:view")
	@RequestMapping(value = {"list", ""})
	public String list(AppMerchants appMerchants, Model model) {
		model.addAttribute("appMerchants", appMerchants);
		return "modules/app/appMerchantsList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("app:appMerchants:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AppMerchants> listData(AppMerchants appMerchants, HttpServletRequest request, HttpServletResponse response) {
		appMerchants.setPage(new Page<>(request, response));
		Page<AppMerchants> page = appMerchantsService.findPage(appMerchants);
		List<AppMerchants> list = page.getList();
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("app:appMerchants:view")
	@RequestMapping(value = "form")
	public String form(AppMerchants appMerchants, Model model) {
		//获取到商户id
		String id = appMerchants.getId();
		//根据商户id查询到图片
		List<AppMerchantsInfoBanner> appMerchantsInfoBannerList = BannerService.findId(id);
		String ste = "";
		//遍历拿到图片，添加到字段中
		for (AppMerchantsInfoBanner Banner : appMerchantsInfoBannerList) {
			String url = Banner.getUrl();
			ste += url + "|";
		}
		if (ste == "") {
			ste = ".";
		}
		//截取字符串除了最后一位
		String substring = ste.substring(0, ste.length() - 1);
		//获取到appmerchants对象数据
		appMerchants.setUrl(substring);
		String id1 = appMerchants.getId();
		String name = appMerchants.getName();
		String phone = appMerchants.getPhoneNumber();
		String address = appMerchants.getAddress();
		String state = appMerchants.getState();
		String reason = appMerchants.getReason();
		Double rebates = appMerchants.getRebates();
		Integer efficiencyScore = appMerchants.getEfficiencyScore();
		Integer serviceScore = appMerchants.getServiceScore();
		Integer score = appMerchants.getScore();
		//放到新对象中
		AppMerchants appMerchants1 = new AppMerchants();
		appMerchants1.setUrl(substring);
		appMerchants1.setName(name);
		appMerchants1.setPhoneNumber(phone);
		appMerchants1.setAddress(address);
		appMerchants1.setState(state);
		appMerchants1.setReason(reason);
		appMerchants1.setEfficiencyScore(efficiencyScore);
		appMerchants1.setServiceScore(serviceScore);
		appMerchants.setScore(score);
		appMerchants1.setRebates(rebates);

		model.addAttribute("appMerchants", appMerchants1);

		return "modules/app/appMerchantsForm";
	}

	/**
	 * 保存商户信息表
	 */
	@RequiresPermissions("app:appMerchants:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated AppMerchants appMerchants) {
		appMerchantsService.save(appMerchants);
		return renderResult(Global.TRUE, text("保存商户信息表成功！"));
	}
	
	/**
	 * 删除商户信息表
	 */
	@RequiresPermissions("app:appMerchants:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(AppMerchants appMerchants) {
		appMerchantsService.delete(appMerchants);
		return renderResult(Global.TRUE, text("删除商户信息表成功！"));
	}
	
}
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
import com.jeesite.modules.app.entity.AppMerchantsLable;
import com.jeesite.modules.app.service.AppMerchantsLableService;

import java.util.ArrayList;
import java.util.List;

/**
 * 商户和标签关系表Controller
 * @author zcq
 * @version 2019-08-20
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appMerchantsLable")
public class AppMerchantsLableController extends BaseController {

	@Autowired
	private AppMerchantsLableService appMerchantsLableService;

	private String inid;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public AppMerchantsLable get(Long id, boolean isNewRecord) {
		return appMerchantsLableService.get(String.valueOf(id), isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("app:appMerchantsLable:view")
	@RequestMapping(value = {"list", ""})
	public String list(AppMerchantsLable appMerchantsLable, Model model,String id) {
		inid = id;
		model.addAttribute("appMerchantsLable", appMerchantsLable);
		return "modules/app/appMerchantsLableList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("app:appMerchantsLable:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AppMerchantsLable> listData(AppMerchantsLable appMerchantsLable, HttpServletRequest request, HttpServletResponse response) {
		appMerchantsLable.setPage(new Page<>(request, response));
		Page<AppMerchantsLable> page = appMerchantsLableService.findPage(appMerchantsLable);
		List<AppMerchantsLable> list = page.getList();
		//新list
		ArrayList<AppMerchantsLable> newlist = new ArrayList<>();
		//遍历所有集合拿到对应id的集合，放到新集合中
		for (AppMerchantsLable merchantsLable : list) {
			if (inid.equals(merchantsLable.getMerchantsId().toString())){
				newlist.add(merchantsLable);
			}
		}
		//用新集合替换原来的数据
		page.setList(newlist);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("app:appMerchantsLable:view")
	@RequestMapping(value = "form")
	public String form(AppMerchantsLable appMerchantsLable, Model model) {
		appMerchantsLable.setMerchantsId(Long.valueOf(inid));
		model.addAttribute("appMerchantsLable", appMerchantsLable);
		return "modules/app/appMerchantsLableForm";
	}

	/**
	 * 保存商户和标签关系表
	 */
	@RequiresPermissions("app:appMerchantsLable:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated AppMerchantsLable appMerchantsLable) {

		//添加新增才进入方法
		if (appMerchantsLable.getIsNewRecord()){
			AppMerchantsLable appMerchantsLable1 = appMerchantsLableService.selectByLable(appMerchantsLable.getMerchantsId(), appMerchantsLable.getLableId());
			//没有查出数据证明没有一样得数据
			if (appMerchantsLable1==null){
				appMerchantsLableService.save(appMerchantsLable);
				return renderResult(Global.TRUE, text("保存商户和标签关系表成功！"));
			}else {
				//有一样的数据就是重复数据
				return renderResult(Global.FALSE, text("有重复数据请勿添加"));
			}
		}
		appMerchantsLableService.save(appMerchantsLable);
		return renderResult(Global.TRUE, text("保存商户和标签关系表成功！"));
	}

	/**
	 * 删除商户和标签关系表
	 */
	@RequiresPermissions("app:appMerchantsLable:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(AppMerchantsLable appMerchantsLable) {
		appMerchantsLableService.delete(appMerchantsLable);
		return renderResult(Global.TRUE, text("删除商户和标签关系表成功！"));
	}
	
}
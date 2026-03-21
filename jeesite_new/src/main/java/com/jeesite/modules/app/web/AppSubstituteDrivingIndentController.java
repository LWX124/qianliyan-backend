/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.modules.app.entity.*;
import com.jeesite.modules.app.service.AppBUserService;
import com.jeesite.modules.app.service.AppLableDetailsReviewTreeService;
import com.jeesite.modules.app.service.AppUserBMessageService;
import com.jeesite.modules.constant2.RedisKeyUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
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
import com.jeesite.modules.app.service.AppSubstituteDrivingIndentService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 代驾订单表Controller
 * @author zcq
 * @version 2020-06-23
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appSubstituteDrivingIndent")
public class AppSubstituteDrivingIndentController extends BaseController {

	@Autowired
	private AppSubstituteDrivingIndentService appSubstituteDrivingIndentService;

	@Resource
	private RedisTemplate redisTemplate;

	@Resource
	private AppBUserService appBUserService;

	@Resource
	private AppUserBMessageService appUserBMessageService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public AppSubstituteDrivingIndent get(Long id, boolean isNewRecord) {
		return appSubstituteDrivingIndentService.get(String.valueOf(id), isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("app:appSubstituteDrivingIndent:view")
	@RequestMapping(value = {"list", ""})
	public String list(AppSubstituteDrivingIndent appSubstituteDrivingIndent, Model model) {
		model.addAttribute("appSubstituteDrivingIndent", appSubstituteDrivingIndent);
		return "modules/app/appSubstituteDrivingIndentList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("app:appSubstituteDrivingIndent:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AppSubstituteDrivingIndent> listData(AppSubstituteDrivingIndent appSubstituteDrivingIndent, HttpServletRequest request, HttpServletResponse response) {
		appSubstituteDrivingIndent.setPage(new Page<>(request, response));
		Page<AppSubstituteDrivingIndent> page = appSubstituteDrivingIndentService.findPage(appSubstituteDrivingIndent);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("app:appSubstituteDrivingIndent:view")
	@RequestMapping(value = "form")
	public String form(AppSubstituteDrivingIndent appSubstituteDrivingIndent, Model model) {
		model.addAttribute("appSubstituteDrivingIndent", appSubstituteDrivingIndent);
		return "modules/app/appSubstituteDrivingIndentForm";
	}

	/**
	 * 保存代驾订单表
	 */
	@RequiresPermissions("app:appSubstituteDrivingIndent:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated AppSubstituteDrivingIndent appSubstituteDrivingIndent) {
		appSubstituteDrivingIndentService.save(appSubstituteDrivingIndent);
		return renderResult(Global.TRUE, text("保存代驾订单表成功！"));
	}
	
	/**
	 * 删除代驾订单表
	 */
	@RequiresPermissions("app:appSubstituteDrivingIndent:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(AppSubstituteDrivingIndent appSubstituteDrivingIndent) {
		appSubstituteDrivingIndentService.delete(appSubstituteDrivingIndent);
		return renderResult(Global.TRUE, text("删除代驾订单表成功！"));
	}



	@RequiresPermissions("app:appSubstituteDrivingIndent:view")
	@RequestMapping(value = "substituteDirveMerchants")
	public String substituteDirveMerchants(AppSubstituteDrivingIndent appSubstituteDrivingIndent, Model model) {
		model.addAttribute("AppSubstituteDrivingIndent", appSubstituteDrivingIndent);
		return "modules/app/substituteDirveMerchants";
	}


	//查询到geo中救援数据
	@RequestMapping(value = "/findGeoSubMerchants")
	@ResponseBody
	public JSONObject findGeoMerchants() {
		AppBUser one = new AppBUser();
		one.setLat(30.657487);  //天府广场
		one.setLng(104.065735);
		List<RescueMerchants> tableMesses = new ArrayList<>();
		Circle circle = new Circle(one.getLng(), one.getLat(), 50.0 * 10000);
		RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().includeCoordinates().sortAscending().limit(50);
		GeoResults<RedisGeoCommands.GeoLocation<String>> radius = redisTemplate.opsForGeo().radius(RedisKeyUtils.MERCHANTS_SUBSTITUTE_DIRVIE_GEO, circle, args);
		List<GeoResult<RedisGeoCommands.GeoLocation<String>>> content = radius.getContent();
		for (GeoResult<RedisGeoCommands.GeoLocation<String>> geoLocationGeoResult : content) {
			RedisGeoCommands.GeoLocation<String> conten = geoLocationGeoResult.getContent();
			String name = conten.getName();
			JSONObject jsonObject = JSONObject.parseObject(name);
			String merchantsName = jsonObject.getString("merchantsName");
			BigDecimal lng = jsonObject.getBigDecimal("lng");
			String address = jsonObject.getString("address");
			BigDecimal lat = jsonObject.getBigDecimal("lat");
			String id1 = jsonObject.getString("id");
			RescueMerchants tableMess = new RescueMerchants();
			tableMess.setId(id1);
			tableMess.setMerchantsName(merchantsName);
			tableMess.setAddress(address);
			tableMess.setLng(lng);
			tableMess.setLat(lat);
			tableMesses.add(tableMess);
		}
		JSONObject result = new JSONObject();

		result.put("code",0);
		result.put("msg","");
		result.put("count",tableMesses.size());
		result.put("data",tableMesses);
		return result;
	}

	//删除geo中救援商户
	@RequestMapping(value = "/delSubMerchants")
	@ResponseBody
	public JSONObject delRescueMerchants(String userBid) {
		JSONObject result = new JSONObject();
		AppUserBMessage appUserBMessage = appUserBMessageService.get(userBid);
		Integer userBId = appUserBMessage.getUserBId();
		AppBUser appBUser = appBUserService.get(String.valueOf(userBId));
		String substituteRedis = appBUser.getSubstituteRedis();
		redisTemplate.opsForGeo().remove(RedisKeyUtils.MERCHANTS_SUBSTITUTE_DIRVIE_GEO, substituteRedis);

		//修改商户服务项目状态
		appBUser.setSubstituteRedis("");
		appBUserService.update(appBUser);

		//查询到代驾技师

		appUserBMessage.setBusinessType(2L);
		appUserBMessageService.update(appUserBMessage);

		result.put("stat","success");
		return result;
	}
	
}
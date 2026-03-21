/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.app.dao.AppBUserDao;
import com.jeesite.modules.app.entity.AppBUser;
import com.jeesite.modules.app.entity.AppLableDetailsReviewTree;
import com.jeesite.modules.app.entity.RescueMerchants;
import com.jeesite.modules.app.service.AppBUserService;
import com.jeesite.modules.app.service.AppLableDetailsReviewTreeService;
import com.jeesite.modules.constant2.RedisKeyUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.app.entity.AppYearCheckIndent;
import com.jeesite.modules.app.service.AppYearCheckIndentService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 年检订单表Controller
 * @author zcq
 * @version 2020-04-03
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appYearCheckIndent")
public class AppYearCheckIndentController extends BaseController {

	@Autowired
	private AppYearCheckIndentService appYearCheckIndentService;

	@Resource
	private AppBUserDao appBUserDao;

	@Resource
	private AppLableDetailsReviewTreeService appLableDetailsReviewTreeService;

	@Resource
	private RedisTemplate redisTemplate;

	@Resource
	private AppBUserService appBUserService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public AppYearCheckIndent get(Long id, boolean isNewRecord) {
		return appYearCheckIndentService.get(String.valueOf(id), isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("app:appYearCheckIndent:view")
	@RequestMapping(value = {"list", ""})
	public String list(AppYearCheckIndent appYearCheckIndent, Model model) {
		model.addAttribute("appYearCheckIndent", appYearCheckIndent);
		return "modules/app/appYearCheckIndentList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("app:appYearCheckIndent:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AppYearCheckIndent> listData(AppYearCheckIndent appYearCheckIndent, HttpServletRequest request, HttpServletResponse response) {
		appYearCheckIndent.setPage(new Page<>(request, response));
		Page<AppYearCheckIndent> page = appYearCheckIndentService.findPage(appYearCheckIndent);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("app:appYearCheckIndent:view")
	@RequestMapping(value = "form")
	public String form(AppYearCheckIndent appYearCheckIndent, Model model) {
		model.addAttribute("appYearCheckIndent", appYearCheckIndent);
		return "modules/app/appYearCheckIndentForm";
	}

	/**
	 * 保存年检订单表
	 */
	@RequiresPermissions("app:appYearCheckIndent:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated AppYearCheckIndent appYearCheckIndent) {
		appYearCheckIndentService.save(appYearCheckIndent);
		return renderResult(Global.TRUE, text("保存年检订单表成功！"));
	}
	
	/**
	 * 删除年检订单表
	 */
	@RequiresPermissions("app:appYearCheckIndent:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(AppYearCheckIndent appYearCheckIndent) {
		appYearCheckIndentService.delete(appYearCheckIndent);
		return renderResult(Global.TRUE, text("删除年检订单表成功！"));
	}


    @RequiresPermissions("app:appBUser:view")
    @RequestMapping(value = "yearCheckMerchants")
    public String test(AppYearCheckIndent appYearCheckIndent, Model model) {
        model.addAttribute("appYearCheckIndent", appYearCheckIndent);
        return "modules/app/addYearCheckMerchants";
    }


	//年检商户添加到geo中
	@RequestMapping(value = "/addYearCheckMerchants")
	@ResponseBody
	public JSONObject addRescueMerchants(String phone) {
		JSONObject data = new JSONObject();
		AppBUser one = appBUserDao.findOne(phone);
		if (one==null){
			data.put("stat","false");
			return data;
		}
		if (StringUtils.isEmpty(one.getAddress())){
			data.put("stat","address");
			return data;
		}
		AppLableDetailsReviewTree appLable = appLableDetailsReviewTreeService.selectLable(Integer.valueOf(one.getId()), 15, 1);
		if (appLable!=null){
			if (appLable.getState().equals("1")){
				data.put("stat","false");
				return data;
			}else if (appLable.getState().equals("0")){
				//开启
				appLable.setState("1");
				JSONObject in = new JSONObject();
				in.put("id", one.getId());
				in.put("merchantsName", one.getMerchantsName());
				in.put("lat", one.getLat());
				in.put("lng", one.getLng());
				in.put("address", one.getAddress());
				redisTemplate.opsForGeo().add(RedisKeyUtils.MERCHANTS_YEAR_CHECK_GEO, new Point(one.getLng(), one.getLat()), in.toJSONString());
				one.setYearcheckRedis(in.toJSONString());
				appBUserService.update(one);
				appLableDetailsReviewTreeService.update(appLable);
				data.put("stat","success");
				return data;
			}
		}

		appLableDetailsReviewTreeService.addFirst(Integer.valueOf(one.getId()));
		appLableDetailsReviewTreeService.addRescueSecond(Integer.valueOf(one.getId()),15,null);
		//查询到数据放到geo中
		JSONObject in = new JSONObject();
		in.put("id", one.getId());
		in.put("merchantsName", one.getMerchantsName());
		in.put("lat", one.getLat());
		in.put("lng", one.getLng());
		in.put("address", one.getAddress());
		redisTemplate.opsForGeo().add(RedisKeyUtils.MERCHANTS_YEAR_CHECK_GEO, new Point(one.getLng(), one.getLat()), in.toJSONString());
		one.setYearcheckRedis(in.toJSONString());
		appBUserService.update(one);
		data.put("stat","success");
		return data;
	}

	//查询geo中年检商户的位置

	@RequestMapping(value = "/findYearCheckMerchants")
	@ResponseBody
	public JSONObject findGeoMerchants() {
		AppBUser one = new AppBUser();
		one.setLat(30.657487);  //天府广场
		one.setLng(104.065735);
		List<RescueMerchants> tableMesses = new ArrayList<>();
		Circle circle = new Circle(one.getLng(), one.getLat(), 50.0 * 10000);
		RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().includeCoordinates().sortAscending().limit(50);
		GeoResults<RedisGeoCommands.GeoLocation<String>> radius = redisTemplate.opsForGeo().radius(RedisKeyUtils.MERCHANTS_YEAR_CHECK_GEO, circle, args);
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

	//删除geo中年检商户

	@RequestMapping(value = "/delYearCheckMerchants")
	@ResponseBody
	public JSONObject delRescueMerchants(String userBid) {
		JSONObject result = new JSONObject();
		AppBUser appBUser = appBUserService.get(userBid);
		String yearcheckRedis = appBUser.getYearcheckRedis();
		redisTemplate.opsForGeo().remove(RedisKeyUtils.MERCHANTS_YEAR_CHECK_GEO, yearcheckRedis);

		//修改商户服务项目状态
		AppLableDetailsReviewTree appLable = appLableDetailsReviewTreeService.selectLable(Integer.valueOf(userBid), 15, 1);
		appLable.setState("0");
		appLableDetailsReviewTreeService.update(appLable);
		appBUser.setYearcheckRedis("");
		appBUserService.update(appBUser);

		result.put("stat","success");
		return result;
	}

	@GetMapping(value = "/findImg")
	@ResponseBody
	public JSONObject findImg(String sprayId){
		JSONObject result = new JSONObject();
		//根据订单id查询到图片数据
		List<String> imgs = appYearCheckIndentService.findImgs(sprayId);
		result.put("imgList",imgs);
		return result;
	}
	
}
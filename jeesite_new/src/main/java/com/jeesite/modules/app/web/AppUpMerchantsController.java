/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.web;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.app.entity.AppPhotoMer;
import com.jeesite.modules.app.entity.AppUpMerchants;
import com.jeesite.modules.app.service.AppBUserService;
import com.jeesite.modules.app.service.AppPhotoMerService;
import com.jeesite.modules.app.service.AppUpMerchantsService;
import com.jeesite.modules.file.entity.FileUpload;
import com.jeesite.modules.file.utils.FileUploadUtils;
import com.jeesite.modules.util2.GaoDeUtils;
import com.jeesite.modules.util2.HuaweiSmsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 通过图片上架的4s店表Controller
 * @author zcq
 * @version 2021-03-08
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appUpMerchants")
public class AppUpMerchantsController extends BaseController {

	private final static Logger logger = LoggerFactory.getLogger(AppUpMerchantsController.class);


	@Resource
	private HuaweiSmsService huaweiSmsService;

	@Autowired
	private AppUpMerchantsService appUpMerchantsService;

	@Resource
	private AppPhotoMerService appPhotoMerService;

	@Resource
	private AppBUserService appBUserService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public AppUpMerchants get(Integer id, boolean isNewRecord) {
		return appUpMerchantsService.get(String.valueOf(id), isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("app:appUpMerchants:view")
	@RequestMapping(value = {"list", ""})
	public String list(AppUpMerchants appUpMerchants, Model model) {
		model.addAttribute("appUpMerchants", appUpMerchants);
		return "modules/app/appUpMerchantsList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("app:appUpMerchants:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AppUpMerchants> listData(AppUpMerchants appUpMerchants, HttpServletRequest request, HttpServletResponse response) {
		appUpMerchants.setPage(new Page<>(request, response));
		Page<AppUpMerchants> page = appUpMerchantsService.findPage(appUpMerchants);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("app:appUpMerchants:view")
	@RequestMapping(value = "form")
	public String form(AppUpMerchants appUpMerchants, Model model) {
		model.addAttribute("appUpMerchants", appUpMerchants);
		return "modules/app/appUpMerchantsForm";
	}

	/**
	 * 保存通过图片上架的4s店表
	 */
	@RequiresPermissions("app:appUpMerchants:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated AppUpMerchants appUpMerchants) {
		//新增一条数据，根据地址信息查询出来经纬度保存
		//修改新增逻辑分开写
		//新增
		try {
			if (appUpMerchants.getId()==null){
				//获取到地址信息 查询经纬度，
				if (appUpMerchants.getLat()==null||appUpMerchants.getLng()==null){
					String address = appUpMerchants.getAddress();
					JSONObject positionObj = GaoDeUtils.getLngAndLat(address);
					String longitude = positionObj.getString("longitude");
					String latitude = positionObj.getString("latitude");
					appUpMerchants.setLng(new BigDecimal(longitude));
					appUpMerchants.setLat(new BigDecimal(latitude));
				}

				//新增
				appUpMerchantsService.insertNew(appUpMerchants);
			}else {
				//修改
				//修改参数
				AppUpMerchants appUpMerchants1 = appUpMerchantsService.get(appUpMerchants.getId());
				String address = appUpMerchants.getAddress();
				if (!appUpMerchants1.getAddress().equals(address)){
					JSONObject positionObj = GaoDeUtils.getLngAndLat(address);
					String longitude = positionObj.getString("longitude");
					String latitude = positionObj.getString("latitude");
					appUpMerchants.setLng(new BigDecimal(longitude));
					appUpMerchants.setLat(new BigDecimal(latitude));
				}
				appUpMerchantsService.update(appUpMerchants);

				//添加环信账号
//获取一个随机数
//				double rand = Math.random();
////将随机数转换为字符串
//				String str = String.valueOf(rand).replace("0.", "");
////截取字符串
//				String newStr = str.substring(0, 11);
//
//				//userService,registerHuanxin()
//				appUpMerchantsService.registerHuanxin(appUpMerchants,newStr);

				//添加图片
				//先查询到图片。有图片就删除
				ArrayList<Integer> imgIdList = appPhotoMerService.findAllImgs(appUpMerchants.getId());
				if (!imgIdList.isEmpty()){
					//不为空就删除
					for (Integer integer : imgIdList) {
						appPhotoMerService.delete(appPhotoMerService.get(String.valueOf(integer)));
					}
				}
				//开始添加
				FileUploadUtils.saveFileUpload(appUpMerchants.getId(), "upMerchants_img");
				List<FileUpload> fileUpload = FileUploadUtils.findFileUpload(appUpMerchants.getId(), "upMerchants_img");
				for (int i = 0; i < fileUpload.size(); i++) {
					FileUpload file = fileUpload.get(i);
					String uploadId = file.getId();
					String newUrl = appBUserService.findNewUrl(uploadId);
					AppPhotoMer appPhotoMer = new AppPhotoMer();
					appPhotoMer.setUrl(newUrl);
					appPhotoMer.setIndex(i+1);
					appPhotoMer.setUpId(Integer.parseInt(appUpMerchants.getId()));
					appPhotoMer.setCreateTime(new Date());
					appPhotoMer.setUpdateTime(new Date());
					appPhotoMerService.insertNew(appPhotoMer);
				}
			}
			return renderResult(Global.TRUE, text("保存通过图片上架的4s店表成功！"));
		} catch (Exception e) {
			logger.error("#### 保存图片上传4S店失败 e={}", e);
			return renderResult(Global.FALSE, text("保存失败请联系管理员！"));
		}
		//appUpMerchantsService.save(appUpMerchants);
	}
	
	/**
	 * 删除通过图片上架的4s店表
	 */
	@RequiresPermissions("app:appUpMerchants:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(AppUpMerchants appUpMerchants) {
		appUpMerchantsService.delete(appUpMerchants);
		return renderResult(Global.TRUE, text("删除通过图片上架的4s店表成功！"));
	}


	@RequestMapping("sendMessage")
	@ResponseBody
	public String sendMessage() {
		try {
			//挨到挨到发短信
			List<String> phoneList = appUpMerchantsService.findAllPhone();
			//查询到所有电话。
			for (String s : phoneList) {
				huaweiSmsService.sendSmsByTemplate("11", s, "");
			}
			return renderResult(Global.TRUE, text("电话通知成功！"));
		} catch (Exception e) {
			e.printStackTrace();
			return renderResult(Global.FALSE, text("操作失败,请联系管理员！"));
		}

	}



	
}
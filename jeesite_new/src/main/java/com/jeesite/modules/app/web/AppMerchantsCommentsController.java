/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.modules.constant2.PicTypeEnum;
import com.jeesite.modules.app.entity.AppImg;
import com.jeesite.modules.app.service.AppImgService;
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
import com.jeesite.modules.app.entity.AppMerchantsComments;
import com.jeesite.modules.app.service.AppMerchantsCommentsService;

import java.util.ArrayList;
import java.util.List;

/**
 * 商户评论表Controller
 * @author zcq
 * @version 2019-08-07
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appMerchantsComments")
public class AppMerchantsCommentsController extends BaseController {

	@Autowired
	private AppMerchantsCommentsService appMerchantsCommentsService;

	@Autowired
	private AppImgService appImgService;

	private String id;


	/**
	 * 获取数据
	 */
	@ModelAttribute
	public AppMerchantsComments get(Long id, boolean isNewRecord) {
		return appMerchantsCommentsService.get(String.valueOf(id), isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("app:appMerchantsComments:view")
	@RequestMapping(value = {"list", ""})
	public String list(AppMerchantsComments appMerchantsComments, Model model) {
		id = appMerchantsComments.getId();
		AppMerchantsComments appMerchantsComments1 = new AppMerchantsComments();
		model.addAttribute("appMerchantsComments", appMerchantsComments1);
		return "modules/app/appMerchantsCommentsList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("app:appMerchantsComments:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AppMerchantsComments> listData(AppMerchantsComments appMerchantsComments, HttpServletRequest request, HttpServletResponse response) {
		appMerchantsComments.setPage(new Page<>(request, response));
		Page<AppMerchantsComments> page = appMerchantsCommentsService.findPage(appMerchantsComments);
		List<AppMerchantsComments> list = page.getList();
		List<AppMerchantsComments> newlist = new ArrayList<>();
		for (AppMerchantsComments merchantsComments : list) {
			if (merchantsComments.getMerchantsId().toString().equals(id)){
				newlist.add(merchantsComments);
			}
		}
		page.setList(newlist);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("app:appMerchantsComments:view")
	@RequestMapping(value = "form")
	public String form(AppMerchantsComments appMerchantsComments, Model model) {
		//获取到商户id
		String id = appMerchantsComments.getId();

		PicTypeEnum partnerCommentImgType = PicTypeEnum.PartnerCommentImgType;
		Integer type = partnerCommentImgType.getType();

		//根据商户id查询到图片
		List<AppImg> img = appImgService.getImg(type, id);
		String ste = "";
		//遍历拿到图片，添加到字段中
		for (AppImg appImg : img) {
			String url = appImg.getUrl();
			ste += url + "|";
		}

		if (ste == "") {
			ste = ".";
		}
		//截取字符串除了最后一位
		String substring = ste.substring(0, ste.length() - 1);
		appMerchantsComments.setUrl(substring);

		appMerchantsComments.setId(null);

		model.addAttribute("appMerchantsComments", appMerchantsComments);
		return "modules/app/appMerchantsCommentsForm";
	}

	/**
	 * 保存商户评论表
	 */
	@RequiresPermissions("app:appMerchantsComments:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated AppMerchantsComments appMerchantsComments) {
		appMerchantsCommentsService.save(appMerchantsComments);
		return renderResult(Global.TRUE, text("保存商户评论表成功！"));
	}
	
	/**
	 * 删除商户评论表
	 */
	@RequiresPermissions("app:appMerchantsComments:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(AppMerchantsComments appMerchantsComments) {
		appMerchantsCommentsService.delete(appMerchantsComments);
		return renderResult(Global.TRUE, text("删除商户评论表成功！"));
	}
	
}
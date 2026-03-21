/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.web;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.idgen.IdGen;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.app.entity.AppVideoComments;
import com.jeesite.modules.app.service.AppVideoCommentsService;
import com.jeesite.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 视频评论表Controller
 * @author zcq
 * @version 2019-08-07
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appVideoComments")
public class AppVideoCommentsController extends BaseController {

	private String id;

	@Autowired
	private AppVideoCommentsService appVideoCommentsService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public AppVideoComments get(String treeCode, boolean isNewRecord) {
		return appVideoCommentsService.get(treeCode, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("app:appVideoComments:view")
	@RequestMapping(value = {"list", ""})
	public String list(AppVideoComments appVideoComments, Model model) {
		AppVideoComments appVideoComments1 = new AppVideoComments();
		id = appVideoComments.getId();
		model.addAttribute("appVideoComments", appVideoComments1);
		return "modules/app/appVideoCommentsList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("app:appVideoComments:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public List<AppVideoComments> listData(AppVideoComments appVideoComments) {
		if (StringUtils.isBlank(appVideoComments.getParentCode())) {
			appVideoComments.setParentCode(AppVideoComments.ROOT_CODE);
		}
		if (StringUtils.isNotBlank(appVideoComments.getTreeName())){
			appVideoComments.setParentCode(null);
		}
		if (StringUtils.isNotBlank(appVideoComments.getCount())){
			appVideoComments.setParentCode(null);
		}
		if (StringUtils.isNotBlank(appVideoComments.getVideoId())){
			appVideoComments.setParentCode(null);
		}
		if (StringUtils.isNotBlank(appVideoComments.getUserId())){
			appVideoComments.setParentCode(null);
		}
		if (StringUtils.isNotBlank(appVideoComments.getThumbsUp())){
			appVideoComments.setParentCode(null);
		}
		List<AppVideoComments> list = appVideoCommentsService.findList(appVideoComments);
		//新创建一个newlist
		ArrayList<AppVideoComments> newlist = new ArrayList<>();
		for (AppVideoComments videoComments : list) {
			if (videoComments.getVideoId().equals(id)){
				newlist.add(videoComments);
			}
		}
		return newlist;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("app:appVideoComments:view")
	@RequestMapping(value = "form")
	public String form(AppVideoComments appVideoComments, Model model) {
		// 创建并初始化下一个节点信息
		appVideoComments = createNextNode(appVideoComments);
		model.addAttribute("appVideoComments", appVideoComments);
		return "modules/app/appVideoCommentsForm";
	}
	
	/**
	 * 创建并初始化下一个节点信息，如：排序号、默认值
	 */
	@RequiresPermissions("app:appVideoComments:edit")
	@RequestMapping(value = "createNextNode")
	@ResponseBody
	public AppVideoComments createNextNode(AppVideoComments appVideoComments) {
		if (StringUtils.isNotBlank(appVideoComments.getParentCode())){
			appVideoComments.setParent(appVideoCommentsService.get(appVideoComments.getParentCode()));
		}
		if (appVideoComments.getIsNewRecord()) {
			AppVideoComments where = new AppVideoComments();
			where.setParentCode(appVideoComments.getParentCode());
			AppVideoComments last = appVideoCommentsService.getLastByParentCode(where);
			// 获取到下级最后一个节点
			if (last != null){
				appVideoComments.setTreeSort(last.getTreeSort() + 30);
				appVideoComments.setTreeCode(IdGen.nextCode(last.getTreeCode()));
			}else if (appVideoComments.getParent() != null){
				appVideoComments.setTreeCode(appVideoComments.getParent().getTreeCode() + "001");
			}
		}
		// 以下设置表单默认数据
		if (appVideoComments.getTreeSort() == null){
			appVideoComments.setTreeSort(AppVideoComments.DEFAULT_TREE_SORT);
		}
		return appVideoComments;
	}

	/**
	 * 保存视频评论表
	 */
	@RequiresPermissions("app:appVideoComments:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated AppVideoComments appVideoComments) {
		appVideoCommentsService.save(appVideoComments);
		return renderResult(Global.TRUE, text("保存视频评论表成功！"));
	}
	
	/**
	 * 删除视频评论表
	 */
	@RequiresPermissions("app:appVideoComments:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(AppVideoComments appVideoComments) {
		appVideoCommentsService.delete(appVideoComments);
		return renderResult(Global.TRUE, text("删除视频评论表成功！"));
	}
	
	/**
	 * 获取树结构数据
	 * @param excludeCode 排除的Code
	 * @param isShowCode 是否显示编码（true or 1：显示在左侧；2：显示在右侧；false or null：不显示）
	 * @return
	 */
	@RequiresPermissions("app:appVideoComments:view")
	@RequestMapping(value = "treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData(String excludeCode, String isShowCode) {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		List<AppVideoComments> list = appVideoCommentsService.findList(new AppVideoComments());
		for (int i=0; i<list.size(); i++){
			AppVideoComments e = list.get(i);
			// 过滤非正常的数据
			if (!AppVideoComments.STATUS_NORMAL.equals(e.getStatus())){
				continue;
			}
			// 过滤被排除的编码（包括所有子级）
			if (StringUtils.isNotBlank(excludeCode)){
				if (e.getId().equals(excludeCode)){
					continue;
				}
				if (e.getParentCodes().contains("," + excludeCode + ",")){
					continue;
				}
			}
			Map<String, Object> map = MapUtils.newHashMap();
			map.put("id", e.getId());
			map.put("pId", e.getParentCode());
			map.put("name", StringUtils.getTreeNodeName(isShowCode, e.getTreeCode(), e.getTreeName()));
			mapList.add(map);
		}
		return mapList;
	}

	/**
	 * 修复表结构相关数据
	 */
	@RequiresPermissions("app:appVideoComments:edit")
	@RequestMapping(value = "fixTreeData")
	@ResponseBody
	public String fixTreeData(AppVideoComments appVideoComments){
		if (!UserUtils.getUser().isAdmin()){
			return renderResult(Global.FALSE, "操作失败，只有管理员才能进行修复！");
		}
		appVideoCommentsService.fixTreeData();
		return renderResult(Global.TRUE, "数据修复成功");
	}
	
}
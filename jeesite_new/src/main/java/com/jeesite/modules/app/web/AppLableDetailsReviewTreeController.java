/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.web;

import java.util.List;
import java.util.Map;

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
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.idgen.IdGen;
import com.jeesite.modules.sys.utils.UserUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.app.entity.AppLableDetailsReviewTree;
import com.jeesite.modules.app.service.AppLableDetailsReviewTreeService;

/**
 * 标签和明细表Controller
 * @author zcq
 * @version 2019-09-28
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appLableDetailsReviewTree")
public class AppLableDetailsReviewTreeController extends BaseController {

	@Autowired
	private AppLableDetailsReviewTreeService appLableDetailsReviewTreeService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public AppLableDetailsReviewTree get(String lableCode, boolean isNewRecord) {
		return appLableDetailsReviewTreeService.get(lableCode, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("app:appLableDetailsReviewTree:view")
	@RequestMapping(value = {"list", ""})
	public String list(AppLableDetailsReviewTree appLableDetailsReviewTree, Model model) {
		model.addAttribute("appLableDetailsReviewTree", appLableDetailsReviewTree);
		return "modules/app/appLableDetailsReviewTreeList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("app:appLableDetailsReviewTree:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public List<AppLableDetailsReviewTree> listData(AppLableDetailsReviewTree appLableDetailsReviewTree) {
		if (StringUtils.isBlank(appLableDetailsReviewTree.getParentCode())) {
			appLableDetailsReviewTree.setParentCode(AppLableDetailsReviewTree.ROOT_CODE);
		}
		if (StringUtils.isNotBlank(appLableDetailsReviewTree.getLableName())){
			appLableDetailsReviewTree.setParentCode(null);
		}
		if (StringUtils.isNotBlank(appLableDetailsReviewTree.getState())){
			appLableDetailsReviewTree.setParentCode(null);
		}
		List<AppLableDetailsReviewTree> list = appLableDetailsReviewTreeService.findList(appLableDetailsReviewTree);
		return list;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("app:appLableDetailsReviewTree:view")
	@RequestMapping(value = "form")
	public String form(AppLableDetailsReviewTree appLableDetailsReviewTree, Model model) {
		// 创建并初始化下一个节点信息
		appLableDetailsReviewTree = createNextNode(appLableDetailsReviewTree);
		model.addAttribute("appLableDetailsReviewTree", appLableDetailsReviewTree);
		return "modules/app/appLableDetailsReviewTreeForm";
	}
	
	/**
	 * 创建并初始化下一个节点信息，如：排序号、默认值
	 */
	@RequiresPermissions("app:appLableDetailsReviewTree:edit")
	@RequestMapping(value = "createNextNode")
	@ResponseBody
	public AppLableDetailsReviewTree createNextNode(AppLableDetailsReviewTree appLableDetailsReviewTree) {
		if (StringUtils.isNotBlank(appLableDetailsReviewTree.getParentCode())){
			appLableDetailsReviewTree.setParent(appLableDetailsReviewTreeService.get(appLableDetailsReviewTree.getParentCode()));
		}
		if (appLableDetailsReviewTree.getIsNewRecord()) {
			AppLableDetailsReviewTree where = new AppLableDetailsReviewTree();
			where.setParentCode(appLableDetailsReviewTree.getParentCode());
			AppLableDetailsReviewTree last = appLableDetailsReviewTreeService.getLastByParentCode(where);
			// 获取到下级最后一个节点
			if (last != null){
				appLableDetailsReviewTree.setTreeSort(last.getTreeSort() + 30);
				appLableDetailsReviewTree.setLableCode(IdGen.nextCode(last.getLableCode()));
			}else if (appLableDetailsReviewTree.getParent() != null){
				appLableDetailsReviewTree.setLableCode(appLableDetailsReviewTree.getParent().getLableCode() + "001");
			}
		}
		// 以下设置表单默认数据
		if (appLableDetailsReviewTree.getTreeSort() == null){
			appLableDetailsReviewTree.setTreeSort(AppLableDetailsReviewTree.DEFAULT_TREE_SORT);
		}
		return appLableDetailsReviewTree;
	}

	/**
	 * 保存标签和明细表
	 */
	@RequiresPermissions("app:appLableDetailsReviewTree:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated AppLableDetailsReviewTree appLableDetailsReviewTree) {
		appLableDetailsReviewTreeService.save(appLableDetailsReviewTree);
		return renderResult(Global.TRUE, text("保存标签和明细表成功！"));
	}
	
	/**
	 * 删除标签和明细表
	 */
	@RequiresPermissions("app:appLableDetailsReviewTree:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(AppLableDetailsReviewTree appLableDetailsReviewTree) {
		appLableDetailsReviewTreeService.delete(appLableDetailsReviewTree);
		return renderResult(Global.TRUE, text("删除标签和明细表成功！"));
	}
	
	/**
	 * 获取树结构数据
	 * @param excludeCode 排除的Code
	 * @param isShowCode 是否显示编码（true or 1：显示在左侧；2：显示在右侧；false or null：不显示）
	 * @return
	 */
	@RequiresPermissions("app:appLableDetailsReviewTree:view")
	@RequestMapping(value = "treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData(String excludeCode, String isShowCode) {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		List<AppLableDetailsReviewTree> list = appLableDetailsReviewTreeService.findList(new AppLableDetailsReviewTree());
		for (int i=0; i<list.size(); i++){
			AppLableDetailsReviewTree e = list.get(i);
			// 过滤非正常的数据
			if (!AppLableDetailsReviewTree.STATUS_NORMAL.equals(e.getStatus())){
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
			map.put("name", StringUtils.getTreeNodeName(isShowCode, e.getLableCode(), e.getLableName()));
			mapList.add(map);
		}
		return mapList;
	}

	/**
	 * 修复表结构相关数据
	 */
	@RequiresPermissions("app:appLableDetailsReviewTree:edit")
	@RequestMapping(value = "fixTreeData")
	@ResponseBody
	public String fixTreeData(AppLableDetailsReviewTree appLableDetailsReviewTree){
		if (!UserUtils.getUser().isAdmin()){
			return renderResult(Global.FALSE, "操作失败，只有管理员才能进行修复！");
		}
		appLableDetailsReviewTreeService.fixTreeData();
		return renderResult(Global.TRUE, "数据修复成功");
	}

	@RequestMapping(value = "pass")
	@ResponseBody
	public String pass(String lableCode){
		//获取到lableCode
		//根据lablecode查询到对应数据
		//修改状态
		try {
			appLableDetailsReviewTreeService.updateState(lableCode);
			return renderResult(Global.TRUE, "审核成功");
		} catch (Exception e) {
			return renderResult(Global.FALSE, "审核失败");
		}

	}

	@RequestMapping(value = "failure")
	@ResponseBody
	public String failure(String code,String reason){
		//修改状态和添加原因
		appLableDetailsReviewTreeService.updateAndReason(code,reason);
		return renderResult(Global.FALSE, "审核未通过");
	}
}
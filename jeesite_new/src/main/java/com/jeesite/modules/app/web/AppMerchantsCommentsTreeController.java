/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jeesite.modules.constant2.PicTypeEnum;
import com.jeesite.modules.app.entity.AppImg;
import com.jeesite.modules.app.entity.AppMerchantsCommentsTree;
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
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.idgen.IdGen;
import com.jeesite.modules.sys.utils.UserUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.app.service.AppMerchantsCommentsTreeService;

/**
 * 商户评论树表Controller
 * @author zcq
 * @version 2019-08-12
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appMerchantsCommentsTree")
public class AppMerchantsCommentsTreeController extends BaseController {

	@Autowired
	private AppMerchantsCommentsTreeService appMerchantsCommentsTreeService;

	@Autowired
	private AppImgService appImgService;

	private String id;

	private String Code;
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public AppMerchantsCommentsTree get(String commentsCode, boolean isNewRecord) {
		return appMerchantsCommentsTreeService.get(commentsCode, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("app:appMerchantsCommentsTree:view")
	@RequestMapping(value = {"list", ""})
	public String list(AppMerchantsCommentsTree appMerchantsCommentsTree, Model model) {
		AppMerchantsCommentsTree appMerchantsCommentsTree1 = new AppMerchantsCommentsTree();
		id = appMerchantsCommentsTree.getId();
		model.addAttribute("appMerchantsCommentsTree", appMerchantsCommentsTree1);
		return "modules/app/appMerchantsCommentsTreeList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("app:appMerchantsCommentsTree:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public List<AppMerchantsCommentsTree> listData(AppMerchantsCommentsTree appMerchantsCommentsTree) {
		if (StringUtils.isBlank(appMerchantsCommentsTree.getParentCode())) {
			appMerchantsCommentsTree.setParentCode(AppMerchantsCommentsTree.ROOT_CODE);
		}
		if (StringUtils.isNotBlank(appMerchantsCommentsTree.getCommentsName())){
			appMerchantsCommentsTree.setParentCode(null);
		}
		if (StringUtils.isNotBlank(appMerchantsCommentsTree.getUserId())){
			appMerchantsCommentsTree.setParentCode(null);
		}
		if (StringUtils.isNotBlank(appMerchantsCommentsTree.getUserId())){
			appMerchantsCommentsTree.setParentCode(null);
		}
		if (StringUtils.isNotBlank(appMerchantsCommentsTree.getState())){
			appMerchantsCommentsTree.setParentCode(null);
		}
		List<AppMerchantsCommentsTree> list = appMerchantsCommentsTreeService.findList(appMerchantsCommentsTree);
		//创建一个新的list，把对应得数据放到新的list里面
		ArrayList<AppMerchantsCommentsTree> newlist = new ArrayList<>();
		for (AppMerchantsCommentsTree merchantsCommentsTree : list) {
			//id和商户id一致
			if (merchantsCommentsTree.getUserId().equals(id)){
				newlist.add(merchantsCommentsTree);
			}
		}
		return newlist;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("app:appMerchantsCommentsTree:view")
	@RequestMapping(value = "form")
	public String form(AppMerchantsCommentsTree appMerchantsCommentsTree, Model model) {

		//判断修改状态
		if (appMerchantsCommentsTree.getCommentsCode()==null) {
			// 创建并初始化下一个节点信息
			appMerchantsCommentsTree = createNextNode(appMerchantsCommentsTree);
			model.addAttribute("appMerchantsCommentsTree", appMerchantsCommentsTree);
			return "modules/app/appMerchantsCommentsTreeForm";
		}else {
			String id = appMerchantsCommentsTree.getId();
			//修改数据
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
			appMerchantsCommentsTree.setUrl(substring);

			//放到新对象中
			//创建并初始化下一个节点信息
			appMerchantsCommentsTree= createNextNode(appMerchantsCommentsTree);
			Code = appMerchantsCommentsTree.getCommentsCode();
			appMerchantsCommentsTree.setId(null);
			model.addAttribute("appMerchantsTree", appMerchantsCommentsTree);
			return "modules/app/appMerchantsCommentsTreeForm";
		}
	}
	
	/**
	 * 创建并初始化下一个节点信息，如：排序号、默认值
	 */
	@RequiresPermissions("app:appMerchantsCommentsTree:edit")
	@RequestMapping(value = "createNextNode")
	@ResponseBody
	public AppMerchantsCommentsTree createNextNode(AppMerchantsCommentsTree appMerchantsCommentsTree) {
		if (StringUtils.isNotBlank(appMerchantsCommentsTree.getParentCode())){
			appMerchantsCommentsTree.setParent(appMerchantsCommentsTreeService.get(appMerchantsCommentsTree.getParentCode()));
		}
		if (appMerchantsCommentsTree.getIsNewRecord()) {
			AppMerchantsCommentsTree where = new AppMerchantsCommentsTree();
			where.setParentCode(appMerchantsCommentsTree.getParentCode());
			AppMerchantsCommentsTree last = appMerchantsCommentsTreeService.getLastByParentCode(where);
			// 获取到下级最后一个节点
			if (last != null){
				appMerchantsCommentsTree.setTreeSort(last.getTreeSort() + 30);
				appMerchantsCommentsTree.setCommentsCode(IdGen.nextCode(last.getCommentsCode()));
				//设置编码名称
				String commentsName = last.getCommentsName();
				appMerchantsCommentsTree.setCommentsName(commentsName+1);
			}else if (appMerchantsCommentsTree.getParent() != null){
				appMerchantsCommentsTree.setCommentsCode(appMerchantsCommentsTree.getParent().getCommentsCode() + "001");
				appMerchantsCommentsTree.setCommentsName(appMerchantsCommentsTree.getParent().getCommentsName()+2);
			}
		}
		// 以下设置表单默认数据
		if (appMerchantsCommentsTree.getTreeSort() == null){
			appMerchantsCommentsTree.setTreeSort(AppMerchantsCommentsTree.DEFAULT_TREE_SORT);
		}
		return appMerchantsCommentsTree;
	}

	/**
	 * 保存商户评论树表
	 */
	@RequiresPermissions("app:appMerchantsCommentsTree:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated AppMerchantsCommentsTree appMerchantsCommentsTree) {
		//code为null就是添加新数据
		if (Code==null){
			appMerchantsCommentsTreeService.save(appMerchantsCommentsTree);
			return renderResult(Global.TRUE, text("保存商户评论树表成功！"));
		}else {
			AppMerchantsCommentsTree appMerchantsCommentsTree1 = appMerchantsCommentsTreeService.get(Code);
			Code = null;
			String content = appMerchantsCommentsTree.getContent();
			String state = appMerchantsCommentsTree.getState();
			Integer serviceScore = appMerchantsCommentsTree.getServiceScore();
			Integer efficiensyScore = appMerchantsCommentsTree.getEfficiensyScore();

			appMerchantsCommentsTree1.setContent(content);
			appMerchantsCommentsTree1.setState(state);
			appMerchantsCommentsTree1.setServiceScore(serviceScore);
			appMerchantsCommentsTree1.setEfficiensyScore(efficiensyScore);
			appMerchantsCommentsTreeService.save(appMerchantsCommentsTree1);
			return renderResult(Global.TRUE, text("保存商户评论树表成功！"));
		}
	}
	
	/**
	 * 删除商户评论树表
	 */
	@RequiresPermissions("app:appMerchantsCommentsTree:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(AppMerchantsCommentsTree appMerchantsCommentsTree) {
		appMerchantsCommentsTreeService.delete(appMerchantsCommentsTree);
		return renderResult(Global.TRUE, text("删除商户评论树表成功！"));
	}
	
	/**
	 * 获取树结构数据
	 * @param excludeCode 排除的Code
	 * @param isShowCode 是否显示编码（true or 1：显示在左侧；2：显示在右侧；false or null：不显示）
	 * @return
	 */
	@RequiresPermissions("app:appMerchantsCommentsTree:view")
	@RequestMapping(value = "treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData(String excludeCode, String isShowCode) {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		List<AppMerchantsCommentsTree> list = appMerchantsCommentsTreeService.findList(new AppMerchantsCommentsTree());
		for (int i=0; i<list.size(); i++){
			AppMerchantsCommentsTree e = list.get(i);
			// 过滤非正常的数据
			if (!AppMerchantsCommentsTree.STATUS_NORMAL.equals(e.getStatus())){
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
			map.put("name", StringUtils.getTreeNodeName(isShowCode, e.getCommentsCode(), e.getCommentsName()));
			mapList.add(map);
		}
		return mapList;
	}

	/**
	 * 修复表结构相关数据
	 */
	@RequiresPermissions("app:appMerchantsCommentsTree:edit")
	@RequestMapping(value = "fixTreeData")
	@ResponseBody
	public String fixTreeData(AppMerchantsCommentsTree appMerchantsCommentsTree){
		if (!UserUtils.getUser().isAdmin()){
			return renderResult(Global.FALSE, "操作失败，只有管理员才能进行修复！");
		}
		appMerchantsCommentsTreeService.fixTreeData();
		return renderResult(Global.TRUE, "数据修复成功");
	}
	
}
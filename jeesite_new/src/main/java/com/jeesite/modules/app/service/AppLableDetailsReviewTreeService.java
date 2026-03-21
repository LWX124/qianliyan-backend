/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.service;

import com.jeesite.common.service.TreeService;
import com.jeesite.modules.app.dao.AppLableDetailsReviewTreeDao;
import com.jeesite.modules.app.entity.AppBUser;
import com.jeesite.modules.app.entity.AppCleanPriceDetail;
import com.jeesite.modules.app.entity.AppLable;
import com.jeesite.modules.app.entity.AppLableDetailsReviewTree;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 标签和明细表Service
 * @author zcq
 * @version 2019-09-28
 */
@Service
@Transactional(readOnly=true)
public class AppLableDetailsReviewTreeService extends TreeService<AppLableDetailsReviewTreeDao, AppLableDetailsReviewTree> {

	@Resource
	private AppCleanPriceDetailService appCleanPriceDetailService;

	@Resource
	private AppLableDetailsReviewTreeDao appLableDetailsReviewTreeDao;

	@Resource
	private AppBUserService appBUserService;

	@Resource
	private AppLableService appLableService;
	
	/**
	 * 获取单条数据
	 * @param appLableDetailsReviewTree
	 * @return
	 */
	@Override
	public AppLableDetailsReviewTree get(AppLableDetailsReviewTree appLableDetailsReviewTree) {
		return super.get(appLableDetailsReviewTree);
	}
	
	/**
	 * 查询列表数据
	 * @param appLableDetailsReviewTree
	 * @return
	 */
	@Override
	public List<AppLableDetailsReviewTree> findList(AppLableDetailsReviewTree appLableDetailsReviewTree) {
		return super.findList(appLableDetailsReviewTree);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param appLableDetailsReviewTree
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(AppLableDetailsReviewTree appLableDetailsReviewTree) {
		super.save(appLableDetailsReviewTree);
	}
	
	/**
	 * 更新状态
	 * @param appLableDetailsReviewTree
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(AppLableDetailsReviewTree appLableDetailsReviewTree) {
		super.updateStatus(appLableDetailsReviewTree);
	}
	
	/**
	 * 删除数据
	 * @param appLableDetailsReviewTree
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(AppLableDetailsReviewTree appLableDetailsReviewTree) {
		super.delete(appLableDetailsReviewTree);
	}


	//修改审核状态
	@Transactional(readOnly = false)
	public void updateState(String lableCode) {
		//根据lableCode查询到对应数据
		AppLableDetailsReviewTree appLable= get(lableCode);
		if (appLable.getLableId()==4){
			//修改审核通过都为4
			List<AppCleanPriceDetail> cleanPriceDetails = appCleanPriceDetailService.finduserB(appLable.getUserBId());
			for (AppCleanPriceDetail detail : cleanPriceDetails) {
				detail.setState(1);
				appCleanPriceDetailService.update(detail);
			}
		}
		appLable.setState("1");
		update(appLable);

/*
		//审核成功之后修改状态并且展示到标签里面
		//新增标签
		AppMerchantsLable appMerchantsLable = new AppMerchantsLable();
		appMerchantsLable.setMerchantsId(Long.valueOf(appLable.getUserBId()));
		appMerchantsLable.setLableId(Long.valueOf(lableCode));
		//找到比例最高的一个
		Double maxRebates = appLableDetailsReviewTreeDao.findMaxRebates(lableCode);
		appMerchantsLable.setRebates(maxRebates);
		appMerchantsLable.setCreateTime(new Date());
		appMerchantsLable.setUpdateTime(new Date());*/
	}

	@Transactional(readOnly = false)
	public void updateAndReason(String code, String reason) {
		AppLableDetailsReviewTree appLable = get(code);
		if (appLable.getLableId()==4){
			List<AppCleanPriceDetail> cleanPriceDetails = appCleanPriceDetailService.finduserB(appLable.getUserBId());
			for (AppCleanPriceDetail cleanPriceDetail : cleanPriceDetails) {
				cleanPriceDetail.setState(0);
				appCleanPriceDetailService.update(cleanPriceDetail);
			}
		}
		appLable.setState("2");
		appLable.setReson(reason);
		update(appLable);

	}

	public List<AppLableDetailsReviewTree> findLable(String id) {
		return appLableDetailsReviewTreeDao.findLable(id);
	}

	@Transactional(readOnly=false)
	public AppLableDetailsReviewTree addFirst(Integer user_b_id) {

		AppLableDetailsReviewTree lableTree = new AppLableDetailsReviewTree();

		//先根据商户id查询是否有商户
		List<AppLableDetailsReviewTree> lableDetailsReview = appLableDetailsReviewTreeDao.finduserBId(user_b_id);
		//如果为空就是第一次添加
		if (lableDetailsReview.isEmpty()) {
			lableTree.setParentCode("0");
			//拿到最后一个一级标签
			Integer lastFirstLable = getLastLable();
			if (lastFirstLable == null) {
				lastFirstLable = 0;
			}
			//根据id获取到对象
			BigDecimal bigDecimal;
			AppLableDetailsReviewTree lastlable = get(String.valueOf(lastFirstLable));
			if (lastlable == null) {
				bigDecimal = BigDecimal.ZERO;
			} else {
				bigDecimal = BigDecimal.valueOf(lastlable.getTreeSort());
			}
			lableTree.setLableCode(String.valueOf(lastFirstLable + 1));
			lableTree.setParentCode("0");
			lableTree.setParentCodes("0" + ",");
			//设置tree_sort,tree_sorts
			BigDecimal a = new BigDecimal(30);
			lableTree.setTreeSort(bigDecimal.add(a).intValue());
			String s = String.valueOf(bigDecimal.add(a));
			int i = Integer.parseInt(s);
			String s1 = bySting(s, i);
			lableTree.setTreeSorts(s1 + ",");

			//查询是否最末级
			List<AppLableDetailsReviewTree> treeLeaf = findidTreeleaf(lableTree.getLableCode());
			if (treeLeaf.isEmpty()) {
				lableTree.setTreeLeaf("1");
			} else {
				lableTree.setTreeLeaf("0");
			}
			//设置节点层次级别
			lableTree.setTreeLevel(0);
			//tree_names
			lableTree.setTreeNames(String.valueOf(lastFirstLable + 1));
			lableTree.setShow(0L);
			lableTree.setUserBId(String.valueOf(user_b_id));
			lableTree.setStatus("0");
			lableTree.setCreateDate(new Date());
			lableTree.setUpdateDate(new Date());
			//通过userBid查询到商户名称
			AppBUser appUserEntity = appBUserService.get(String.valueOf(user_b_id));
			System.out.println(user_b_id);
			String merchantsName = appUserEntity.getMerchantsName();
			lableTree.setLableName(merchantsName);
			appLableDetailsReviewTreeDao.add(lableTree);
			return lableTree;
		}


		return null;
	}

	private List<AppLableDetailsReviewTree> findidTreeleaf(String lableCode) {
		return appLableDetailsReviewTreeDao.findIdTreeLeaf(lableCode);
	}


	private Integer getLastLable() {
		return appLableDetailsReviewTreeDao.getLastLable();
	}


	private String bySting(String str, Integer stelength) {
		//int数据是90
		//字符串长度是10
		int steleng = str.length();
		steleng = 10;
		int length = String.valueOf(stelength).length();
		if (length < steleng) {
			while (length < steleng) {
				StringBuffer sb = new StringBuffer();
				sb.append("0").append(str);//左补0
				str = sb.toString();
				length = str.length();
			}
		}
		return str;
	}

	@Transactional(readOnly=false)
	public void addCleanSecond(Integer userBId, int lableId, String o) {
		//获取到对应服务得名称
		AppLable lableEntity = appLableService.get(String.valueOf(lableId));
		String label = lableEntity.getLabel();
		Integer index = lableEntity.getIndex();
		AppLableDetailsReviewTree lableDetailsReview = new AppLableDetailsReviewTree();
		//根据商户id查询到一级数据
		List<AppLableDetailsReviewTree> lablemerchants = finduserandParent(userBId,0);
		for (AppLableDetailsReviewTree lablemerchant : lablemerchants) {
			//保存服务
			//一级内容得code
			String lableCode = lablemerchant.getLableCode();
			//找到下面得所有二级
			List<AppLableDetailsReviewTree> list = findidTreeleaf(lableCode);
			for (int i = 0; i < list.size(); i++) {
				if (i == list.size() - 1) {
					AppLableDetailsReviewTree lastSecond = list.get(i);
					String lableCode1 = lastSecond.getLableCode();
					lableDetailsReview.setLableCode(String.valueOf(Integer.valueOf(lableCode1) + 1));
					lableDetailsReview.setLableName(label);
					lableDetailsReview.setTreeSort((list.size() + 1) * 30);

				}
			}
			if (list.isEmpty()) {
				String lableCode1 = lablemerchant.getLableCode();
				lableDetailsReview.setLableCode(lableCode1 + "001");
				lableDetailsReview.setLableName(label);
				lableDetailsReview.setTreeSort(30);
			}
			//保存parent_codes
			lableDetailsReview.setParentCode(lableCode);
			lableDetailsReview.setParentCodes(lablemerchant.getParentCodes() + lableCode + ",");
			//保存tree_sorts
			String s = lableDetailsReview.getTreeSort().toString();
			int i = Integer.parseInt(s);
			String treeSorts = lablemerchant.getTreeSorts();
			String s1 = bySting(s, i);
			lableDetailsReview.setTreeSorts(treeSorts + s1 + ",");
			lableDetailsReview.setTreeLeaf("1");
			//设置tree_level
			BigDecimal treeLevel = new BigDecimal(lablemerchant.getTreeLevel());
			BigDecimal v = new BigDecimal(1);
			lableDetailsReview.setTreeLevel(treeLevel.add(v).intValue());
			//tree_names
			String treeNames = lablemerchant.getTreeNames();
			lableDetailsReview.setTreeNames(treeNames + "/" + lableDetailsReview.getLableCode());

			lableDetailsReview.setState(String.valueOf(1));
			lableDetailsReview.setShow(1L);
			lableDetailsReview.setStatus("0");
			lableDetailsReview.setLableId(lableId);
			lableDetailsReview.setUserBId(String.valueOf(userBId));
			lableDetailsReview.setIndex(index);
			lableDetailsReview.setCleanType(99);
			lableDetailsReview.setCreateDate(new Date());
			lableDetailsReview.setUpdateDate(new Date());

			lablemerchant.setTreeLeaf("0");
			update(lablemerchant);
			appLableDetailsReviewTreeDao.add(lableDetailsReview);
		}
	}

	@Transactional(readOnly=false)
	public void addRescueSecond(Integer userBId, int lableId, String o) {
		//获取到对应服务得名称
		AppLable lableEntity = appLableService.get(String.valueOf(lableId));
		String label = lableEntity.getLabel();
		Integer index = lableEntity.getIndex();
		AppLableDetailsReviewTree lableDetailsReview = new AppLableDetailsReviewTree();
		//根据商户id查询到一级数据
		List<AppLableDetailsReviewTree> lablemerchants = finduserandParent(userBId,0);
		for (AppLableDetailsReviewTree lablemerchant : lablemerchants) {
			//保存服务
			//一级内容得code
			String lableCode = lablemerchant.getLableCode();
			//找到下面得所有二级
			List<AppLableDetailsReviewTree> list = findidTreeleaf(lableCode);
			for (int i = 0; i < list.size(); i++) {
				if (i == list.size() - 1) {
					AppLableDetailsReviewTree lastSecond = list.get(i);
					String lableCode1 = lastSecond.getLableCode();
					lableDetailsReview.setLableCode(String.valueOf(Integer.valueOf(lableCode1) + 1));
					lableDetailsReview.setLableName(label);
					lableDetailsReview.setTreeSort((list.size() + 1) * 30);

				}
			}
			if (list.isEmpty()) {
				String lableCode1 = lablemerchant.getLableCode();
				lableDetailsReview.setLableCode(lableCode1 + "001");
				lableDetailsReview.setLableName(label);
				lableDetailsReview.setTreeSort(30);
			}
			//保存parent_codes
			lableDetailsReview.setParentCode(lableCode);
			lableDetailsReview.setParentCodes(lablemerchant.getParentCodes() + lableCode + ",");
			//保存tree_sorts
			String s = lableDetailsReview.getTreeSort().toString();
			int i = Integer.parseInt(s);
			String treeSorts = lablemerchant.getTreeSorts();
			String s1 = bySting(s, i);
			lableDetailsReview.setTreeSorts(treeSorts + s1 + ",");
			lableDetailsReview.setTreeLeaf("1");
			//设置tree_level
			BigDecimal treeLevel = new BigDecimal(lablemerchant.getTreeLevel());
			BigDecimal v = new BigDecimal(1);
			lableDetailsReview.setTreeLevel(treeLevel.add(v).intValue());
			//tree_names
			String treeNames = lablemerchant.getTreeNames();
			lableDetailsReview.setTreeNames(treeNames + "/" + lableDetailsReview.getLableCode());

			lableDetailsReview.setState(String.valueOf(1));
			lableDetailsReview.setShow(1L);
			lableDetailsReview.setStatus("1");
			lableDetailsReview.setLableId(lableId);
			lableDetailsReview.setCleanType(77);
			lableDetailsReview.setUserBId(String.valueOf(userBId));
			lableDetailsReview.setIndex(index);
			lableDetailsReview.setCreateDate(new Date());
			lableDetailsReview.setUpdateDate(new Date());

			if (lablemerchant.getTreeLeaf().equals("1")){
				lablemerchant.setTreeLeaf("0");
				update(lablemerchant);
			}
//			insert(lableDetailsReview);
			appLableDetailsReviewTreeDao.add(lableDetailsReview);
		}
	}

	private List<AppLableDetailsReviewTree> finduserandParent(Integer userBId, int i) {
		return appLableDetailsReviewTreeDao.finduserandParent(userBId,i);
	}


	public AppLableDetailsReviewTree selectLable(Integer userBId, int lableId, int show){
		return appLableDetailsReviewTreeDao.selectLable(userBId,lableId,show);
	}


	public List<Integer> selectAllAccident() {
		return appLableDetailsReviewTreeDao.selectAllAccident();
	}

    public List<BigDecimal> selectAllList(Integer userBId) {
        return appLableDetailsReviewTreeDao.selectAllList(userBId);
    }
}
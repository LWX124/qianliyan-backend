/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import com.jeesite.common.entity.TreeEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

import java.math.BigDecimal;

/**
 * 标签和明细表Entity
 * @author zcq
 * @version 2019-09-28
 */
@Table(name="app_lable_details_review_tree", alias="a", columns={
		@Column(name="lable_code", attrName="lableCode", label="节点编码", isPK=true),
		@Column(name="lable_name", attrName="lableName", label="节点名称", queryType=QueryType.LIKE, isTreeName=true),
		@Column(includeEntity=TreeEntity.class),
		@Column(name="status", attrName="status", label="状态"),
		@Column(name="parent_code", attrName="parentCode", label="节点上级编码"),
		@Column(name="parent_codes", attrName="parentCodes", label="节点所有上级编码"),
		@Column(name="tree_sort", attrName="treeSort", label="当前层级排序号"),
		@Column(name="tree_sorts", attrName="treeSorts", label="树节点完整排序号"),
		@Column(name="tree_leaf", attrName="treeLeaf", label="是否最末级"),
		@Column(name="tree_level", attrName="treeLevel", label="节点层次级别"),
		@Column(name="clean_type", attrName="clean_type", label="清洗类型"),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="修改时间", isQuery=false),
		@Column(name="lable_id", attrName="lableId", label="标签id", isQuery=false),
		@Column(name="lable_details_id", attrName="lableDetailsId", label="标签详情id", isQuery=false),
		@Column(name="rebates", attrName="rebates", label="返点比例", isQuery=false),
		@Column(name="user_b_id", attrName="userBId", label="商户id", isQuery=false),
		@Column(name="original_price", attrName="originalPrice", label="原价", isQuery=false),
		@Column(name="preferential_price", attrName="preferentialPrice", label="优惠价", isQuery=false),
		@Column(name="thrie_price", attrName="thriePrice", label="到手价", isQuery=false),
		@Column(name="show", attrName="show", label="是否展示", isQuery=false),
		@Column(name="index", attrName="index", label="排序"),
		@Column(name="state", attrName="state", label="审核状态", comment="审核状态(0.未审核，1.审核通过，3.审核未通过)"),
		@Column(name="reson", attrName="reson", label="审核未通过原因", isQuery=false),
}, orderBy="a.tree_sorts, a.lable_code"
)
public class AppLableDetailsReviewTree extends TreeEntity<AppLableDetailsReviewTree> {

	private static final long serialVersionUID = 1L;
	private String lableCode;		// 节点编码
	private String lableName;		// 节点名称
	private String parentCode;
	private String parentCodes;
	private Integer treeSort;
	private String treeSorts;
	private String treeLeaf;
	private Integer treeLevel;
	private String status;
	private Integer lableId;		// 标签id
	private Integer lableDetailsId;		// 标签详情id
	private Double rebates;		// 返点比例
	private String userBId;		// 商户id
	private Long show;		// 是否展示
	private String state;		// 审核状态(0.未审核，1.审核通过，3.审核未通过)
	private String reson;		// 审核未通过原因
	private Integer index;
	/**
	 * 原价
	 */
	private BigDecimal originalPrice;
	/**
	 * 优惠价
	 */
	private BigDecimal preferentialPrice;
	/**
	 * 到手价
	 */
	private BigDecimal thriePrice;
	private Integer cleanType;


	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	@Override
	public String getParentCode() {
		return parentCode;
	}

	@Override
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	@Override
	public String getParentCodes() {
		return parentCodes;
	}

	@Override
	public void setParentCodes(String parentCodes) {
		this.parentCodes = parentCodes;
	}

	@Override
	public Integer getTreeSort() {
		return treeSort;
	}

	public void setTreeSort(Integer treeSort) {
		this.treeSort = treeSort;
	}

	@Override
	public String getTreeSorts() {
		return treeSorts;
	}

	@Override
	public void setTreeSorts(String treeSorts) {
		this.treeSorts = treeSorts;
	}

	@Override
	public String getTreeLeaf() {
		return treeLeaf;
	}

	@Override
	public void setTreeLeaf(String treeLeaf) {
		this.treeLeaf = treeLeaf;
	}

	@Override
	public Integer getTreeLevel() {
		return treeLevel;
	}

	public void setTreeLevel(Integer treeLevel) {
		this.treeLevel = treeLevel;
	}

	@Override
	public String getStatus() {
		return status;
	}

	@Override
	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getCleanType() {
		return cleanType;
	}

	public void setCleanType(Integer cleanType) {
		this.cleanType = cleanType;
	}

	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}

	public BigDecimal getPreferentialPrice() {
		return preferentialPrice;
	}

	public void setPreferentialPrice(BigDecimal preferentialPrice) {
		this.preferentialPrice = preferentialPrice;
	}

	public BigDecimal getThriePrice() {
		return thriePrice;
	}

	public void setThriePrice(BigDecimal thriePrice) {
		this.thriePrice = thriePrice;
	}

	public AppLableDetailsReviewTree() {
		this(null);
	}

	public AppLableDetailsReviewTree(String id){
		super(id);
	}

	@Override
	public AppLableDetailsReviewTree getParent() {
		return parent;
	}

	@Override
	public void setParent(AppLableDetailsReviewTree parent) {
		this.parent = parent;
	}

	public String getLableCode() {
		return lableCode;
	}

	public void setLableCode(String lableCode) {
		this.lableCode = lableCode;
	}

	@NotBlank(message="节点名称不能为空")
	@Length(min=0, max=64, message="节点名称长度不能超过 64 个字符")
	public String getLableName() {
		return lableName;
	}

	public void setLableName(String lableName) {
		this.lableName = lableName;
	}

	public Integer getLableId() {
		return lableId;
	}

	public void setLableId(Integer lableId) {
		this.lableId = lableId;
	}

	public Integer getLableDetailsId() {
		return lableDetailsId;
	}

	public void setLableDetailsId(Integer lableDetailsId) {
		this.lableDetailsId = lableDetailsId;
	}

	public Double getRebates() {
		return rebates;
	}

	public void setRebates(Double rebates) {
		this.rebates = rebates;
	}

	public String getUserBId() {
		return userBId;
	}

	public void setUserBId(String userBId) {
		this.userBId = userBId;
	}

	public Long getShow() {
		return show;
	}

	public void setShow(Long show) {
		this.show = show;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Length(min=0, max=100, message="审核未通过原因长度不能超过 100 个字符")
	public String getReson() {
		return reson;
	}

	public void setReson(String reson) {
		this.reson = reson;
	}


	@Override
	public String toString() {
		return "AppLableDetailsReviewTree{" +
				"lableCode='" + lableCode + '\'' +
				", lableName='" + lableName + '\'' +
				", parentCode='" + parentCode + '\'' +
				", parentCodes='" + parentCodes + '\'' +
				", treeSort=" + treeSort +
				", treeSorts='" + treeSorts + '\'' +
				", treeLeaf='" + treeLeaf + '\'' +
				", treeLevel=" + treeLevel +
				", status='" + status + '\'' +
				", lableId=" + lableId +
				", lableDetailsId=" + lableDetailsId +
				", rebates=" + rebates +
				", userBId='" + userBId + '\'' +
				", show=" + show +
				", state='" + state + '\'' +
				", reson='" + reson + '\'' +
				", index=" + index +
				", originalPrice=" + originalPrice +
				", preferentialPrice=" + preferentialPrice +
				", thriePrice=" + thriePrice +
				", cleanType=" + cleanType +
				'}';
	}
}
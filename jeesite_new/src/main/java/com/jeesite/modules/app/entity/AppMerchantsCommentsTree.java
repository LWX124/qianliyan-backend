/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import com.jeesite.common.entity.TreeEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 商户评论树表Entity
 * @author zcq
 * @version 2019-08-12
 */
@Table(name="app_merchants_comments_tree", alias="a", columns={
		@Column(name="comments_code", attrName="commentsCode", label="节点编码", isPK=true),
		@Column(name="comments_name", attrName="commentsName", label="节点名称", queryType=QueryType.LIKE, isTreeName=true),
		@Column(includeEntity=TreeEntity.class),
		@Column(name="user_id", attrName="userId", label="关联用户id"),
		@Column(name="user_b_id", attrName="userBId", label="关联商户id"),
		@Column(name="content", attrName="content", label="评论内容", isQuery=false),
		@Column(name="state", attrName="state", label="评论状态是否展示"),
		@Column(name="service_score", attrName="serviceScore", label="服务分", isQuery=false),
		@Column(name="efficiensy_score", attrName="efficiensyScore", label="效率分", isQuery=false),
		@Column(name="create_date", attrName="createDate", label="create_date", isUpdate=false, isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="update_date", isUpdate=false, isQuery=false),
	},joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=AppUser.class, attrName="this", alias="b",
				on="b.id = a.user_id",
				columns={
						@Column(name="name", attrName="name", label="名称"),
				}),
		@JoinTable(type=Type.LEFT_JOIN, entity=AppBUser.class, attrName="this", alias="c",
				on="c.id = a.user_b_id",
				columns={
						@Column(name="merchants_name", attrName="merchantsName", label="商户名称"),
				}),

}, orderBy="a.tree_sorts, a.comments_code"
)
public class AppMerchantsCommentsTree extends TreeEntity<AppMerchantsCommentsTree> {
	
	private static final long serialVersionUID = 1L;
	private String commentsCode;		// 节点编码
	private String commentsName;		// 节点名称
	private String  userId;		// 关联用户id
	private String userBId;		// 关联商户id
	private String content;		// 评论内容
	private String state;		// 评论状态是否展示
	private Integer serviceScore;		// 服务分
	private Integer efficiensyScore;		// 效率分
	private String name;				//用户名称
	private String merchantsName; 		//商户名称
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMerchantsName() {
		return merchantsName;
	}

	public void setMerchantsName(String merchantsName) {
		this.merchantsName = merchantsName;
	}

	public AppMerchantsCommentsTree() {
		this(null);
	}

	public AppMerchantsCommentsTree(String id){
		super(id);
	}
	
	@Override
	public AppMerchantsCommentsTree getParent() {
		return parent;
	}

	@Override
	public void setParent(AppMerchantsCommentsTree parent) {
		this.parent = parent;
	}
	
	public String getCommentsCode() {
		return commentsCode;
	}

	public void setCommentsCode(String commentsCode) {
		this.commentsCode = commentsCode;
	}
	
	@NotBlank(message="节点名称不能为空")
	@Length(min=0, max=64, message="节点名称长度不能超过 64 个字符")
	public String getCommentsName() {
		return commentsName;
	}

	public void setCommentsName(String commentsName) {
		this.commentsName = commentsName;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserBId() {
		return userBId;
	}

	public void setUserBId(String userBId) {
		this.userBId = userBId;
	}

	@Length(min=0, max=255, message="评论内容长度不能超过 255 个字符")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Length(min=0, max=10, message="评论状态是否展示长度不能超过 10 个字符")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public Integer getServiceScore() {
		return serviceScore;
	}

	public void setServiceScore(Integer serviceScore) {
		this.serviceScore = serviceScore;
	}
	
	public Integer getEfficiensyScore() {
		return efficiensyScore;
	}

	public void setEfficiensyScore(Integer efficiensyScore) {
		this.efficiensyScore = efficiensyScore;
	}

	@Override
	public String toString() {
		return "AppMerchantsCommentsTree{" +
				"commentsCode='" + commentsCode + '\'' +
				", commentsName='" + commentsName + '\'' +
				", userId='" + userId + '\'' +
				", userBId='" + userBId + '\'' +
				", content='" + content + '\'' +
				", state='" + state + '\'' +
				", serviceScore=" + serviceScore +
				", efficiensyScore=" + efficiensyScore +
				", name='" + name + '\'' +
				", merchantsName='" + merchantsName + '\'' +
				", url='" + url + '\'' +
				'}';
	}
}
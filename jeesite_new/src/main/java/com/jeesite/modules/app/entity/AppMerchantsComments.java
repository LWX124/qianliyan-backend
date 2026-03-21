/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;
import java.util.Date;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;

/**
 * 商户评论表Entity
 * @author zcq
 * @version 2019-08-07
 */
@Table(name="app_merchants_comments", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="user_id", attrName="userId", label="关联用户表", isInsert=false),
		@Column(name="merchants_id", attrName="merchantsId", label="管理商户表", isInsert=false),
		@Column(name="content", attrName="content", label="评论内容", isInsert=false, isQuery=false),
		@Column(name="state", attrName="state", label="是否展示该评论", isInsert=false),
		@Column(name="service_score", attrName="serviceScore", label="服务分", isInsert=false, isQuery=false),
		@Column(name="efficiency_score", attrName="efficiencyScore", label="效率分", isInsert=false, isQuery=false),
		@Column(name="creat_time", attrName="creatTime", label="creat_time", isInsert=false),
		@Column(name="update_time", attrName="updateTime", label="update_time", isInsert=false, isQuery=false),
	},joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=AppUser.class, attrName="this", alias="b",
				on="b.id = a.user_id",
				columns={
						@Column(name="name", attrName="name", label="名称"),
				}),
		@JoinTable(type=Type.LEFT_JOIN, entity=AppBUser.class, attrName="this", alias="b",
				on="b.id = a.merchants_id",
				columns={
						@Column(name="merchants_name", attrName="merchantsName", label="商户名称"),
				}),

},  orderBy="a.id DESC"
)
public class AppMerchantsComments extends DataEntity<AppMerchantsComments> {
	
	private static final long serialVersionUID = 1L;
	private Long userId;		// 关联用户表
	private Long merchantsId;		// 管理商户表
	private String content;		// 评论内容
	private String state;		// 是否展示该评论
	private Integer serviceScore;		// 服务分
	private Integer efficiencyScore;		// 效率分
	private Date creatTime;		// creat_time
	private Date updateTime;		// update_time
	private String name;
	private String merchantsName;
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

	public AppMerchantsComments() {
		this(null);
	}

	public AppMerchantsComments(String id){
		super(id);
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public Long getMerchantsId() {
		return merchantsId;
	}

	public void setMerchantsId(Long merchantsId) {
		this.merchantsId = merchantsId;
	}
	
	@NotBlank(message="评论内容不能为空")
	@Length(min=0, max=255, message="评论内容长度不能超过 255 个字符")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@NotBlank(message="是否展示该评论不能为空")
	@Length(min=0, max=10, message="是否展示该评论长度不能超过 10 个字符")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	@NotNull(message="服务分不能为空")
	public Integer getServiceScore() {
		return serviceScore;
	}

	public void setServiceScore(Integer serviceScore) {
		this.serviceScore = serviceScore;
	}
	
	@NotNull(message="效率分不能为空")
	public Integer getEfficiencyScore() {
		return efficiencyScore;
	}

	public void setEfficiencyScore(Integer efficiencyScore) {
		this.efficiencyScore = efficiencyScore;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}


	@Override
	public String toString() {
		return "AppMerchantsComments{" +
				"userId=" + userId +
				", merchantsId=" + merchantsId +
				", content='" + content + '\'' +
				", state='" + state + '\'' +
				", serviceScore=" + serviceScore +
				", efficiencyScore=" + efficiencyScore +
				", creatTime=" + creatTime +
				", updateTime=" + updateTime +
				", name='" + name + '\'' +
				", merchantsName='" + merchantsName + '\'' +
				", url='" + url + '\'' +
				'}';
	}
}
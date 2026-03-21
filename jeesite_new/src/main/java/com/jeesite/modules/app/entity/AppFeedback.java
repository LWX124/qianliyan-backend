/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * app_feedbackEntity
 * @author zcq
 * @version 2019-09-28
 */
@Table(name="app_feedback", alias="a", columns={
		@Column(name="id", attrName="id", label="主键id", isPK=true),
		@Column(name="user_id", attrName="userId", label="用户id", isInsert=false, isQuery=false),
		@Column(name="merchants_id", attrName="merchantsId", label="关联商户表", isInsert=false, isQuery=false),
		@Column(name="phone_number", attrName="phoneNumber", label="电话号码", isInsert=false, isQuery=false),
		@Column(name="content", attrName="content", label="反馈内容", isInsert=false, isQuery=false),
		@Column(name="img_first", attrName="imgFirst", label="第一张图片", isInsert=false, isQuery=false),
		@Column(name="img_second", attrName="imgSecond", label="第二张图片", isInsert=false, isQuery=false),
		@Column(name="img_third", attrName="imgThird", label="第三张图片", isInsert=false, isQuery=false),
		@Column(name="img_fourth", attrName="imgFourth", label="第四张图片", isInsert=false, isQuery=false),
		@Column(name="create_time", attrName="createTime", label="create_time", isInsert=false),
		@Column(name="update_time", attrName="updateTime", label="update_time", isInsert=false, isQuery=false),
	},joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=AppBUser.class, attrName="this", alias="b",
				on="b.id = a.user_id",
				columns={
						@Column(name="merchants_name", attrName="merchantsName", label="名称"),
				}),
		@JoinTable(type=Type.LEFT_JOIN, entity=AppUser.class, attrName="this", alias="b",
				on="b.id = a.user_id",
				columns={
						@Column(name="name", attrName="name", label="名称"),
				})

},orderBy="a.id DESC"
)
public class AppFeedback extends DataEntity<AppFeedback> {
	
	private static final long serialVersionUID = 1L;
	private Long userId;		// 用户id
	private String merchantsId;		// 关联商户表
	private String phoneNumber;		// 电话号码
	private String content;		// 反馈内容
	private String imgFirst;		// 第一张图片
	private String imgSecond;		// 第二张图片
	private String imgThird;		// 第三张图片
	private String imgFourth;		// 第四张图片
	private Date createTime;		// create_time
	private Date updateTime;		// update_time
	private String name;
	private String merchantsName;
	
	public AppFeedback() {
		this(null);
	}

	public AppFeedback(String id){
		super(id);
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	@Length(min=0, max=50, message="关联商户表长度不能超过 50 个字符")
	public String getMerchantsId() {
		return merchantsId;
	}

	public void setMerchantsId(String merchantsId) {
		this.merchantsId = merchantsId;
	}
	
	@Length(min=0, max=11, message="电话号码长度不能超过 11 个字符")
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	@Length(min=0, max=255, message="反馈内容长度不能超过 255 个字符")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Length(min=0, max=255, message="第一张图片长度不能超过 255 个字符")
	public String getImgFirst() {
		return imgFirst;
	}

	public void setImgFirst(String imgFirst) {
		this.imgFirst = imgFirst;
	}
	
	@Length(min=0, max=255, message="第二张图片长度不能超过 255 个字符")
	public String getImgSecond() {
		return imgSecond;
	}

	public void setImgSecond(String imgSecond) {
		this.imgSecond = imgSecond;
	}
	
	@Length(min=0, max=255, message="第三张图片长度不能超过 255 个字符")
	public String getImgThird() {
		return imgThird;
	}

	public void setImgThird(String imgThird) {
		this.imgThird = imgThird;
	}
	
	@Length(min=0, max=255, message="第四张图片长度不能超过 255 个字符")
	public String getImgFourth() {
		return imgFourth;
	}

	public void setImgFourth(String imgFourth) {
		this.imgFourth = imgFourth;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
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
}
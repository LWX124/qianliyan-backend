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
 * 图片表Entity
 * @author zcq
 * @version 2019-08-06
 */
@Table(name="app_img", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="key_id", attrName="keyId", label="keyId,订单或者商户评论外键id", isInsert=false, isQuery=false),
		@Column(name="url", attrName="url", label="图片地址", isInsert=false, isQuery=false),
		@Column(name="type", attrName="type", label="图片类型", comment="图片类型(1,商户评论图,2,订单资料图)", isInsert=false),
		@Column(name="index", attrName="index", label="图片顺序", isInsert=false, isQuery=false),
		@Column(name="creat_time", attrName="creatTime", label="creat_time", isInsert=false),
		@Column(name="update_time", attrName="updateTime", label="update_time", isInsert=false, isQuery=false),
	}, orderBy="a.id DESC"
)
public class AppImg extends DataEntity<AppImg> {

	private static final long serialVersionUID = 1L;
	private Long keyId;		// keyId,订单或者商户评论外键id
	private String url;		// 图片地址
	private Long type;		// 图片类型(1,商户评论图,2,订单资料图)
	private Long index;		// 图片顺序
	private Date creatTime;		// creat_time
	private Date updateTime;		// update_time
	
	public AppImg() {
		this(null);
	}

	public AppImg(String id){
		super(id);
	}
	
	public Long getKeyId() {
		return keyId;
	}

	public void setKeyId(Long keyId) {
		this.keyId = keyId;
	}
	
	@Length(min=0, max=255, message="图片地址长度不能超过 255 个字符")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}
	
	public Long getIndex() {
		return index;
	}

	public void setIndex(Long index) {
		this.index = index;
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
	
}
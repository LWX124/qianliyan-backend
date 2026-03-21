/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;
import java.util.Date;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 拍卖车详情图片Entity
 * @author y
 * @version 2022-10-10
 */
@Table(name="app_auction_img", alias="a", columns={
		@Column(name="id", attrName="id", label="主键id", isPK=true),
		@Column(name="url", attrName="url", label="图片地址",isUpdate=true),
		@Column(name="index", attrName="index", label="排序",isUpdate=true),
		@Column(name="car_id", attrName="carId", label="关联拍卖车id",isUpdate=true),
		@Column(name="create_time", attrName="createTime", label="create_time",isUpdate=true),
		@Column(name="update_time", attrName="updateTime", label="update_time",isUpdate=true),
		@Column(name="state", attrName="state", label="图片类型",isUpdate=true),
	}, orderBy="a.id DESC"
)
public class AppAuctionImg extends DataEntity<AppAuctionImg> {
	
	private static final long serialVersionUID = 1L;
	private String url;		// 图片地址
	private Integer index;		// 排序
	private Long carId;		// 关联拍卖车id
	private Date createTime;		// create_time
	private Date updateTime;		// update_time
	private String state;		// 图片类型
	
	public AppAuctionImg() {
		this(null);
	}

	public AppAuctionImg(String id){
		super(id);
	}
	
	@Length(min=0, max=255, message="图片地址长度不能超过 255 个字符")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
	
	@NotNull(message="关联拍卖车id不能为空")
	public Long getCarId() {
		return carId;
	}

	public void setCarId(Long carId) {
		this.carId = carId;
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
	
	@Length(min=0, max=10, message="图片类型长度不能超过 10 个字符")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
}
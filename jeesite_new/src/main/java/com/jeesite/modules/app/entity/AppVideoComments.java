/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.app.entity;

import com.jeesite.common.entity.TreeEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 视频评论表Entity
 * @author zcq
 * @version 2019-08-07
 */
@Table(name="app_video_comments", alias="a", columns={
		@Column(name="tree_code", attrName="treeCode", label="节点编码", isPK=true),
		@Column(name="tree_name", attrName="treeName", label="节点名称", queryType=QueryType.LIKE, isTreeName=true),
		@Column(includeEntity=TreeEntity.class),
		@Column(name="status", attrName="status", label="节点状态", comment="节点状态(0:正常,1:删除,2:停用)"),
		@Column(name="create_date", attrName="createDate", label="create_date", isUpdate=false, isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="update_date", isQuery=false),
		@Column(name="parent_id", attrName="parentId", label="一级评论id", isQuery=false),
		@Column(name="count", attrName="count", label="评论内容"),
		@Column(name="video_id", attrName="videoId", label="关联视频表"),
		@Column(name="user_id", attrName="userId", label="关联用户表"),
		@Column(name="thumbs_up", attrName="thumbsUp", label="点赞数"),
	},
		// 联合查询出外键编码的名称数据（attrName="this"，指定this代表，当前实体）
		joinTable={
				@JoinTable(type=Type.LEFT_JOIN, entity=AppUser.class, attrName="this", alias="b",
						on="b.id = a.user_id",
						columns={
								@Column(name="name", attrName="name", label="用户名字"),
						}),
		}, orderBy="a.tree_sorts, a.tree_code"
)
public class AppVideoComments extends TreeEntity<AppVideoComments> {
	
	private static final long serialVersionUID = 1L;
	private String treeCode;		// 节点编码
	private String treeName;		// 节点名称
	private Integer parentId;		//一级评论id
	private String count;		// 评论内容
	private String videoId;		// 关联视频表
	private String userId;		// 关联用户表
	private String thumbsUp;		// 点赞数
	private String name;			//用户名字

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AppVideoComments() {
		this(null);
	}

	public AppVideoComments(String id){
		super(id);
	}
	
	@Override
	public AppVideoComments getParent() {
		return parent;
	}

	@Override
	public void setParent(AppVideoComments parent) {
		this.parent = parent;
	}
	
	public String getTreeCode() {
		return treeCode;
	}

	public void setTreeCode(String treeCode) {
		this.treeCode = treeCode;
	}
	
	@NotBlank(message="节点名称不能为空")
	@Length(min=0, max=200, message="节点名称长度不能超过 200 个字符")
	public String getTreeName() {
		return treeName;
	}

	public void setTreeName(String treeName) {
		this.treeName = treeName;
	}
	
	@NotBlank(message="评论内容不能为空")
	@Length(min=0, max=255, message="评论内容长度不能超过 255 个字符")
	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}
	
	@Length(min=0, max=10, message="关联视频表长度不能超过 10 个字符")
	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	
	@Length(min=0, max=10, message="关联用户表长度不能超过 10 个字符")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Length(min=0, max=20, message="点赞数长度不能超过 20 个字符")
	public String getThumbsUp() {
		return thumbsUp;
	}

	public void setThumbsUp(String thumbsUp) {
		this.thumbsUp = thumbsUp;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	@Override
	public String toString() {
		return "AppVideoComments{" +
				"treeCode='" + treeCode + '\'' +
				", treeName='" + treeName + '\'' +
				", parentId=" + parentId +
				", count='" + count + '\'' +
				", videoId='" + videoId + '\'' +
				", userId='" + userId + '\'' +
				", thumbsUp='" + thumbsUp + '\'' +
				", name='" + name + '\'' +
				'}';
	}
}
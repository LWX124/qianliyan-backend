package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 通知表
 * </p>
 *
 * @author kosans
 * @since 2017-07-11
 */
@TableName("sys_notice")
public class Notice extends Model<Notice> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 标题
     */
	private String title;
    /**
     * 类型
     */
//	private Integer type;
	/**
	 * 首页图链接
	 */
	private String homePageImgUrl;
	/**
	 * 新闻类型
	 */
	private Integer newsType;
    /**
     * 内容
     */
	private String content;
	/**
	 * 创建人
	 */
	private Integer pageview;
    /**
     * 创建时间
     */
	private Date createtime;
    /**
     * 创建人
     */
	private Integer creater;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

//	public Integer getType() {
//		return type;
//	}
//
//	public void setType(Integer type) {
//		this.type = type;
//	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Integer getCreater() {
		return creater;
	}

	public void setCreater(Integer creater) {
		this.creater = creater;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	public String getHomePageImgUrl() {
		return homePageImgUrl;
	}

	public void setHomePageImgUrl(String homePageImgUrl) {
		this.homePageImgUrl = homePageImgUrl;
	}

	public Integer getNewsType() {
		return newsType;
	}

	public void setNewsType(Integer newsType) {
		this.newsType = newsType;
	}

	public Integer getPageview() {
		return pageview;
	}

	public void setPageview(Integer pageview) {
		this.pageview = pageview;
	}

	@Override
	public String toString() {
		return "Notice{" +
				"id=" + id +
				", title='" + title + '\'' +
				", homePageImgUrl='" + homePageImgUrl + '\'' +
				", newsType=" + newsType +
				", content='" + content + '\'' +
				", pageview=" + pageview +
				", createtime=" + createtime +
				", creater=" + creater +
				'}';
	}
}

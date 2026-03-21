package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门表
 * </p>
 *
 * @author kosans
 * @since 2017-07-11
 */
@ApiModel(value="Dept", description="开放平台4S店实体bean")
@TableName("sys_dept")
public class Dept extends Model<Dept> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 排序
     */
	private Integer num;
    /**
     * 父部门id
     */
	private Integer pid;
    /**
     * 父级ids
     */
	private String pids;
    /**
     * 简称
     */
	@ApiModelProperty(value="简称",name="simplename")
	private String simplename;
    /**
     * 全称
     */
	@ApiModelProperty(value="全称,注册时请对应名称",name="fullname")
	private String fullname;
	/**
	 * 经度
	 */
	@ApiModelProperty(value="经度",name="lng")
	private BigDecimal lng;
	/**
	 * 纬度
	 */
	@ApiModelProperty(value="纬度",name="lat")
	private BigDecimal lat;
    /**
     * 提示
     */
	@ApiModelProperty(value="品牌",name="tips")
	private String tips;
	/**
	 * 详细地址
	 */
	@ApiModelProperty(value="详细地址",name="address")
	private String address;

	/**
	 * 图片介绍urls
	 */
	@ApiModelProperty(value="图片介绍urls，第一张为首页图，后面为详情图片，用|分隔",name="imgUrls")
	private String imgUrls;
	/**
	 * 简介
	 */
	private String synopsis;
	/**
	 * 合作保险公司id集合
	 */
	@ApiModelProperty(value="合作保险公司id集合，数字以,分隔",name="companyids")
	private String companyids;
	/**
	 * 合作保险公司id集合
	 */
	@TableField(exist = false)
	private List<Map> companyUrls2;



	/**
	 * 合作保险公司id集合
	 */
	@TableField(exist = false)
	private List<String> companyUrls;
	/**
	 * 车己对业务员返点比例
	 */
	@TableField("scale_for_emp")
	private Double scaleForEmp;
	/**
	 * 4s店业务返点比例
	 */
	@TableField("scale_for_company")
	private Double scaleForCompany;
    /**
     * 版本（乐观锁保留字段）
     */
	private Integer version;

	@TableField(exist = false)
	private String[] detailImg;

	@TableField(exist = false)
	private String indexImg;




	@ApiModelProperty(value="销售人员，根据姓名|职位|电话添加，以,分隔每个人的个人信息",name="salesperson")
	private String salesperson;


	public String[] getDetailImg() {
		return detailImg;
	}

	public void setDetailImg(String[] detailImg) {
		this.detailImg = detailImg;
	}

	public String getIndexImg() {
		return indexImg;
	}

	public void setIndexImg(String indexImg) {
		this.indexImg = indexImg;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getPids() {
		return pids;
	}

	public void setPids(String pids) {
		this.pids = pids;
	}

	public String getSimplename() {
		return simplename;
	}

	public void setSimplename(String simplename) {
		this.simplename = simplename;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getTips() {
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	public BigDecimal getLng() {
		return lng;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setLng(BigDecimal lng) {
		this.lng = lng;
	}

	public String getImgUrls() {
		return imgUrls;
	}

	public void setImgUrls(String imgUrls) {
		this.imgUrls = imgUrls;
	}

	public String getCompanyids() {
		return companyids;
	}

	public void setCompanyids(String companyids) {
		this.companyids = companyids;
	}

	public List<String> getCompanyUrls() {
		return companyUrls;
	}



	public Double getScaleForEmp() {
		return scaleForEmp;
	}

	public void setScaleForEmp(Double scaleForEmp) {
		this.scaleForEmp = scaleForEmp;
	}

	public Double getScaleForCompany() {
		return scaleForCompany;
	}

	public void setScaleForCompany(Double scaleForCompany) {
		this.scaleForCompany = scaleForCompany;
	}

	@Override
	public String toString() {
		return "Dept{" +
				"id=" + id +
				", num=" + num +
				", pid=" + pid +
				", pids='" + pids + '\'' +
				", simplename='" + simplename + '\'' +
				", fullname='" + fullname + '\'' +
				", lng=" + lng +
				", lat=" + lat +
				", tips='" + tips + '\'' +
				", address='" + address + '\'' +
				", imgUrls='" + imgUrls + '\'' +
				", synopsis='" + synopsis + '\'' +
				", companyids='" + companyids + '\'' +
				", companyUrls=" + companyUrls +
				", companyUrls2=" + companyUrls2 +
				", scaleForEmp=" + scaleForEmp +
				", scaleForCompany=" + scaleForCompany +
				", version=" + version +
				", detailImg=" + Arrays.toString(detailImg) +
				", indexImg='" + indexImg + '\'' +
				", salesperson='" + salesperson + '\'' +
				'}';
	}

	public BigDecimal getLat() {
		return lat;
	}

	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}

	public String getSalesperson() {
		return salesperson;
	}

	public void setSalesperson(String salesperson) {
		this.salesperson = salesperson;
	}




	public List<Map> getCompanyUrls2() {
		return companyUrls2;
	}

	public void setCompanyUrls2(List<Map> companyUrls2) {
		this.companyUrls2 = companyUrls2;
	}

	public void setCompanyUrls(List<String> companyUrls) {
		this.companyUrls = companyUrls;
	}
}

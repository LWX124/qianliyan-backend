package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 管理员表
 * </p>
 *
 * @author kosans
 * @since 2017-07-11
 */
@ApiModel(value="User", description="开放平台用户实体bean")
@TableName("sys_user")
public class User extends Model<User> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	/**
	 * 头像
	 */
	private String avatar;
	/**
	 * 账号
	 */
	@ApiModelProperty(value = "账号，填写手机的时候会默认注册", name = "account")
	private String account;
	/**
	 * 密码
	 */
	@ApiModelProperty(value = "密码", name = "password")
	private String password;
	/**
	 * md5密码盐
	 */
	private String salt;
	/**
	 * 名字
	 */
	@ApiModelProperty(value = "名字", name = "name")
	private String name;
	/**
	 * 生日
	 */
	private Date birthday;
	/**
	 * 性别（1：男 2：女）
	 */
	private Integer sex;
	/**
	 * 电子邮件
	 */
	private String email;
	/**
	 * 电话
	 */
	@ApiModelProperty(value = "电话", name = "phone")
	private String phone;
	/**
	 * 角色id
	 */
	@ApiModelProperty(value = "角色id，理赔员入7，4S店员入6", name = "roleid")
	private String roleid;
	/**
	 * 部门id
	 */
	@ApiModelProperty(value = "部门id,理赔员默认传30,,4S店员可传对应的店id,可不传", name = "deptid")
	private Integer deptid;
	/**
	 * 状态(1：启用  2：冻结  3：删除,4:待审核,5：审核成功）
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	private Date createtime;
	/**
	 * 保留字段
	 */
	private Integer version;
	/**
	 * 精度
	 */
	private BigDecimal lng;
	/**
	 * 纬度
	 */
	private BigDecimal lat;
	/**
	 * 省
	 */
	private String province;
	/**
	 * 市
	 */
	private String city;
	/**
	 * 区
	 */
	private String area;
	/**
	 * 银行卡号
	 */
	@ApiModelProperty(value = "银行卡号", name = "bankcard")
	private String bankcard;
	/**
	 * 银行卡户名
	 */
	@ApiModelProperty(value = "银行卡户名", name = "bankUserName")
	private String bankUserName;
	/**
	 * 开户银行
	 */
	private String bankName;
	/**
	 * 开户支行名称
	 */
	private String bankSecondName;
	/**
	 * 身份证
	 */
	private String idcard;
	/**
	 * 是否扣费(默认扣费，N：不扣费)
	 */
	private String sfkf;
	/**
	 * 是否黑明单(默认，N)
	 */
	private String black_flag;

	/**
	 * APP头像
	 */
	@ApiModelProperty(value = "APP头像", name = "headImg")
	private String headImg;


	/**
	 * 星级
	 */
	private Double stars;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	public Integer getDeptid() {
		return deptid;
	}

	public void setDeptid(Integer deptid) {
		this.deptid = deptid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public BigDecimal getLng() {
		return lng;
	}

	public void setLng(BigDecimal lng) {
		this.lng = lng;
	}

	public BigDecimal getLat() {
		return lat;
	}

	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getBankcard() {
		return bankcard;
	}

	public void setBankcard(String bankcard) {
		this.bankcard = bankcard;
	}

	public String getBankUserName() {
		return bankUserName;
	}

	public void setBankUserName(String bankUserName) {
		this.bankUserName = bankUserName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankSecondName() {
		return bankSecondName;
	}

	public void setBankSecondName(String bankSecondName) {
		this.bankSecondName = bankSecondName;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getSfkf() {
		return sfkf;
	}

	public void setSfkf(String sfkf) {
		this.sfkf = sfkf;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", avatar='" + avatar + '\'' +
				", account='" + account + '\'' +
				", password='" + password + '\'' +
				", salt='" + salt + '\'' +
				", name='" + name + '\'' +
				", birthday=" + birthday +
				", sex=" + sex +
				", email='" + email + '\'' +
				", phone='" + phone + '\'' +
				", roleid='" + roleid + '\'' +
				", deptid=" + deptid +
				", status=" + status +
				", createtime=" + createtime +
				", version=" + version +
				", lng=" + lng +
				", lat=" + lat +
				", province='" + province + '\'' +
				", city='" + city + '\'' +
				", area='" + area + '\'' +
				", bankcard='" + bankcard + '\'' +
				", bankUserName='" + bankUserName + '\'' +
				", bankName='" + bankName + '\'' +
				", bankSecondName='" + bankSecondName + '\'' +
				", idcard='" + idcard + '\'' +
				", headImg='" + headImg + '\'' +
				", stars='" + stars + '\'' +
				", sfkf='" + sfkf + '\'' +
				", black_flag='" + black_flag + '\'' +
				'}';
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}


	public String getBlack_flag() {
		return black_flag;
	}

	public void setBlack_flag(String black_flag) {
		this.black_flag = black_flag;
	}

	public void setStars(Double stars) {
		this.stars = stars;
	}

	public Double getStars() {
		return stars;
	}

}



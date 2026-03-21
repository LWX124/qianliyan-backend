package com.stylefeng.guns.modular.system.transfer;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户传输bean
 * 
 * @author kosans
 * @Date 2017/5/5 22:40
 */
public class UserDto{

	private Integer id;
	private String account;
	private String password;
	private String salt;
	private String name;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthday;
	private Integer sex;
	private String email;
	private String phone;
	private String roleid;
	private Integer deptid;
	private Integer status;
	private Date createtime;
	private Integer version;
	private String avatar;

	private String sfkf;

	private String black_flag;

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

	private BigDecimal lng;
	private BigDecimal lat;


	public String getBlack_flag() {
		return black_flag;
	}

	public void setBlack_flag(String black_flag) {
		this.black_flag = black_flag;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
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

	public String getSfkf() {
		return sfkf;
	}

	@Override
	public String toString() {
		return "UserDto{" +
				"id=" + id +
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
				", avatar='" + avatar + '\'' +
				", sfkf='" + sfkf + '\'' +
				", black_flag='" + black_flag + '\'' +
				", province='" + province + '\'' +
				", city='" + city + '\'' +
				", area='" + area + '\'' +
				", lng=" + lng +
				", lat=" + lat +
				'}';
	}

	public void setSfkf(String sfkf) {


		this.sfkf = sfkf;
	}
}

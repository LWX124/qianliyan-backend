//package com.stylefeng.guns.modular.system.model;
//
//import com.baomidou.mybatisplus.activerecord.Model;
//import com.baomidou.mybatisplus.annotations.TableId;
//import com.baomidou.mybatisplus.annotations.TableName;
//import com.baomidou.mybatisplus.enums.IdType;
//
//import java.io.Serializable;
//import java.math.BigDecimal;
//import java.util.Date;
//
///**
// * <p>
// * 部门表
// * </p>
// *
// * @author kosans
// * @since 2017-07-11
// */
//@TableName("biz_wx_user")
//public class WxUser extends Model<WxUser> {
//
//    private static final long serialVersionUID = 1L;
//
//    /**
//     * 主键id
//     */
//	@TableId(value="id", type= IdType.AUTO)
//	private Long id;
//	/**
//	 * 微信用户id
//	 */
//	private String openid;
//	/**
//	 * 用户来源0：公众号，1：小程序
//	 */
//	private Integer type;
//	/**
//	 * 生成时间
//	 */
//	private Date createTime;
//	/**
//	 * 支付宝账户
//	 */
//	private String alipay_account;
//	/**
//	 * 审核时间
//	 */
//	private Date checkTime;
//	/**
//	 * 审核状态  1：未审核  2：审核通过  3：审核失败
//	 */
//	private Integer status;
//	/**
//	 * 上报地址名称
//	 */
//	private String address;
//
//    /**
//     * 版本（乐观锁保留字段）
//     */
//	private Integer version;
//
//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//	public String getOpenid() {
//		return openid;
//	}
//
//	public void setOpenid(String openid) {
//		this.openid = openid;
//	}
//
//	public String getVideo() {
//		return video;
//	}
//
//	public void setVideo(String video) {
//		this.video = video;
//	}
//
//	public BigDecimal getLng() {
//		return lng;
//	}
//
//	public void setLng(BigDecimal lng) {
//		this.lng = lng;
//	}
//
//	public BigDecimal getLat() {
//		return lat;
//	}
//
//	public void setLat(BigDecimal lat) {
//		this.lat = lat;
//	}
//
//
//	public String getCheckId() {
//		return checkId;
//	}
//
//	public void setCheckId(String checkId) {
//		this.checkId = checkId;
//	}
//
//	public Date getCreateTime() {
//		return createTime;
//	}
//
//	public void setCreateTime(Date createTime) {
//		this.createTime = createTime;
//	}
//
//	public Date getCheckTime() {
//		return checkTime;
//	}
//
//	public void setCheckTime(Date checkTime) {
//		this.checkTime = checkTime;
//	}
//
//	public Integer getStatus() {
//		return status;
//	}
//
//	public void setStatus(Integer status) {
//		this.status = status;
//	}
//
//	public Integer getVersion() {
//		return version;
//	}
//
//	public void setVersion(Integer version) {
//		this.version = version;
//	}
//
//	@Override
//	protected Serializable pkVal() {
//		return this.id;
//	}
//
//	public String getAddress() {
//		return address;
//	}
//
//	public void setAddress(String address) {
//		this.address = address;
//	}
//
//	@Override
//	public String toString() {
//		return "Accident{" +
//				"id=" + id +
//				", openid='" + openid + '\'' +
//				", video='" + video + '\'' +
//				", lng=" + lng +
//				", lat=" + lat +
//				", createTime=" + createTime +
//				", checkId='" + checkId + '\'' +
//				", checkTime=" + checkTime +
//				", status=" + status +
//				", address='" + address + '\'' +
//				", version=" + version +
//				'}';
//	}
//}

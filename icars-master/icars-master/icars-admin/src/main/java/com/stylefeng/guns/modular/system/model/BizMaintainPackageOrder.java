package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 汽车保养订单表
 * </p>
 *
 * @author stylefeng
 * @since 2018-11-21
 */
@TableName("biz_maintain_package_order")
public class BizMaintainPackageOrder extends Model<BizMaintainPackageOrder> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 订单号
     */

    private String orderno;
    /**
     * 微信用户id
     */
    private String openid;
    /**
     * 套餐id
     */
    private Integer packageid;
    /**
     * 用户电话
     */
    private String phone;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 原价
     */
    private BigDecimal prePrice;
    /**
     * 订单状态
     */
    @TableField("order_status")
    private String orderStatus;
    /**
     * 经度
     */
    private BigDecimal lng;
    /**
     * 纬度
     */
    private BigDecimal lat;
    /**
     * 地址
     */
    private String address;
    /**
     * 修改时间
     */
    @TableField("modify_time")
    private Date modifyTime;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 创建开始时间（用于查询）
     */
    @TableField(exist = false)
    private String createStartTime;

    /**
     * 创建结束时间（用于查询）
     */
    @TableField(exist = false)
    private String createEndTime;

    /**
     * 套餐名称
     */
    @TableField(exist = false)
    private String packageName;

    /**
     * 套餐商品明细名称
     */
    @TableField(exist = false)
    private String detail;

    /**
     * 套餐商品图片
     */
    @TableField(exist = false)
    private String img;

    /**
     * 推送维修人员姓名
     */
    @TableField(exist = false)
    private String repaireName;

    /**
     * 推送维修人员账号
     */
    @TableField(exist = false)
    private String account;

    /**
     * 推送维修人员电话
     */
    @TableField(exist = false)
    private String repairePhone;

    /**
     * 定位url
     */
    @TableField(exist = false)
    private String mapUrl;

    /**
     * 客户名称
     */
    private String name;

    /**
     * 到店前图片
     */
    private String preImgs;

    /**
     * 到店后图片
     */
    private String aftImgs;

    /**
     * 到店前图片集合
     */
    @TableField(exist = false)
    private List<String> preImgsList;

    /**
     * 到店后图片集合
     */
    @TableField(exist = false)
    private List<String> aftImgsList;

    /**
     * 套餐商品明细名称列表
     */
    @TableField(exist = false)
    private List<String> detailList;

    /**
     * 套餐类型（用于查询）
     */
    @TableField(exist = false)
    private Integer packageType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateStartTime() {
        return createStartTime;
    }

    public void setCreateStartTime(String createStartTime) {
        this.createStartTime = createStartTime;
    }

    public String getCreateEndTime() {
        return createEndTime;
    }

    public void setCreateEndTime(String createEndTime) {
        this.createEndTime = createEndTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Integer getPackageid() {
        return packageid;
    }

    public void setPackageid(Integer packageid) {
        this.packageid = packageid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public List<String> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<String> detailList) {
        this.detailList = detailList;
    }

    public String getPreImgs() {
        return preImgs;
    }

    public void setPreImgs(String preImgs) {
        this.preImgs = preImgs;
    }

    public String getAftImgs() {
        return aftImgs;
    }

    public void setAftImgs(String aftImgs) {
        this.aftImgs = aftImgs;
    }

    public List<String> getPreImgsList() {
        return preImgsList;
    }

    public void setPreImgsList(List<String> preImgsList) {
        this.preImgsList = preImgsList;
    }

    public List<String> getAftImgsList() {
        return aftImgsList;
    }

    public void setAftImgsList(List<String> aftImgsList) {
        this.aftImgsList = aftImgsList;
    }

    public String getRepaireName() {
        return repaireName;
    }

    public void setRepaireName(String repaireName) {
        this.repaireName = repaireName;
    }

    public String getRepairePhone() {
        return repairePhone;
    }

    public void setRepairePhone(String repairePhone) {
        this.repairePhone = repairePhone;
    }

    public String getMapUrl() {
        return mapUrl;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public BigDecimal getPrePrice() {
        return prePrice;
    }

    public void setPrePrice(BigDecimal prePrice) {
        this.prePrice = prePrice;
    }

    public Integer getPackageType() {
        return packageType;
    }

    public void setPackageType(Integer packageType) {
        this.packageType = packageType;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "BizRepairePackageOrder{" +
        "id=" + id +
        ", orderno=" + orderno +
        ", openid=" + openid +
        ", packageid=" + packageid +
        ", phone=" + phone +
        ", price=" + price +
        ", orderStatus=" + orderStatus +
        ", lng=" + lng +
        ", lat=" + lat +
        ", address=" + address +
        ", modifyTime=" + modifyTime +
        ", createTime=" + createTime +
        "}";
    }
}

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
 * 汽车保养套餐商品表
 * </p>
 *
 * @author kosan
 * @since 2018-11-21
 */
@TableName("biz_maintain_package")
public class BizMaintainPackage extends Model<BizMaintainPackage> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 套餐名称
     */
    @TableField("package_name")
    private String packageName;
    /**
     * 套餐明细
     */
    private String detail;
    /**
     * 套餐首页图
     */
    private String img;
    /**
     * 实际价格
     */
    private BigDecimal price;
    /**
     * 原价价格
     */
    private BigDecimal prePrice;
    /**
     * 状态 0：上架，1：下架
     */
    private Integer pstatus;
    @TableField("create_time")
    private Date createTime;
    /**
     * 套餐类型（0：保养、1：维修）
     */
    @TableField("package_type")
    private Integer packageType;


    /**
     * 套餐明细
     */
    @TableField(exist = false)
    private List<String> detailList;

    /**
     * 状态名称
     */
    @TableField(exist = false)
    private String pstatusName;

    public String getPstatusName() {
        return pstatusName;
    }

    public void setPstatusName(String pstatusName) {
        this.pstatusName = pstatusName;
    }

    public List<String> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<String> detailList) {
        this.detailList = detailList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getPstatus() {
        return pstatus;
    }

    public void setPstatus(Integer pstatus) {
        this.pstatus = pstatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
        return "BizRepairePackage{" +
        "id=" + id +
        ", packageName=" + packageName +
        ", detail=" + detail +
        ", price=" + price +
        ", pstatus=" + pstatus +
        ", createTime=" + createTime +
        "}";
    }
}

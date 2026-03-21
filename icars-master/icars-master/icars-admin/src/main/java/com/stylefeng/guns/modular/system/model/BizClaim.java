package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 理赔单
 * </p>
 *
 * @author stylefeng
 * @since 2018-08-27
 */
@TableName("biz_claim")
public class BizClaim extends Model<BizClaim> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("maintenance_id")
    private Integer maintenanceId;
    /**
     * 客户手机号
     */
    @NotBlank
    private String phone;
    /**
     * 客户openid
     */
    @NotBlank
    private String openid;
    /**
     * 客户姓名
     */
    @NotBlank
    private String name;
    /**
     * 车牌号
     */
    private String cph;
    /**
     * 理赔单类型
     */
    @NotNull
    private Integer type;
    /**
     * 理赔状态,0:已下单、1：理赔中、2：理赔完成
     */
    private Integer status;
    /**
     * 描述
     */
    private String desc;
    /**
     * 理赔负责人
     */
    private String claimer;
    /**
     * 经度
     */
    @NotNull
    private BigDecimal lng;
    /**
     * 纬度
     */
    @NotNull
    private BigDecimal lat;
    /**
     * 定位地址名称
     */
    @NotBlank
    private String address;
    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 理赔图片
     */
    private String claimImg;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCph() {
        return cph;
    }

    public void setCph(String cph) {
        this.cph = cph;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getClaimer() {
        return claimer;
    }

    public void setClaimer(String claimer) {
        this.claimer = claimer;
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

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public String getClaimImg() {
        return claimImg;
    }

    public void setClaimImg(String claimImg) {
        this.claimImg = claimImg;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Integer getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(Integer maintenanceId) {
        this.maintenanceId = maintenanceId;
    }
}

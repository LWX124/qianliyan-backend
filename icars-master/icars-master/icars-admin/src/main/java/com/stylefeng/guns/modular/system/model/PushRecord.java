package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.IdType;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 事故推送记录
 * </p>
 *
 * @author kosans
 * @since 2018-07-26
 */
@TableName("biz_push_record")
public class PushRecord extends Model<PushRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 员工账号
     */
    private String account;
    /**
     * 事故主键id
     */
    private Long accid;
    /**
     * 部门id
     */
    private Integer deptid;
    /**
     * 部门全称
     */
    @TableField(exist = false)
    private String fullname;
    /**
     * 推送人员名字
     */
    @TableField(exist = false)
    private String name;
    /**
     * 状态，0：未查看，1：已查看
     */
    private Integer status;
    /**
     * 推送时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date modifyTime;
    /**
     * 保留字段
     */
    private Integer version;

    /**
     * 视频url
     */
    @TableField(exist = false)
    private String video;
    /**
     * 事故地址
     */
    @TableField(exist = false)
    private String address;
    /**
     * 经度
     */
    @TableField(exist = false)
    private BigDecimal lng;
    /**
     * 纬度
     */
    @TableField(exist = false)
    private BigDecimal lat;
    /**
     * 上报人手机号
     */
    @TableField(exist = false)
    private String phone;


    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    public Long getAccid() {
        return accid;
    }

    public void setAccid(Long accid) {
        this.accid = accid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Integer getDeptid() {
        return deptid;
    }

    public void setDeptid(Integer deptid) {
        this.deptid = deptid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "PushRecord{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", accid=" + accid +
                ", deptid=" + deptid +
                ", fullname='" + fullname + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                ", version=" + version +
                ", video='" + video + '\'' +
                ", address='" + address + '\'' +
                ", lng=" + lng +
                ", lat=" + lat +
                ", phone='" + phone + '\'' +
                '}';
    }
}

package com.cheji.b.modular.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 理赔老师表
 * </p>
 *
 * @author Ashes
 * @since 2022-03-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("app_claim_teacher")
public class AppClaimTeacherEntity extends Model<AppClaimTeacherEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联用户表
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 级别
     */
    private String level;

    /**
     * 状态 1,正常
     */
    private Integer state;

    /**
     * 1.理赔老师，2.4s店，3.修理厂
     */
    private Integer type;

    /**
     * 在线， 1在，2不在
     */
    @TableField("on_lion")
    private Integer onLion;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 当前位置
     */
    @TableField("current_position")
    private String currentPosition;

    /**
     * 地址
     */
    private String address;

    /**
     * 是否添加
     */
    @TableField("is_add")
    private Integer isAdd;

    /**
     * 品牌
     */
    private String brand;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getOnLion() {
        return onLion;
    }

    public void setOnLion(Integer onLion) {
        this.onLion = onLion;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(String currentPosition) {
        this.currentPosition = currentPosition;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getIsAdd() {
        return isAdd;
    }

    public void setIsAdd(Integer isAdd) {
        this.isAdd = isAdd;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}

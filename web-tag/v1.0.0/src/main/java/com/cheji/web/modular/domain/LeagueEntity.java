package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 加盟信息表
 * </p>
 *
 * @author Ashes
 * @since 2019-08-21
 */
@TableName("app_league")
public class LeagueEntity extends Model<LeagueEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 店铺名字
     */
    @TableField("shop_name")
    private String shopName;
    /**
     * 品牌
     */
    private String brand;
    /**
     * 店铺详细地址
     */
    private String address;
    /**
     * 加盟人姓名
     */
    private String name;
    /**
     * 加盟人电话
     */
    @TableField("phone_number")
    private String phoneNumber;
    /**
     * 城市名字
     */
    @TableField("city_name")
    private String cityName;
    @TableField("creat_time")
    private Date creatTime;
    @TableField("update_time")
    private Date updateTime;

    public LeagueEntity(String name,Date creatTime){
        this.name =name;
        this.creatTime = creatTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "LeagueEntity{" +
        ", id=" + id +
        ", shopName=" + shopName +
        ", brand=" + brand +
        ", address=" + address +
        ", name=" + name +
        ", phoneNumber=" + phoneNumber +
        ", cityName=" + cityName +
        ", creatTime=" + creatTime +
        ", updateTime=" + updateTime +
        "}";
    }
}

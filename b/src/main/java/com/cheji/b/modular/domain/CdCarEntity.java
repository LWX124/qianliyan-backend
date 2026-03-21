package com.cheji.b.modular.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.annotation.Resource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author Ashes
 * @since 2022-01-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("cd_car")
public class CdCarEntity extends Model<CdCarEntity> {

    private static final long serialVersionUID = 1L;

    private String name;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("parent_id")
    private Integer parentId;

    private Integer hierarchy;


    @TableField(exist = false)
    private List<CdCarEntity> carEntities;

    @TableField("commodity_price")
    private BigDecimal commodityPrice;

    @TableField("lit_fix_price")
    private BigDecimal litFixPrice;

    @TableField("min_fix_price")
    private BigDecimal minFixPrice;

    @TableField("big_fix_price")
    private BigDecimal bigFixPrice;

    @TableField("mounting_price_one")
    private BigDecimal mountingPriceOne;

    @TableField("mounting_price_all")
    private BigDecimal mountingPriceAll;

    @TableField("half_spray_price")
    private BigDecimal halfSprayPrice;

    @TableField("all_spray_price")
    private BigDecimal allSprayPrice;

    public BigDecimal getCommodityPrice() {
        return commodityPrice;
    }

    public void setCommodityPrice(BigDecimal commodityPrice) {
        this.commodityPrice = commodityPrice;
    }

    public BigDecimal getLitFixPrice() {
        return litFixPrice;
    }

    public void setLitFixPrice(BigDecimal litFixPrice) {
        this.litFixPrice = litFixPrice;
    }

    public BigDecimal getMinFixPrice() {
        return minFixPrice;
    }

    public void setMinFixPrice(BigDecimal minFixPrice) {
        this.minFixPrice = minFixPrice;
    }

    public BigDecimal getBigFixPrice() {
        return bigFixPrice;
    }

    public void setBigFixPrice(BigDecimal bigFixPrice) {
        this.bigFixPrice = bigFixPrice;
    }

    public BigDecimal getMountingPriceOne() {
        return mountingPriceOne;
    }

    public void setMountingPriceOne(BigDecimal mountingPriceOne) {
        this.mountingPriceOne = mountingPriceOne;
    }

    public BigDecimal getMountingPriceAll() {
        return mountingPriceAll;
    }

    public void setMountingPriceAll(BigDecimal mountingPriceAll) {
        this.mountingPriceAll = mountingPriceAll;
    }

    public BigDecimal getHalfSprayPrice() {
        return halfSprayPrice;
    }

    public void setHalfSprayPrice(BigDecimal halfSprayPrice) {
        this.halfSprayPrice = halfSprayPrice;
    }

    public BigDecimal getAllSprayPrice() {
        return allSprayPrice;
    }

    public void setAllSprayPrice(BigDecimal allSprayPrice) {
        this.allSprayPrice = allSprayPrice;
    }

    public List<CdCarEntity> getCarEntities() {
        return carEntities;
    }

    public void setCarEntities(List<CdCarEntity> carEntities) {
        this.carEntities = carEntities;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(Integer hierarchy) {
        this.hierarchy = hierarchy;
    }
}

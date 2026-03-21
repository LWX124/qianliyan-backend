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
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 配件明细
 * </p>
 *
 * @author Ashes
 * @since 2022-01-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("cd_parts_details")
public class CdPartsDetailsEntity extends Model<CdPartsDetailsEntity> {

    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 订单id
     */
    @TableField("indent_id")
    private Integer indentId;

    /**
     * 类型，1.换配件，2.拆装，3.修复，4.喷漆
     */
    private Integer type;


    private Integer state;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 配件id
     */
    @TableField("parts_id")
    private Integer partsId;

    @TableField("create_time")
    private Date createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIndentId() {
        return indentId;
    }

    public void setIndentId(Integer indentId) {
        this.indentId = indentId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getPartsId() {
        return partsId;
    }

    public void setPartsId(Integer partsId) {
        this.partsId = partsId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}

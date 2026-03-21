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
 * 工单表
 * </p>
 *
 * @author Ashes
 * @since 2022-01-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("cd_repair_order")
public class CdRepairOrderEntity extends Model<CdRepairOrderEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 订单id
     */
    @TableField("indent_id")
    private Integer indentId;

    private Integer type;

    /**
     * 钣金人员
     */
    @TableField("sheet_metal")
    private String sheetMetal;

    /**
     * 钣金工时
     */
    @TableField("work_price")
    private String workPrice;

    /**
     * 维修时间
     */
    @TableField("work_time")
    private String workTime;

    /**
     * 领料
     */
    private Integer picking;

    /**
     * 补货
     */
    private Integer replenishment;

    /**
     * 退货
     */
    private Integer sales;

    /**
     * 转工
     */
    @TableField("change_job")
    private String changeJob;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public String getSheetMetal() {
        return sheetMetal;
    }

    public void setSheetMetal(String sheetMetal) {
        this.sheetMetal = sheetMetal;
    }

    public String getWorkPrice() {
        return workPrice;
    }

    public void setWorkPrice(String workPrice) {
        this.workPrice = workPrice;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public Integer getPicking() {
        return picking;
    }

    public void setPicking(Integer picking) {
        this.picking = picking;
    }

    public Integer getReplenishment() {
        return replenishment;
    }

    public void setReplenishment(Integer replenishment) {
        this.replenishment = replenishment;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getChangeJob() {
        return changeJob;
    }

    public void setChangeJob(String changeJob) {
        this.changeJob = changeJob;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}

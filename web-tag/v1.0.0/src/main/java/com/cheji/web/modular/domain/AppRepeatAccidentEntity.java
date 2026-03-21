package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author Ashes
 * @since 2019-11-27
 */
@TableName("app_repeat_accident")
public class AppRepeatAccidentEntity extends Model<AppRepeatAccidentEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 上传事故id
     */
    @TableField("accident_id")
    private String accidentId;
    /**
     * 事故来源(1,APP，2,小程序)
     */
    @TableField("accident_source")
    private Integer accidentSource;
    /**
     * 重复事故id
     */
    @TableField("repeat_id")
    private String repeatId;
    /**
     * 重复事故来源(1,App，2.小程序)
     */
    @TableField("repeat_srouce")
    private Integer repeatSrouce;
    /**
     * 状态(1.是重复数据，2.不是重复数据)
     */
    private String state;

    @TableField("create_time")
    private Date createTime;


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccidentId() {
        return accidentId;
    }

    public void setAccidentId(String accidentId) {
        this.accidentId = accidentId;
    }

    public Integer getAccidentSource() {
        return accidentSource;
    }

    public void setAccidentSource(Integer accidentSource) {
        this.accidentSource = accidentSource;
    }

    public String getRepeatId() {
        return repeatId;
    }

    public void setRepeatId(String repeatId) {
        this.repeatId = repeatId;
    }

    public Integer getRepeatSrouce() {
        return repeatSrouce;
    }

    public void setRepeatSrouce(Integer repeatSrouce) {
        this.repeatSrouce = repeatSrouce;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "AppRepeatAccidentEntity{" +
                "id=" + id +
                ", accidentId='" + accidentId + '\'' +
                ", accidentSource=" + accidentSource +
                ", repeatId='" + repeatId + '\'' +
                ", repeatSrouce=" + repeatSrouce +
                ", state='" + state + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}

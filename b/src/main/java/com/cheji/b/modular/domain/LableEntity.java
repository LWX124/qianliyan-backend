package com.cheji.b.modular.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 标签表
 * </p>
 *
 * @author Ashes
 * @since 2019-09-29
 */
@TableName("app_lable")
public class LableEntity extends Model<LableEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 标签
     */
    private String label;
    /**
     * 标签类型(1.商户列表标签2.商户页面标签)
     */
    private Integer type;

    //状态
    @TableField(exist = false)
    private String state;

    private String url;

    private Integer enable;

    private Integer index;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "LableEntity{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", type=" + type +
                ", state='" + state + '\'' +
                ", url='" + url + '\'' +
                ", enable=" + enable +
                ", index=" + index +
                '}';
    }
}

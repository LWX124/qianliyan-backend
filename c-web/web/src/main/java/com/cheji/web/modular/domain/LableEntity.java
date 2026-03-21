package com.cheji.web.modular.domain;

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
 * @since 2019-08-20
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

    private String url;


    private Integer enable;     //是否开启功能


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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "LableEntity{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", type=" + type +
                ", url='" + url + '\'' +
                ", enable=" + enable +
                '}';
    }
}

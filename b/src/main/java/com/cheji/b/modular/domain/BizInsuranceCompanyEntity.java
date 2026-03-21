package com.cheji.b.modular.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 保险公司信息表
 * </p>
 *
 * @author Ashes
 * @since 2019-10-10
 */
@TableName("biz_insurance_company")
public class BizInsuranceCompanyEntity extends Model<BizInsuranceCompanyEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer pid;
    /**
     * 公司名称
     */
    private String name;
    /**
     * 公司图标链接
     */
    private String url;

    @TableField(exist = false)
    private String state;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    @Override
    public String toString() {
        return "BizInsuranceCompanyEntity{" +
                "id=" + id +
                ", pid=" + pid +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}

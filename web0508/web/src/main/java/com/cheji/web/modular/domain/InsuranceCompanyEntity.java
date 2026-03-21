package com.cheji.web.modular.domain;

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
 * @since 2019-08-23
 */
@TableName("biz_insurance_company")
public class InsuranceCompanyEntity extends Model<InsuranceCompanyEntity> {

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

    @Override
    public String toString() {
        return "InsuranceCompanyEntity{" +
        ", id=" + id +
        ", pid=" + pid +
        ", name=" + name +
        ", url=" + url +
        "}";
    }
}

package com.stylefeng.guns.modular.system.model;

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
 * @author stylefeng
 * @since 2018-09-08
 */
@TableName("biz_insurance_company")
public class BizInsuranceCompany extends Model<BizInsuranceCompany> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 公司名称
     */
    private String name;
    /**
     * 公司图标
     */
    private String url;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        return "BizInsuranceCompany{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}

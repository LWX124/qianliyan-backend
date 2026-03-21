package com.cheji.web.modular.domain;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 保险和商户关联表
 * </p>
 *
 * @author Ashes
 * @since 2019-08-23
 */
@TableName("app_insurance_merchants")
public class InsuranceMerchantsEntity extends Model<InsuranceMerchantsEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 保险公司id
     */
    @TableField("insurance_id")
    private Integer insuranceId;
    /**
     * 商户id
     */
    @TableField("user_b_id")
    private Integer userBId;

    @TableField(exist = false)
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(Integer insuranceId) {
        this.insuranceId = insuranceId;
    }

    public Integer getUserBId() {
        return userBId;
    }

    public void setUserBId(Integer userBId) {
        this.userBId = userBId;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "InsuranceMerchantsEntity{" +
                "id=" + id +
                ", insuranceId=" + insuranceId +
                ", userBId=" + userBId +
                ", url='" + url + '\'' +
                '}';
    }
}

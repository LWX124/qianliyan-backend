package com.cheji.web.modular.domain;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 城市表
 * </p>
 *
 * @author Ashes
 * @since 2019-09-20
 */
@TableName("app_city")
public class CityEntity extends Model<CityEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("city_name")
    private String cityName;
    private Integer adcode;
    private Integer citycode;

    @TableField(exist = false)
    private JSONArray jsonArray;

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    /**
     * 推荐城市
     */
    @TableField("recommend_city")
    private Integer recommendCity;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getAdcode() {
        return adcode;
    }

    public void setAdcode(Integer adcode) {
        this.adcode = adcode;
    }

    public Integer getCitycode() {
        return citycode;
    }

    public void setCitycode(Integer citycode) {
        this.citycode = citycode;
    }

    public Integer getRecommendCity() {
        return recommendCity;
    }

    public void setRecommendCity(Integer recommendCity) {
        this.recommendCity = recommendCity;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "CityEntity{" +
        ", id=" + id +
        ", cityName=" + cityName +
        ", adcode=" + adcode +
        ", citycode=" + citycode +
        ", recommendCity=" + recommendCity +
        "}";
    }
}

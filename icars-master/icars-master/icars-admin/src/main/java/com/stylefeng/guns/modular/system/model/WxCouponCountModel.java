package com.stylefeng.guns.modular.system.model;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 微信红包统计model
 * </p>
 *
 * @author duanhong
 * @since 2019-03-14
 */
public class WxCouponCountModel {

    private static final long serialVersionUID = 1L;

    /**
     * 城市名称
     */
    private String cityName;

    private Integer oneAmountNumber;
    private Integer fiveAmountNumber;
    private Integer tenAmountNumber;

    private Integer sumNumber;
    private double sumMoney;
    private double percentage;

    @Override
    public String toString() {
        return "WxCouponCountModel{" +
                "cityName='" + cityName + '\'' +
                ", oneAmountNumber=" + oneAmountNumber +
                ", fiveAmountNumber=" + fiveAmountNumber +
                ", tenAmountNumber=" + tenAmountNumber +
                ", sumNumber=" + sumNumber +
                ", sumMoney=" + sumMoney +
                ", percentage=" + percentage +
                '}';
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getOneAmountNumber() {
        if (oneAmountNumber == null) {
            return 0;
        }
        return oneAmountNumber;
    }

    public void setOneAmountNumber(Integer oneAmountNumber) {
        this.oneAmountNumber = oneAmountNumber;
    }

    public Integer getFiveAmountNumber() {
        if (fiveAmountNumber == null) {
            return 0;
        }
        return fiveAmountNumber;
    }

    public void setFiveAmountNumber(Integer fiveAmountNumber) {
        this.fiveAmountNumber = fiveAmountNumber;
    }

    public Integer getTenAmountNumber() {
        if (tenAmountNumber == null) {
            return 0;
        }
        return tenAmountNumber;
    }

    public void setTenAmountNumber(Integer tenAmountNumber) {
        this.tenAmountNumber = tenAmountNumber;
    }

    public Integer getSumNumber() {
        return sumNumber;
    }

    public void setSumNumber(Integer sumNumber) {
        this.sumNumber = sumNumber;
    }

    public double getSumMoney() {
        return sumMoney;
    }

    public void setSumMoney(double sumMoney) {
        this.sumMoney = sumMoney;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}

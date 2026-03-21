package com.cheji.web.modular.cwork;

import java.math.BigDecimal;

public class PromoteList {
    private Integer id;
    private String name;                //列表
    private String state;               //开通状态
    private String accidentState;       //提报状态
    private BigDecimal money;           //金额

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAccidentState() {
        return accidentState;
    }

    public void setAccidentState(String accidentState) {
        this.accidentState = accidentState;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

}

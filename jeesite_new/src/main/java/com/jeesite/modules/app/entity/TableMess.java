package com.jeesite.modules.app.entity;

import java.math.BigDecimal;

public class TableMess {
    private String id;
    private String name;
    private String leave;
    private BigDecimal balance;
    private Integer type;
    private String nowState;
    private Integer monCount;       //本月台次
    private BigDecimal monAmount;   //本月金额
    private String level;       //级别

    public Integer getMonCount() {
        return monCount;
    }

    public void setMonCount(Integer monCount) {
        this.monCount = monCount;
    }

    public BigDecimal getMonAmount() {
        return monAmount;
    }

    public void setMonAmount(BigDecimal monAmount) {
        this.monAmount = monAmount;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getNowState() {
        return nowState;
    }

    public void setNowState(String nowState) {
        this.nowState = nowState;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLeave() {
        return leave;
    }

    public void setLeave(String leave) {
        this.leave = leave;
    }

    @Override
    public String toString() {
        return "TableMess{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", leave='" + leave + '\'' +
                ", balance=" + balance +
                '}';
    }
}

package com.cheji.web.modular.cwork;

import java.math.BigDecimal;
import java.util.List;

//我的钱包打工类
public class UserWallet {
    private BigDecimal income;  //收入
    private BigDecimal balance; //余额
    private List<BankCard> banks;

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<BankCard> getBanks() {
        return banks;
    }

    public void setBanks(List<BankCard> banks) {
        this.banks = banks;
    }

    @Override
    public String toString() {
        return "UserWallet{" +
                "income=" + income +
                ", balance=" + balance +
                ", banks=" + banks +
                '}';
    }
}

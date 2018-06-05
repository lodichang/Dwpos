package com.dw.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by liang.caixing on 2018/5/4.
 */
public class MemberDetail {

    private StringProperty cardno = new SimpleStringProperty("");
    private StringProperty name = new SimpleStringProperty("");
    private StringProperty tel = new SimpleStringProperty("");
    private StringProperty birth = new SimpleStringProperty("");
    private StringProperty balance = new SimpleStringProperty("");
    private StringProperty gitBalance = new SimpleStringProperty("");
    private StringProperty integral = new SimpleStringProperty("");
    private StringProperty clostDate = new SimpleStringProperty("");

    public MemberDetail(StringProperty cardno, StringProperty name, StringProperty tel, StringProperty birth, StringProperty balance, StringProperty gitBalance, StringProperty integral, StringProperty clostDate) {
        this.cardno = cardno;
        this.name = name;
        this.tel = tel;
        this.birth = birth;
        this.balance = balance;
        this.gitBalance = gitBalance;
        this.integral = integral;
        this.clostDate = clostDate;
    }

    public MemberDetail() {
    }

    public String getCardno() {
        return cardno.get();
    }

    public StringProperty cardnoProperty() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno.set(cardno);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getTel() {
        return tel.get();
    }

    public StringProperty telProperty() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel.set(tel);
    }

    public String getBirth() {
        return birth.get();
    }

    public StringProperty birthProperty() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth.set(birth);
    }

    public String getBalance() {
        return balance.get();
    }

    public StringProperty balanceProperty() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance.set(balance);
    }

    public String getGitBalance() {
        return gitBalance.get();
    }

    public StringProperty gitBalanceProperty() {
        return gitBalance;
    }

    public void setGitBalance(String gitBalance) {
        this.gitBalance.set(gitBalance);
    }

    public String getIntegral() {
        return integral.get();
    }

    public StringProperty integralProperty() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral.set(integral);
    }

    public String getClostDate() {
        return clostDate.get();
    }

    public StringProperty clostDateProperty() {
        return clostDate;
    }

    public void setClostDate(String clostDate) {
        this.clostDate.set(clostDate);
    }
}

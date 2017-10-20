package com.cdeth.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigInteger;

/**
 * Created by tianlei on 2017/十月/19.
 */
@JsonIgnoreProperties(value = {"calcAmount"})
public class Account {

    private Integer id;
    private String userId; //手机号
    //数据库中字符串
    private String amount;

    //java中运算
    private BigInteger calcAmount;

    //
    public Integer getId() {
        return id;
    }

    //
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    //
    public BigInteger getCalcAmount() {

        return new BigInteger(this.amount);

    }

    public void setCalcAmount(BigInteger calcAmount) {

        this.amount = calcAmount.toString();

    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}

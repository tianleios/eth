package com.cdeth.pojo;

import java.math.BigInteger;

/**
 * Created by tianlei on 2017/十月/19.
 */
public class Account {

    private Integer id;
    private String userId; //手机号
    private BigInteger amount;

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

    //
    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

}

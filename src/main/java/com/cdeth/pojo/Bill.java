package com.cdeth.pojo;

import java.math.BigInteger;

/**
 * Created by tianlei on 2017/十月/19.
 */
public class Bill {

    private Integer id;
    private Integer accountId;
    //
    private String from;
    private String to;
    //
    private BigInteger amount;

    public Integer getId() {
        return id;
    }


    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    //      `id` integer AUTO_INCREMENT PRIMARY KEY ,
//  `account_id` integer NOT NULL ,
//            `from` VARCHAR(30) NOT NULL,
//  `to` VARCHAR(30) NOT NULL,
//  `amount` BIGINT NOT NULL DEFAULT 0

}

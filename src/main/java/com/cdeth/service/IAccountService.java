package com.cdeth.service;

import com.cdeth.common.Response;
import com.cdeth.pojo.Account;
import com.cdeth.pojo.Bill;
import com.cdeth.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.security.PublicKey;
import java.util.List;

/**
 * Created by tianlei on 2017/十月/19.
 */
public interface IAccountService {

    public Boolean reg(User user) throws Exception;

    //
    public void insertBill(Bill bill);

    // add
    public int update(Integer accountId,BigInteger addAmount) throws Exception;

    public Account getAccountById(Integer accountId) throws Exception ;
    public Account getAccountByUserId(String userId) throws Exception;

    //列表查流水
    public List<Bill> billList(Integer accountId);

    public Integer mainAccountId();



}

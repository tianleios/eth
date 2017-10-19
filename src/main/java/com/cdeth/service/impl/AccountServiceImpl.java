package com.cdeth.service.impl;

import com.cdeth.common.Response;
import com.cdeth.dao.AccountMapper;
import com.cdeth.pojo.Account;
import com.cdeth.pojo.Bill;
import com.cdeth.pojo.User;
import com.cdeth.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by tianlei on 2017/十月/19.
 */
@Service("iAccountService")
public class AccountServiceImpl implements IAccountService {

    @Autowired
    AccountMapper accountMapper;

    @Override
    public Boolean reg(User user) throws Exception {

      int count =  this.accountMapper.insertAccount(user.getId());
      return count == 1;

    }

    @Override
    public int update(Integer accountId, BigInteger addAmount) throws Exception {

      Account account =  this.getAccountById(accountId);
      account.setAmount(account.getAmount().add(addAmount));
      return this.accountMapper.update(account);

    }

    @Override
    public Account getAccountById(Integer accountId) throws Exception {

        return  this.accountMapper.getAccountById(accountId);

    }

    @Override
    public Account getAccountByUserId(String userId) throws Exception {

        return  this.accountMapper.getAccountByUserId(userId);

    }

    @Override
    public void insertBill(Bill bill) {

        this.accountMapper.insertBill(bill);

    }

    @Override
    public List<Bill> billList(Integer accountId) {

        return this.accountMapper.billList(accountId);

    }
}

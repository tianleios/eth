package com.cdeth.dao;

import com.cdeth.pojo.Account;
import com.cdeth.pojo.Bill;
import com.cdeth.pojo.User;

import java.util.List;

/**
 * Created by tianlei on 2017/十月/19.
 */
public interface AccountMapper {

   public int insertAccount(String userId);
   public int update(Account account);
   public Account getAccountById(Integer id);
   public Account getAccountByUserId(String userId);

   public int insertBill(Bill bill);
   public List<Bill> billList(Integer accountId);


}

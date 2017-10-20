package com.cdeth.controller;

import com.cdeth.common.Response;
import com.cdeth.pojo.Account;
import com.cdeth.pojo.Bill;
import com.cdeth.pojo.User;
import com.cdeth.service.impl.AccountServiceImpl;
import com.cdeth.service.impl.EthServiceImpl;
import com.cdeth.service.impl.UserServiceImpl;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by tianlei on 2017/十月/18.
 */

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserServiceImpl iUserService;
    //
    @Autowired
    EthServiceImpl iEthService;

    @Autowired
    AccountServiceImpl iAccountService;


    @ResponseBody
    @PostMapping("/reg")
    public Response balance(String mobile, String password) throws Exception {

        if ((mobile == null) || (password == null)) {
            return Response.failure(-10,"参数错误");
        }
        //2.检查用户是否存在
        User checkUser = this.iUserService.getUserByMobile(mobile);
        if (checkUser != null) {
            return Response.failure(-1,"手机号已经注册");
        }

        //1.注册以太币地址
        String ethPassword = password + "ethPassword";
        String address = iEthService.reg(ethPassword);


        //3.注册用户
        String userId = "u" + Long.toString(new  Date().getTime());
        User user = new User();
        user.setId(userId);
        user.setMobile(mobile);
        user.setPassword(password);
        user.setAddress(address);
        user.setEthPassword(ethPassword);
        this.iAccountService.reg(user);

        //3.
        return this.iUserService.reg(user);

    }

    @ResponseBody
    @PostMapping("/login")
    public Response login(String mobile, String password) throws Exception {

        if (mobile == null || password == null) {
            return Response.failure(-1,"参数不能为空");
        }

        User user =  this.iUserService.getUserByMobile(mobile);
        if (user == null) {
            return Response.failure(-1,"用户不存在");
        }
        //
        if (!user.getPassword().equals(password)) {
            return Response.failure(-1,"密码错误");
        }
        //
       return  Response.success(user);

    }

    @ResponseBody
    @GetMapping("/{userId}")
    public Response userInfo(@PathVariable("userId") String userId) throws Exception {

        return Response.success(this.iUserService.getUserById(userId));

    }


    @ResponseBody
    @PostMapping("/tx")
    public Response tx(String userId, String toMobile, BigInteger amount) throws Exception {

        User toUser = this.iUserService.getUserByMobile(toMobile);
        User fromUser = this.iUserService.getUserById(userId);

        if (toUser == null) {
            return Response.failure(-1,"手机号不是该平台用户");
        }

        //
        Account fromAccount =  this.iAccountService.getAccountByUserId(userId);
        if (fromAccount.getCalcAmount().subtract(amount).compareTo(BigInteger.valueOf(0)) == -1) {
            return Response.failure(-1,"余额不足");
        }

        // 虚拟账户减去
        BigInteger subAmount = BigInteger.valueOf(-1).multiply(amount);
        this.iAccountService.update(fromAccount.getId(),subAmount);
        Bill bill = new Bill();
        bill.setFrom(fromUser.getAddress());
        bill.setTo(toUser.getAddress());
        bill.setAccountId(fromAccount.getId());
        bill.setAmount(subAmount.toString());
        this.iAccountService.insertBill(bill);

        // 虚拟账户增加
        Account toAccount = this.iAccountService.getAccountByUserId(toUser.getId());
        this.iAccountService.update(toAccount.getId(),amount);
        Bill toBill = new Bill();
        toBill.setFrom(fromUser.getAddress());
        toBill.setTo(toUser.getAddress());
        toBill.setAccountId(toAccount.getId());
        toBill.setAmount(amount.toString());
        this.iAccountService.insertBill(toBill);

        return Response.failure(0,"转账成功");

    }

    @ResponseBody
    @PostMapping("/withdraw")
    public Response withdraw(String userId, String toAddress, BigInteger amount) throws Exception {

        if (userId == null || toAddress == null || amount == null) {
            return Response.failure(-1,"参数不正确");
        }

        User currentUser =  this.iUserService.getUserById(userId);

        //检查余额
        Account account =  this.iAccountService.getAccountByUserId(userId);
        if (account.getCalcAmount().subtract(amount).compareTo(BigInteger.valueOf(0)) == -1) {
            return Response.failure(-1,"余额不足");
        }

        // 虚拟账户减去
        BigInteger subAmount = BigInteger.valueOf(-1).multiply(amount);
        this.iAccountService.update(account.getId(),subAmount);


        //流水
//        int a = 10;
        Bill bill = new Bill();
        bill.setFrom(currentUser.getAddress());
        bill.setTo(toAddress);
        bill.setAccountId(account.getId());
        bill.setAmount(subAmount.toString());
        this.iAccountService.insertBill(bill);

        //由平台账户转出
        User platformUser = this.iUserService.getPlatformUser();
        return this.iEthService.tx(platformUser.getAddress(),platformUser.getEthPassword(),toAddress,amount);

    }


    @ResponseBody
    @GetMapping("/account/{userId}")
    public Response account(@PathVariable("userId") String userId) throws Exception {

        if (userId == null || userId.length() <= 0) {
            return Response.failure(-1,"请传入正确的userId");
        }

      return  Response.success(this.iAccountService.getAccountByUserId(userId));


    }

    @ResponseBody
    @GetMapping("/billList/{userId}")
    public Response accountList(@PathVariable("userId") String userId) throws Exception {

        if (userId == null || userId.length() <= 0) {
            return Response.failure(-1,"请传入正确的userId");
        }

        Account account = this.iAccountService.getAccountByUserId(userId);

        return  Response.success(this.iAccountService.billList(account.getId()));

    }

}

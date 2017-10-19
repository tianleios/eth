package com.cdeth.common;

import com.cdeth.pojo.Account;
import com.cdeth.pojo.User;
import com.cdeth.service.impl.AccountServiceImpl;
import com.cdeth.service.impl.EthServiceImpl;
import com.cdeth.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.parity.Parity;

/**
 * Created by tianlei on 2017/十月/19.
 */
public class EthManager {
    private static EthManager ourInstance = new EthManager();

    public static EthManager getInstance() {
        return ourInstance;
    }

    private UserServiceImpl iUserService;

    private AccountServiceImpl iAccountService;

    private EthServiceImpl iEthService;


    public  String mainAccountAddress;
    public  String mainAccountEthPassword;

    public Web3j web3j;
    public Parity parity;

    private EthManager() {
        web3j = Web3j.build(new HttpService("http://localhost:8545"));
        parity = Parity.build(new HttpService("http://localhost:8545"));

        this.iUserService = new UserServiceImpl();
        this.iAccountService = new AccountServiceImpl();
        this.iEthService = new EthServiceImpl();

    }

    public void start() {

        //todo 开定时器，定时检查账户余额， 有余额的转至 转至主账户，虚拟账户增加余额
        //
        web3j.pendingTransactionObservable().subscribe( tx -> {
            //挂起的交易
            System.out.print("交易开始");


        });

        //
        web3j.transactionObservable().subscribe( tx -> {

            //完成的交易，推送到
            System.out.print("交易完成");

            //1.外部向 我们的用户转账
            //2. 我们主动发起的转账

//            org.web3j.protocol.core.methods.response.Transaction transaction = tx;

            String toAddress =  tx.getTo();

            User user = null;
            try {
                 user = this.iUserService.getUserByAddress(toAddress);
                 if (user != null) {
                     //1.转到主账户
                     // 手续费 + 转账金额 = transaction.getValue();
                     // 先不考虑手续费
                     this.iEthService.tx(user.getAddress(),user.getEthPassword(),this.mainAccountAddress,tx.getValue());

                     //2.TODO user虚拟账户 增加余额
                     Account userAccount = this.iAccountService.getAccountByUserId(user.getId());
                     this.iAccountService.update(userAccount.getId(),tx.getValue());

                 }

            } catch (Exception e) {

                 e.printStackTrace();

            }

        });
    }



}

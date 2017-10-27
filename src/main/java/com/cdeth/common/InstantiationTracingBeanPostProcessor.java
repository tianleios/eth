package com.cdeth.common;

import com.cdeth.pojo.Account;
import com.cdeth.pojo.Bill;
import com.cdeth.pojo.User;
import com.cdeth.service.impl.AccountServiceImpl;
import com.cdeth.service.impl.EthServiceImpl;
import com.cdeth.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.infura.InfuraHttpService;
import org.web3j.protocol.parity.Parity;

import java.math.BigInteger;

/**
 * Created by tianlei on 2017/十月/19.
 */
public class InstantiationTracingBeanPostProcessor implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserServiceImpl iUserService;

    @Autowired
    private EthServiceImpl iEthService;

    @Autowired
    private AccountServiceImpl iAccountService;

    public Web3j web3j;
    public Parity parity;
    public String mainAccountAddress ;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        //
        if(event.getApplicationContext().getParent() == null){

            String url = EthManager.url;
            if (url.indexOf("infura") != -1) {

                web3j = Web3j.build(new InfuraHttpService(url));
                parity = Parity.build(new InfuraHttpService(url));

            } else {

                web3j = Web3j.build(new HttpService(url));
                parity = Parity.build(new HttpService(url));
            }


            try {

                String version =  web3j.web3ClientVersion().send().getWeb3ClientVersion();
                BigInteger count =  web3j.ethBlockNumber().send().getBlockNumber();
                int a = 10;
            } catch (Exception e) {

                e.printStackTrace();
            }


        }

        web3j.blockObservable(true).subscribe(block -> {

            int a = 10;

        });

        //
            web3j.pendingTransactionObservable().subscribe( tx -> {
                //挂起的交易
                System.out.print("交易开始");

            });

//
//            //
//            web3j.transactionObservable().subscribe( ( tx) -> {
//
//                //完成的交易，推送到
//                System.out.print("交易完成");
//
//                //1.外部向 我们的用户转账
//                //2. 我们主动发起的转账
//            org.web3j.protocol.core.methods.response.Transaction transaction = tx;
//                String toAddress = tx.getTo();
//                BigInteger value = tx.getValue();
//                //
//                User user = null;
//                //
//                try {
//                    user = this.iUserService.getUserByAddress(toAddress);
//                    if (user != null) {
//                        //1.转到主账户
//                        // 手续费 + 转账金额 = transaction.getValue();
//                        // 先不考虑手续费
//                        this.iEthService.tx(user.getAddress(),user.getEthPassword(),this.mainAccountAddress,value);
//
//                        //2.TODO user虚拟账户 增加余额
//                        Account userAccount = this.iAccountService.getAccountByUserId(user.getId());
//
//                        this.iAccountService.update(userAccount.getId(),value);
//
//                        //3.增加流水
//                        Bill bill = new Bill();
//                        bill.setAccountId(userAccount.getId());
//                        bill.setAmount(value.toString());
//                        bill.setFrom(tx.getFrom());
//                        bill.setTo(user.getMobile());
//                        this.iAccountService.insertBill(bill);
//                        //
//
//                    }
//
//                } catch (Exception e) {
//
//                    e.printStackTrace();
//
//                }
//
//            });
//
        }



}

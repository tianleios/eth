package com.cdeth.service.impl;

import com.cdeth.common.Response;
import com.cdeth.pojo.Account;
import com.cdeth.pojo.User;
import com.cdeth.service.IEthService;
import jnr.ffi.Struct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import com.cdeth.common.*;
import org.web3j.protocol.parity.Parity;
import org.web3j.protocol.parity.methods.response.PersonalUnlockAccount;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tianlei on 2017/十月/18.
 */
@Service("iEthService")
public class EthServiceImpl implements IEthService {

     static  private Web3j web3j = null;
     static  private Parity parity = null;

//     @Autowired
//     private UserServiceImpl iUserService;

     static {

         String url =   EthManager.url;


         web3j = Web3j.build(new HttpService(url));
         parity = Parity.build(new HttpService(url));

         //
//         web3j.pendingTransactionObservable().subscribe( tx -> {
//             //挂起的交易
//             System.out.print("交易开始");
//
//         });

         //
//         web3j.transactionObservable().subscribe( tx -> {
//
//             //完成的交易，推送到
//             System.out.print("交易完成");
//
//             //1.外部向 我们的用户转账
//             //2. 我们主动发起的转账
//             //
//             String toAddress =  tx.getTo();
//
//             User user = null;
//             try {
//                 user = this.iUserService.getUserByAddress(toAddress);
//                 if (user != null) {
//                     //1.转到主账户
//                     // 手续费 + 转账金额 = transaction.getValue();
//                     // 先不考虑手续费
//                     this.iEthService.tx(user.getAddress(),user.getEthPassword(),this.mainAccountAddress,tx.getValue());
//
//                     //2.TODO user虚拟账户 增加余额
//                     Account userAccount = this.iAccountService.getAccountByUserId(user.getId());
//                     this.iAccountService.update(userAccount.getId(),tx.getValue());
//
//                 }
//
//             } catch (Exception e) {
//
//                 e.printStackTrace();
//
//             }
//
//         });

     }

    //
    //
    @Override
    public String reg(String password) throws Exception {
        if (password == null || password.length() <= 0) {
            throw new Exception("密码格式不对");
        }

      return  parity.personalNewAccount(password).send().getAccountId();

    }

    @Override
    public Response tx(String from, String fromPassword, String to, BigInteger amount) throws Exception {

        if (from == null || fromPassword == null || to == null || amount == null) {

            return Response.failure(-1,"参数不能为空");

        }

        String mineAddress = from;
        String otherAddress = to;

        //
        String mineAddressPasssword = fromPassword;
        Parity parity = Parity.build(new HttpService());
        PersonalUnlockAccount personalUnlockAccountResp = parity.personalUnlockAccount(mineAddress, mineAddressPasssword).sendAsync().get();

        if (personalUnlockAccountResp.getError() != null) {
            return Response.failure(-300,personalUnlockAccountResp.getError().getMessage());
        }
        //
        if (!personalUnlockAccountResp.accountUnlocked()) {
            //账户处于锁定状态
            return Response.failure(ResponseCode.ACCOUNT_LOCAK.getCode(),ResponseCode.ACCOUNT_LOCAK.getDesc());
        }

        //获得 nonce
        EthGetTransactionCount transactionCount = web3j.ethGetTransactionCount(mineAddress, DefaultBlockParameterName.LATEST).sendAsync().get();

        BigInteger nonce = transactionCount.getTransactionCount();

        //指定交易的gasPrice, 如果 null ，会获取 链上的 gasPrice, 可以为任意值
        BigInteger gasPrice = BigInteger.valueOf(0);
        BigInteger gaslimit = BigInteger.valueOf(30000);
//        BigInteger gasPrice = null;
//        BigInteger gaslimit = null;



        BigInteger value = amount;

        //1.创建交易
        org.web3j.protocol.core.methods.request.Transaction transaction = org.web3j.protocol.core.methods.request.Transaction.createEtherTransaction(mineAddress, nonce, gasPrice, gaslimit, otherAddress, value);

        //2.预估手续费
      EthEstimateGas ethEstimateGas = web3j.ethEstimateGas(transaction).send();


        //2.发起交易
        EthSendTransaction ethSendTransactionResp = parity.ethSendTransaction(transaction).sendAsync().get();

        if (ethSendTransactionResp.getError() != null) {

            return Response.failure(-300,ethSendTransactionResp.getError().getMessage());

        }
        //结果哈希
        String transactionHash = ethSendTransactionResp.getTransactionHash();

        if (transactionHash == null || transactionHash.isEmpty()) {

            //交易先被pending，交易应该有  -- 待确认，已确认（真正成功）
            //此处应该等到回调
            return Response.failure(ResponseCode.TRANSFER_FAILURE.getCode(),ResponseCode.TRANSFER_FAILURE.getDesc());

        }

        return Response.success("转账成功");
    }

    //
    @Override
    public Response balance(String address) throws Exception {

        //todo 校验地址
        if (address == null || address.length() <= 0) {
            return Response.failure(ResponseCode.ADDRESS_ERROR.getCode(),ResponseCode.ADDRESS_ERROR.getDesc());
        }

        //
        Web3ClientVersion clientVersion =
                web3j.web3ClientVersion().sendAsync().get();

        //
        Request<?, EthGetBalance> ethGetBalanceRequest = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST);
        BigInteger balance = ethGetBalanceRequest.send().getBalance();

        //
        Map banlanceMap = new HashMap();
        banlanceMap.put("banlance", balance);
        return Response.success(banlanceMap);

    }

//    @Override
//    public String mainAccountAddress() {
//        return "";
//    }
//
//    @Override
//    public String mainAccountEthPassword() {
//        return null;
//    }

}

package com.cdeth.service.impl;

import com.cdeth.common.Response;
import com.cdeth.service.IEthService;
import jnr.ffi.Struct;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
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

    static private Web3j web3j = null;
    static private Parity parity = null;

    static {

        // parity 应该是一个功能丰富的 web3j
        web3j = Web3j.build(new HttpService("http://localhost:8545"));
        parity = Parity.build(new HttpService("http://localhost:8545"));

    }

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

        if (from == null || to == null || amount == null) {
            throw  new Exception("参数不能为空");
        }

        String mineAddress = from;
        String otherAddress = to;

        //
        String mineAddressPasssword = fromPassword;
        Parity parity = Parity.build(new HttpService());
        PersonalUnlockAccount personalUnlockAccount = parity.personalUnlockAccount(mineAddress, mineAddressPasssword).sendAsync().get();
        //
        if (!personalUnlockAccount.accountUnlocked()) {
            //账户处于锁定状态
            return Response.failure(ResponseCode.ACCOUNT_LOCAK.getCode(),ResponseCode.ACCOUNT_LOCAK.getDesc());
        }


        //获得 nonce
        EthGetTransactionCount transactionCount = web3j.ethGetTransactionCount(mineAddress, DefaultBlockParameterName.LATEST).sendAsync().get();

        BigInteger nonce = transactionCount.getTransactionCount();
        BigInteger gasPrice = BigInteger.valueOf(2);
        BigInteger gaslimit = BigInteger.valueOf(30000);

        BigInteger jinLv = new BigInteger("1000000000000000000");
        BigInteger value = amount.multiply(jinLv);

        //1.创建交易
        org.web3j.protocol.core.methods.request.Transaction transaction = org.web3j.protocol.core.methods.request.Transaction.createEtherTransaction(mineAddress, nonce, gasPrice, gaslimit, otherAddress, value);

        //2.发起交易
        EthSendTransaction ethSendTransactionResp = parity.ethSendTransaction(transaction).sendAsync().get();

        //结果哈希
        String transactionHash = ethSendTransactionResp.getTransactionHash();

        if (transactionHash == null || transactionHash.isEmpty()) {

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
}

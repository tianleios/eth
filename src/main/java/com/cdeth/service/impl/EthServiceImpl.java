package com.cdeth.service.impl;

import com.cdeth.common.Response;
import com.cdeth.pojo.Account;
import com.cdeth.pojo.User;
import com.cdeth.service.IEthService;
//import jnr.ffi.Struct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.parity.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.*;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import com.cdeth.common.*;
import org.web3j.protocol.infura.InfuraHttpService;
import org.web3j.protocol.parity.Parity;
//import org.web3j.protocol.parity.methods.response.PersonalUnlockAccount;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tianlei on 2017/十月/18.
 */
@Service("iEthService")
public class EthServiceImpl implements IEthService {

     static  public Web3j web3j = null;
     static  private Parity parity = null;

     static {

         String url =   EthManager.url;

         if (url.indexOf("infura") != -1) {

             web3j = Web3j.build(new InfuraHttpService(url));
             parity = Parity.build(new InfuraHttpService(url));

         } else {

             web3j = Web3j.build(new HttpService(url));
             parity = Parity.build(new HttpService(url));
         }

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

    //通过password 进行交易，但要确保 node(节点)上有，相应的keystore文件
    @Override
    public Response tx(String from, String fromPassword, String to, BigInteger amount) throws Exception {

        if (from == null || fromPassword == null || to == null || amount == null) {

            return Response.failure(-1,"参数不能为空");

        }

        String mineAddress = from;
        String otherAddress = to;

        //密码- - 用于 - -解锁账户
        String mineAddressPasssword = fromPassword;

        PersonalUnlockAccount personalUnlockAccountResp = parity.personalUnlockAccount(mineAddress, mineAddressPasssword).sendAsync().get();

        if (personalUnlockAccountResp.getError() != null) {
            return Response.failure(-300,personalUnlockAccountResp.getError().getMessage());
        }
        //
        if (!personalUnlockAccountResp.accountUnlocked()) {
            //账户处于锁定状态
            return Response.failure(ResponseCode.ACCOUNT_LOCAK.getCode(),ResponseCode.ACCOUNT_LOCAK.getDesc());
        }

//        parity.personalSignAndSendTransaction()

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




    // 发送交易，通过钱包文件
    public void txByWalletFile(String from, String fromPassword, String to, BigInteger amount) throws Exception {

         Web3j web3j = Web3j.build(new HttpService());

         //应该是加载钱包的 keyStore 文件
        File walletFile = new File("");
         Credentials credentials = WalletUtils.loadCredentials(fromPassword,walletFile);
        TransactionReceipt transactionReceipt = Transfer.sendFunds(web3j,credentials,to,new BigDecimal(amount), Convert.Unit.WEI);


    }

    @Override
    public void customTxByWalletFile(String from, String fromPassword, String to, BigInteger amount) throws Exception{

//        Web3j web3 = Web3j.build(new HttpService());

        String fileDirPath = "/Users/tianlei/Documents/ethereum/data/keystore";
        File keyStoreFileDir = new File(fileDirPath);
        File[] subFiles = keyStoreFileDir.listFiles();
        File keystoreFile = null;
        for (File file : subFiles) {
            if (file.isDirectory() != true) {

                //TODO 这种判断方式，不太对
                if(file.getName().indexOf(from.substring(2,10)) != -1) {
                    //找到了该文件
                    keystoreFile = file;
                    break;
                }
            }
        }

        if (keystoreFile == null) {
            //
            throw new Exception("未找到文件");
        }

        //
        Credentials credentials = WalletUtils.loadCredentials(fromPassword, keystoreFile);
        //
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                from, DefaultBlockParameterName.LATEST).sendAsync().get();
        //
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        //TODO 动态获取
        BigInteger gasLimit = BigInteger.valueOf(30000);
        BigInteger gasPrice = BigInteger.valueOf(4000);

        //本地签名的
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce,gasPrice,gasLimit,to,amount,"121312");

      //签名
      byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
      String hexValue = Numeric.toHexString(signedMessage);
      EthSendTransaction ethSendTransaction =  web3j.ethSendRawTransaction(hexValue).sendAsync().get();

      if (ethSendTransaction.getError() != null) {
          //failure
      }

      //success

    }

    @Override
    public void log(String address) throws Exception {

       String str =  "DepositMade(hexstring,uint256)";
       String hexStr =  Encrypt.byte2Hex(str.getBytes());

//    Web3Sha3  web3Sha3 =  web3j.web3Sha3(hexStr).send();

//    if (web3Sha3.getError() != null) {
//        throw new Exception(web3Sha3.getError().getMessage());
//    }
        String topic = Hash.sha3(hexStr);

//        web3Sha3.getResult();

        org.web3j.protocol.core.methods.request.EthFilter ethFilter = new EthFilter(DefaultBlockParameterName.EARLIEST,DefaultBlockParameterName.LATEST,address);

      EthLog ethLogResp =  web3j.ethGetLogs(ethFilter).send();
      if (ethLogResp.getError() != null) {

          throw new Exception(ethLogResp.getError().getMessage());

      }

      ethLogResp.getResult().forEach(logResult -> {

          int a = 10;

      });

//              .getLogs().forEach(logResult -> {
//
//             int a = 10;
//
//         });

    }
}

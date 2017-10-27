package com.cdeth.controller;

import com.cdeth.common.Response;
import com.cdeth.service.impl.EthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.Web3j;
import rx.Subscription;

import javax.ws.rs.GET;
import java.io.File;
import java.math.BigInteger;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@Controller
@RequestMapping("/eth")
public class EthController {

    @Autowired()
    EthServiceImpl iEthService;


    @ResponseBody
    @GetMapping("/balance/{address}")
    public Response balance(@PathVariable("address") String address) throws Exception {

        return  this.iEthService.balance(address);

    }

    @ResponseBody
    @PostMapping("/tx")
    public Response tx(String from,String fromPassword, String  to, BigInteger amount) throws Exception {

        return  this.iEthService.tx(from,fromPassword,to,amount);

    }

    @ResponseBody
    @GetMapping("/log/{address}")
    public void log(@PathVariable("address") String address) throws Exception {


        String fileDirPath = "/Users/tianlei/Library/Ethereum/keystore";
        File keyStoreFileDir = new File(fileDirPath);
        File[] subFiles = keyStoreFileDir.listFiles();
        String keystoreFileName = null;
        for (File file : subFiles) {
            if (file.isDirectory() != true) {
                if(file.getName().indexOf(address.substring(2,10)) != -1) {
                    //找到了该文件
                    keystoreFileName = file.getName();
                    break;
                }
                int a = 10;
            }
        }

        if (keystoreFileName == null) {
            //
            throw  new Exception("未找到文件");
        }
        String keystoreFileTotalPath = fileDirPath + "/" + keystoreFileName;



        Subscription subscription = EthServiceImpl.web3j.blockObservable(true).subscribe(block -> {

            int a = 10;

            });


//        this.iEthService.log(address);
        subscription.unsubscribe();

    }


    @ResponseBody
    @PostMapping("/txByWalletFile")
    public void txByWalletFile(String from, String fromPassword, String to, BigInteger amount) throws Exception {

        this.iEthService.customTxByWalletFile(from,fromPassword,to,amount);

    }






}

package com.cdeth.controller;

import com.cdeth.common.Response;
import com.cdeth.service.impl.EthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.GET;
import java.math.BigInteger;

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
    public Response tx(String from, String  to, BigInteger amount) throws Exception {

        String password = "";
        return  this.iEthService.tx(from,password,to,amount);

    }






}

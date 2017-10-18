package com.cdeth.service;

import com.cdeth.common.Response;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

/**
 * Created by tianlei on 2017/十月/18.
 */
public interface IEthService {

    // 获得余额
    public Response balance(String address) throws Exception;

    //转账
    public Response tx(String from, String fromPassword, String to, BigInteger amount) throws Exception;

    //注册
    public String reg(String password) throws Exception;

}

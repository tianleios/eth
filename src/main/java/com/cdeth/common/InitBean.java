package com.cdeth.common;

import com.cdeth.service.impl.UserServiceImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by tianlei on 2017/十月/19.
 */
public class InitBean implements InitializingBean {


    @Override
    public void afterPropertiesSet() throws Exception {

//        EthManager.getInstance().start();
//        EthManager.getInstance().mainAccountAddress = "0x53663d7126cfDaE77165aD3Edaf7E680814b2AEc";
//        EthManager.getInstance().mainAccountEthPassword = "q4121585";
//
//        System.out .println ("----------------------------" );
    }
}

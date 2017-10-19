package com.cdeth.common;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by tianlei on 2017/十月/19.
 */
public class InitListener implements ServletContextListener {


    @Override
    public void contextInitialized(ServletContextEvent sce) {

//        EthManager.getInstance().start();
//        EthManager.getInstance().mainAccountAddress = "0x53663d7126cfDaE77165aD3Edaf7E680814b2AEc";
//        EthManager.getInstance().mainAccountEthPassword = "q4121585";
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {


    }
}

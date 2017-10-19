package com.cdeth.service;

import com.cdeth.common.Response;
import com.cdeth.pojo.User;

/**
 * Created by tianlei on 2017/十月/18.
 */
public interface IUserService {

    public Response reg(User user) throws Exception;
//    public Response userInfo(User user) throws Exception;

    public User getUserByAddress(String address) throws  Exception;

    public User getUserByMobile(String mobile) throws  Exception;

    public User getUserById(String userId) throws  Exception;


}

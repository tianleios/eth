package com.cdeth.service.impl;

import com.cdeth.common.Response;
import com.cdeth.dao.UserMapper;
import com.cdeth.pojo.User;
import com.cdeth.service.IEthService;
import com.cdeth.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tianlei on 2017/十月/18.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    EthServiceImpl iEthService;


    @Override
    public Response reg(User user) throws Exception  {

        //TODO
        //检查手机号是否已经注册

        //1.注册以太币地址
        String password = user.getPassword();
        String ethPassword = password + "ethPassword";
        String address = iEthService.reg(ethPassword);

        //2.注册该用户
        user.setEthPassword(ethPassword);
        user.setAddress(address);
        int count =   this.userMapper.insertUser(user);

        //commit
if (count == 1) {
       //获得useId

    Map map = new HashMap();
    map.put("userId","");
    return Response.success(map);
} else {
    return Response.failure(-1,"注册失败");
}
    }
}

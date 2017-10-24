package com.cdeth.service.impl;

import com.cdeth.common.Response;
import com.cdeth.dao.UserMapper;
import com.cdeth.pojo.User;
import com.cdeth.service.IEthService;
import com.cdeth.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.soap.SOAPBinding;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

/**
 * Created by tianlei on 2017/十月/18.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public Response reg(User user) throws Exception  {

        //TODO
        //检查手机号是否已经注册

        //2.注册该用户
//        user.setEthPassword(ethPassword);
//        user.setAddress(address);
        int count =  this.userMapper.insertUser(user);

        //3.注册账户
        User successUser = this.userMapper.getUserByAddress(user.getAddress());


        //commit
if (count == 1) {
       //获得useId

                Map map = new HashMap();
               map.put("userId",successUser.getId());
               return Response.success(map);
} else {
               return Response.failure(-1,"注册失败");
}
    }

    @Override
    public User getUserByAddress(String address) throws Exception {

        if (address == null || address.length() <= 0) {
            return null;
        }

        return this.userMapper.getUserByAddress(address);
    }

    @Override
    public User getUserByMobile(String mobile) throws Exception {

        return this.userMapper.getUserByMobile(mobile);

    }

    @Override
    public User getUserById(String userId) throws Exception {

        return this.userMapper.getUserById(userId);

    }

    @Override
    public User getPlatformUser() throws Exception {
        return this.getUserById("u000000000000");
    }

    private rx.Observable observableTest() {
        return rx.Observable.create(subscriber -> {

            subscriber.onNext("");

//            subscriber.onError();

            subscriber.onCompleted();
        });
    }

}

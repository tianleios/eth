package com.cdeth.dao;

import com.cdeth.pojo.User;
import org.apache.ibatis.annotations.*;
import org.mybatis.spring.annotation.MapperScan;

import java.io.StringReader;
import java.util.List;

/**
 * Created by tianlei on 2017/8/2.
 */
public interface UserMapper {


    int insertUser(User user);

//    int changePwd(@Param("userId") int userId, @Param("password") String password); //修改哪个用户的pwd
//    User getUserByMobile(String mobile);
//    List<User> getUsers();
//    List<User> getUsersByPage(@Param("start") int start, @Param("limit") int limit);
//
//    int checkUser(String phone);


}

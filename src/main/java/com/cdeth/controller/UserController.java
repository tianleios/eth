package com.cdeth.controller;

import com.cdeth.common.Response;
import com.cdeth.pojo.User;
import com.cdeth.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by tianlei on 2017/十月/18.
 */

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserServiceImpl iUserService;

    @ResponseBody
    @PostMapping("/reg")
    public Response balance(String mobile, String password) throws Exception {

        if ((mobile == null) || (password == null)) {
            return Response.failure(-10,"参数错误");
        }

        User user = new User();
        user.setMobile(mobile);
        user.setPassword(password);


       return this.iUserService.reg(user);

    }

}

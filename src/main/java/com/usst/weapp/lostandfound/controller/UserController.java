package com.usst.weapp.lostandfound.controller;

import com.usst.weapp.lostandfound.model.common.Response;
import com.usst.weapp.lostandfound.model.dto.UserDTO;
import com.usst.weapp.lostandfound.model.entity.UserDO;
import com.usst.weapp.lostandfound.service.UserService;
import com.usst.weapp.lostandfound.utils.WelinkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Sunforge
 * @Date 2021-07-13 23:23
 * @Version V1.0.0
 * @Description
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    WelinkUtil welinkUtil;

    @Autowired
    UserService userService;

    @GetMapping("/basicInfo")
    public Response getUserInfo(Principal principal) throws IOException {
        System.out.println(principal.getName());
        UserDO user = userService.getUserByWelinkId(principal.getName());
//        UserDO user = welinkUtil.getUserByUserInfoStr(principal.getName());
        UserDTO userDTO = new UserDTO(user);
        return Response.success(userDTO);
    }
}

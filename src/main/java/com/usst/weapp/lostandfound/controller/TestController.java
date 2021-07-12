package com.usst.weapp.lostandfound.controller;

import com.usst.weapp.lostandfound.dao.UserDao;
import com.usst.weapp.lostandfound.model.entity.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author Sunforge
 * @Date 2021-07-11 20:38
 * @Version V1.0.0
 * @Description
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    UserDao userDao;

    @GetMapping ("/hello")
    public String hello(){
        return "ni hao";
    }
    @GetMapping ("/testinsert")
//    public UserDO testInsert(){
    public String testInsert(){
//        UserDO user = new UserDO("123","12364","12346","123465");
//        return userDao.insertUser(user);
//        return user.toString();
        return "success";
    }


}


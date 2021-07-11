package com.usst.weapp.lostandfound.controller;

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
    @GetMapping ("/hello")
    public String hello(){
        return "ni hao";
    }
}

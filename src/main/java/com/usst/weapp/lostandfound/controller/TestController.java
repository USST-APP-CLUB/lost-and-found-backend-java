package com.usst.weapp.lostandfound.controller;

import com.usst.weapp.lostandfound.dao.UserDao;
import com.usst.weapp.lostandfound.model.entity.UserDO;
import com.usst.weapp.lostandfound.service.ImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;


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

    @Autowired
    ImgService imgService;

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

    @GetMapping(value = "/image/{id}")
    @ResponseBody
    public BufferedImage getImage(@PathVariable String id) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(imgService.getImage(id));    //将b作为输入流；

        BufferedImage image = ImageIO.read(in);     //将in作为输入流，读取图片存入image中，而这里in可以为
        return image;
    }



}


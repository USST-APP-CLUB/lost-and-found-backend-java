package com.usst.weapp.lostandfound.dao.impl;

import com.usst.weapp.lostandfound.dao.UserDao;
import com.usst.weapp.lostandfound.model.entity.UserDO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserDaoImplTest {
    @Autowired
    UserDao userDao;

    @Test
    void insertUser() {
//        UserDO user = new UserDO();
//        user.setUserId("123");
//        user.setUserWelinkId("123456");
//        user.setDepartments(new String[]{"123"});
//        user.setNickname("123");
//        user.setRandomAvatarUrl("123");
//        System.out.println(userDao.insertUser(user,"user"));
    }
}
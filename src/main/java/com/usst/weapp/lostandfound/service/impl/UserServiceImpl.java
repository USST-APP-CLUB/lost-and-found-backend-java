package com.usst.weapp.lostandfound.service.impl;

import com.usst.weapp.lostandfound.constants.enums.MongoMethod;
import com.usst.weapp.lostandfound.dao.UserDao;
import com.usst.weapp.lostandfound.model.entity.UserDO;
import com.usst.weapp.lostandfound.service.UserService;
import com.usst.weapp.lostandfound.service.extention.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Sunforge
 * @Date 2021-07-12 10:42
 * @Version V1.0.0
 * @Description
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<UserDO> implements UserService {


    @Override
    public UserDO getUserByWelinkId(String welinkId) {
        Map<String, Object> queryConditions = new HashMap<>();
        queryConditions.put("userWelinkId", welinkId);
        UserDO user = this.find(queryConditions, UserDO.class, "user").get(0);
        assert user != null;
        return user;
    }
}

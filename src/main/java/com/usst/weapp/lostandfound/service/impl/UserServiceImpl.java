package com.usst.weapp.lostandfound.service.impl;

import com.usst.weapp.lostandfound.dao.UserDao;
import com.usst.weapp.lostandfound.model.entity.UserDO;
import com.usst.weapp.lostandfound.service.UserService;
import com.usst.weapp.lostandfound.service.extention.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
//    @Autowired
//    private UserDao userDao;
//
//
//    @Override
//    public List<UserDO> listByMap(Map<String, Object> queryConditions) {
//        return userDao.listByMap(queryConditions);
//    }
//
//    @Override
//    public UserDO insertUser(UserDO user) {
//        return userDao.insertUser(user);
//    }
//
//    @Override
//    public long updateByMongoDBId(String mongoDBId, Map<String, Object> updateConditions) {
//        return userDao.updateByMongoDBId(mongoDBId, updateConditions);
//    }
}

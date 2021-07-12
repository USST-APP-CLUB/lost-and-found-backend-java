package com.usst.weapp.lostandfound.dao.impl;

import com.usst.weapp.lostandfound.dao.UserDao;
import com.usst.weapp.lostandfound.model.entity.UserDO;
import com.usst.weapp.lostandfound.utils.MongoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Author Sunforge
 * @Date 2021-07-12 10:39
 * @Version V1.0.0
 * @Description
 */
@Repository
public class UserDaoImpl implements UserDao {

//    @Autowired
//    private MongoUtil mongoUtil;
//
//
//    @Override
//    public List<UserDO> listByMap(Map<String, Object> queryConditions) {
////        System.out.println();
//        return mongoUtil.listByMap(queryConditions, UserDO.class, "user");
//    }
//
//    @Override
//    public UserDO insertUser(UserDO user) {
//        return mongoUtil.save(user, "user");
//    }
//
//    @Override
//    public long updateByMongoDBId(String mongoDBId, Map<String, Object> updateConditions) {
//        return mongoUtil.updateById(mongoDBId, updateConditions, "user");
//    }

//    public UserDO getUserByUserId()
}

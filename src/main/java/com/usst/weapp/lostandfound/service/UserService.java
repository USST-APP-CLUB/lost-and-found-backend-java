package com.usst.weapp.lostandfound.service;

import com.usst.weapp.lostandfound.model.entity.UserDO;
import com.usst.weapp.lostandfound.service.extention.BaseService;

import java.util.List;
import java.util.Map;


public interface UserService extends BaseService<UserDO> {


    UserDO getUserByWelinkId(String welinkId);

}

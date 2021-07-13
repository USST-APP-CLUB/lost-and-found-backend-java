package com.usst.weapp.lostandfound.utils;

import com.usst.weapp.lostandfound.model.entity.UserDO;
import com.usst.weapp.lostandfound.service.UserService;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Sunforge
 * @Date 2021-07-13 11:04
 * @Version V1.0.0
 * @Description
 */
@Async
@Component
public class AsyncUtil {

    @Autowired
    UserService userService;

    @Autowired
    WelinkUtil welinkUtil;

    @Autowired
    TimeUtil timeUtil;

    @Async
    public void refreshUser(UserDO userFromDB, String userId) throws IOException, ParseException {
        // 从welink查用户
        UserDO userFromWelink = welinkUtil.getUserByUserId(userId);

        // 解析时间
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime welinkLastUpdate = timeUtil.convertStringToTime(userFromWelink.getWelinkLastUpdatedTime());
        LocalDateTime dbLastUpdate = timeUtil.convertStringToTime(userFromDB.getWelinkLastUpdatedTime());

        // 如果更新时间相等，并且权限相等，则无需更新。
        if (welinkLastUpdate.equals(dbLastUpdate)
                && userFromWelink.getAuthorityStr().equals(userFromDB.getAuthorityStr()))
            return;
        // 如果有一个不相等，则需要更新
        Map<String, Object> updateConditions = new HashMap<>();
        updateConditions.put("departments", userFromWelink.getDepartments());
        updateConditions.put("welinkLastUpdatedTime", userFromWelink.getWelinkLastUpdatedTime());
        updateConditions.put("authorityStr", userFromWelink.getAuthorityStr());

        Map<String, Object> queryConditions = new HashMap<>();
        queryConditions.put("userWelinkId", userFromWelink.getUserWelinkId());

        boolean modifiedCount = userService.update(updateConditions, queryConditions, "user");
        System.out.println(modifiedCount);
    }
}

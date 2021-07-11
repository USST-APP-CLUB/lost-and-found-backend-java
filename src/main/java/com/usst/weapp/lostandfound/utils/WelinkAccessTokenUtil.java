package com.usst.weapp.lostandfound.utils;


import com.usst.weapp.lostandfound.constants.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Sunforge
 * @Date 2021-07-11 19:10
 * @Version V1.0.0
 * @Description
 */

@Component
public class WelinkAccessTokenUtil {

    @Autowired
    private HttpClientUtil httpClientUtil;

//    private WelinkAccessTokenUtil(){}

    public String getAccessToken() throws IOException {
        return getAccessTokenFromWelink();
    }

    // 从缓存获取
    private String getAccessTokenFromStorage(){
        return null;
    }

    // 从welink获取
    private String getAccessTokenFromWelink() throws IOException {
        Map<String ,String> body = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        body.put("client_id", Constant.WELINK_CLIENT_ID);
        body.put("client_secret", Constant.WELINK_CLIENT_SECRET);
        String tokenStr = httpClientUtil.postJsonToWelink(Constant.WELINK_GET_ACCESS_TOKEN_URL, body, headers);
        // 提取access_token
        return JSONUtil.getStringJsonValue(tokenStr, "access_token");
    }
}

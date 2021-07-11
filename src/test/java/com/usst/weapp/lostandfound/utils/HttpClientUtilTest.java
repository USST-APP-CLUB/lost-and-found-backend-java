package com.usst.weapp.lostandfound.utils;

import com.usst.weapp.lostandfound.constants.Constant;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class HttpClientUtilTest {
    @Autowired
    private HttpClientUtil httpClientUtil;
    @Test
    void postJsonToWelink() throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("client_id", Constant.WELINK_CLIENT_ID);
        map.put("client_secret", Constant.WELINK_CLIENT_SECRET);
        String resultStr = httpClientUtil.postJsonToWelink(Constant.WELINK_GET_ACCESS_TOKEN_URL, map, null);
        System.out.println(resultStr);
    }
}
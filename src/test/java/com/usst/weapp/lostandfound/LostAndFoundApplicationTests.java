package com.usst.weapp.lostandfound;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.usst.weapp.lostandfound.utils.HttpClientUtil;
import org.apache.http.client.utils.HttpClientUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class LostAndFoundApplicationTests {

    @Autowired
    HttpClientUtil httpClientUtil;

    @Test
    void contextLoads() throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("client_id", "20210705093731544077254");
        map.put("client_secret", "9f24b5bd-6451-48fc-9403-86e36bd8f34c");

        String result = httpClientUtil.postJsonToWelink("https://open.welink.huaweicloud.com/api/auth/v2/tickets", map, null);
        System.out.println(result);
    }

}

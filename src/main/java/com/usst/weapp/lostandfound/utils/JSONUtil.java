package com.usst.weapp.lostandfound.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Sunforge
 * @Date 2021-07-10 23:45
 * @Version V1.0.0
 * @Description
 */
public class JSONUtil {
    private JSONUtil(){}
    public static String getStringValue(String jsonStr, String key) throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree(jsonStr).get(key);
        return jsonNode.asText();
    }

    public static Integer getIntNumberValue(String jsonStr, String key) throws JsonProcessingException {
        JsonNode jsonNode = new ObjectMapper().readTree(jsonStr).get(key);
        assert jsonNode.isIntegralNumber();
//        assert jsonNode.isIntegralNumber() : "不可以用 .asInt() 方法解析非整型值";
        return jsonNode.asInt();
    }

    public static Boolean getBooleanValue(String jsonStr, String key) throws JsonProcessingException {
        JsonNode jsonNode = new ObjectMapper().readTree(jsonStr).get(key);
        assert jsonNode.isBoolean();
        return jsonNode.asBoolean();
    }

    public static String getStringValue(JsonNode jsonNode, String key) {
        return jsonNode.get(key).asText();
    }



    public static String convertMapToJsonString(Map<String, String> body) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        for (Map.Entry<String, String> entry : body.entrySet()){
            rootNode.put(entry.getKey(), entry.getValue());
        }
        return objectMapper.writeValueAsString(rootNode);
    }

    public static String objToStr(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }
}

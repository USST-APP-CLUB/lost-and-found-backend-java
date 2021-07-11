package com.usst.weapp.lostandfound.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    public static String getStringJsonValue(String jsonStr, String key) throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser  = factory.createParser(jsonStr);
        String result = "";
        while(!parser.isClosed()){
            JsonToken jsonToken = parser.nextToken();
            if(JsonToken.FIELD_NAME.equals(jsonToken)){
                String fieldName = parser.getCurrentName();
                jsonToken = parser.nextToken();
                if(key.equals(fieldName)){
                    result = parser.getValueAsString();
                }
            }
        }
        return result;
    }
    public static Map<String, String> getStringJsonValueMap(String jsonStr, String[] keys) throws IOException {
        Map<String, String> map = new HashMap<>();

        return map;
    }

    public static String convertMapToJsonString(Map<String, String> body) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        for (Map.Entry<String, String> entry : body.entrySet()){
            rootNode.put(entry.getKey(), entry.getValue());
        }
        return objectMapper.writeValueAsString(rootNode);
    }
}

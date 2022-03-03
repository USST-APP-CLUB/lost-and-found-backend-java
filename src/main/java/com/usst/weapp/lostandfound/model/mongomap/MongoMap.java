package com.usst.weapp.lostandfound.model.mongomap;

import lombok.Data;

/**
 * @Author Sunforge
 * @Date 2021-07-15 14:23
 * @Version V1.0.0
 * @Description
 */
@Data
public class MongoMap {
    private String key;
    private Object value;

    public MongoMap(String key, Object value) {
        this.key = key;
        this.value = value;
    }
}

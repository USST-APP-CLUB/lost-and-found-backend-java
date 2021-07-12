package com.usst.weapp.lostandfound.utils;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.internal.bulk.UpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author Sunforge
 * @Date 2021-07-12 14:47
 * @Version V1.0.0
 * @Description
 */
@Component
public class MongoUtil {
    @Autowired
    private MongoTemplate mongoTemplate;

    public  <T> T save(T entity, String collectionName) {
        mongoTemplate.save(entity, collectionName);
        return entity;
    }

    public <T> T getByMongoId(String id, String collectionName, Class<T> tClass){
        T result;
        Criteria criteria = Criteria.where("_id").is(id);
        Query query = Query.query(criteria);
        result = mongoTemplate.findOne(query, tClass ,collectionName);
        return result;
    }


    public <T> List<T> listByMap(Map<String, Object> queryConditions, Class<?> tClass, String collectionName) {
        Query query = new Query();
        for (Map.Entry<String, Object> entry : queryConditions.entrySet()) {
            query.addCriteria(Criteria.where(entry.getKey()).is(entry.getValue()));
        }
        return (List<T>) mongoTemplate.find(query,tClass,collectionName);
    }

    public long updateById(String mongoId, Map<String, Object> updateConditions, String collectionName) {
        // 根据传入的id构建查询条件
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(mongoId));
        // 根据传入的map 构建更新条件
        Update update = new Update();
        for(Map.Entry<String, Object> entry : updateConditions.entrySet()) {
            update.set(entry.getKey(), entry.getValue());
        }

        UpdateResult result = mongoTemplate.upsert(query, update, collectionName);
        return result.getModifiedCount();
    }
}

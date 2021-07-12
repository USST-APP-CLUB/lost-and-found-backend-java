package com.usst.weapp.lostandfound.service.extention;

import com.usst.weapp.lostandfound.constants.enums.MongoMethod;
import com.usst.weapp.lostandfound.model.vo.MongoPage;
import com.usst.weapp.lostandfound.model.vo.MongoPageByMapVO;
import com.usst.weapp.lostandfound.model.vo.MyPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @Author Sunforge
 * @Date 2021-07-13 0:00
 * @Version V1.0.0
 * @Description
 */
public class BaseServiceImpl<T> implements BaseService<T> {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public boolean save(Object entity) {
        try{
            mongoTemplate.save(entity);
            mongoLogByConsole(MongoMethod.INSERT,entity.toString());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Object save(Object entity, String collectionName) {
        try{
            mongoTemplate.save(entity,collectionName);
            mongoLogByConsole(MongoMethod.INSERT,entity.toString());
            return entity;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean saveList(List<T> list) {
        try{
            mongoTemplate.insertAll(list);
            mongoLogByConsole(MongoMethod.INSERT,list.toString());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean saveList(List<T> list, String collectionName) {
        try{
            mongoTemplate.insert(list,collectionName);
            mongoLogByConsole(MongoMethod.INSERT,list.toString());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeById(Object entity) {
        try{
            mongoTemplate.remove(entity);
            mongoLogByConsole(MongoMethod.DELETE,entity.toString());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeById(Object entity, String collectionName) {
        try{
            mongoTemplate.remove(entity,collectionName);
            mongoLogByConsole(MongoMethod.DELETE,collectionName+":"+entity.toString());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateById(String[] keys, Object[] values, Serializable id, Class<?> clazz) {
        Criteria criteria= Criteria.where("_id").is(id);
        Query query=new Query(criteria);
        Update update=new Update();
        for (int i = 0; i < keys.length; i++) {
            update.set(keys[i],values[i]);
        }
        try{
            mongoTemplate.updateFirst(query,update,clazz);
            mongoLogByConsole(MongoMethod.UPDATE,update.toString());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateById(String[] keys,Object[] values,Serializable id,String collectionName) {
        Criteria criteria=Criteria.where("_id").is(id);
        Query query=new Query(criteria);
        Update update=new Update();
        for (int i = 0; i < keys.length; i++) {
            update.set(keys[i],values[i]);
        }
        try{
            mongoTemplate.updateFirst(query,update,collectionName);
            mongoLogByConsole(MongoMethod.UPDATE,update.toString());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateById(String[] keys,Object[] values,Serializable id,Class<?> clazz,String collectionName) {
        Criteria criteria=Criteria.where("_id").is(id);
        Query query=new Query(criteria);
        Update update=new Update();
        for (int i = 0; i < keys.length; i++) {
            update.set(keys[i],values[i]);
        }
        try{
            mongoTemplate.updateFirst(query,update,clazz,collectionName);
            mongoLogByConsole(MongoMethod.UPDATE,update.toString());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateByWelinkId(String[] keys, Object[] values, String welinkId, Class<?> clazz, String collectionName) {
        Criteria criteria=Criteria.where("userWelinkId").is(welinkId);
        Query query=new Query(criteria);
        Update update=new Update();
        for (int i = 0; i < keys.length; i++) {
            update.set(keys[i],values[i]);
        }
        try{
            mongoTemplate.updateFirst(query,update,clazz,collectionName);
            mongoLogByConsole(MongoMethod.UPDATE,update.toString());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Object findByMongoId(Serializable id,Class<?> clazz) {
        Object result;
        try {
            Criteria criteria = Criteria.where("_id").is(id);
            Query query = Query.query(criteria);
            result = mongoTemplate.findOne(query, clazz);
            mongoLogByConsole(MongoMethod.SELECT,result.toString());
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Object findByMongoId(Serializable id, Class<?> clazz,String collectionName) {
        Object result;
        try {
            Criteria criteria = Criteria.where("_id").is(id);
            Query query = Query.query(criteria);
            result = mongoTemplate.findOne(query, clazz,collectionName);
            mongoLogByConsole(MongoMethod.SELECT,result.toString());
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Object findByMongoId(Serializable id,String[] fields,Class<?> clazz) {
        Object result;
        try {
            Criteria criteria = Criteria.where("_id").is(id);
            Query query = Query.query(criteria);
            Field field=query.fields();
            field.include(fields);
            result = mongoTemplate.findOne(query, clazz);
            mongoLogByConsole(MongoMethod.SELECT,result.toString());
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Object findByMongoId(Serializable id,String[] fields,Class<?> clazz,String collectionName) {
        Object result;
        try {
            Criteria criteria = Criteria.where("_id").is(id);
            Query query = Query.query(criteria);
            Field field=query.fields();
            field.include(fields);
            result = mongoTemplate.findOne(query, clazz,collectionName);
            mongoLogByConsole(MongoMethod.SELECT,result.toString());
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<T> findByMap(String[] keys,Object[] values,Class<?> clazz) {
        Criteria criteria = null;
        List<T> list;
        try {
            for (int i = 0; i < keys.length; i++) {
                if (i == 0) {
                    criteria = Criteria.where(keys[i]).is(values[i]);
                } else {
                    criteria.and(keys[i]).is(values[i]);
                }
            }
            Query query = Query.query(criteria);
            list= (List<T>) mongoTemplate.find(query,clazz);
            mongoLogByConsole(MongoMethod.SELECT,list.toString());
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<T> findByMap(String[] keys,Object[] values,Class<?> clazz,String collectionName) {
        Criteria criteria = null;
        List<T> list;
        try {
            for (int i = 0; i < keys.length; i++) {
                if (i == 0) {
                    criteria = Criteria.where(keys[i]).is(values[i]);
                } else {
                    criteria.and(keys[i]).is(values[i]);
                }
            }
            Query query = Query.query(criteria);
            list= (List<T>) mongoTemplate.find(query,clazz,collectionName);
            mongoLogByConsole(MongoMethod.SELECT,list.toString());
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<T> findByMap(String[] keys,Object[] values,String[] fields,Class<?> clazz) {
        Criteria criteria = null;
        List<T> list;
        try {
            for (int i = 0; i < keys.length; i++) {
                if (i == 0) {
                    criteria = Criteria.where(keys[i]).is(values[i]);
                } else {
                    criteria.and(keys[i]).is(values[i]);
                }
            }
            Query query = Query.query(criteria);
            Field field=query.fields();
            field.include(fields);
            list= (List<T>) mongoTemplate.find(query,clazz);
            mongoLogByConsole(MongoMethod.SELECT,list.toString());
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<T> findByMap(String[] keys,Object[] values,String[] fields,Class<?> clazz,String collectionName) {
        Criteria criteria = null;
        List<T> list;
        try {
            for (int i = 0; i < keys.length; i++) {
                if (i == 0) {
                    criteria = Criteria.where(keys[i]).is(values[i]);
                } else {
                    criteria.and(keys[i]).is(values[i]);
                }
            }
            Query query = Query.query(criteria);
            Field field=query.fields();
            field.include(fields);
            list= (List<T>) mongoTemplate.find(query,clazz,collectionName);
            mongoLogByConsole(MongoMethod.SELECT,list.toString());
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<T> findAll(Class<?> clazz) {
        List<T> list;
        try{
            list= (List<T>) mongoTemplate.findAll(clazz);
            mongoLogByConsole(MongoMethod.SELECT,list.toString());
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<T> findAll(Class<?> clazz,String collectionName) {
        List<T> list;
        try{
            list= (List<T>) mongoTemplate.find(new Query(),clazz,collectionName);
            mongoLogByConsole(MongoMethod.SELECT,list.toString());
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<T> findAll(String[] fields,Class<?> clazz) {
        List<T> list;
        try{
            Query query = new Query(new Criteria());
            Field field=query.fields();
            field.include(fields);
            list= (List<T>) mongoTemplate.find(query,clazz);
            mongoLogByConsole(MongoMethod.SELECT,list.toString());
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<T> findAll(String[] fields,Class<?> clazz,String collectionName) {
        List<T> list;
        try{
            Query query = new Query(new Criteria());
            Field field=query.fields();
            field.include(fields);
            list= (List<T>) mongoTemplate.find(query,clazz,collectionName);
            mongoLogByConsole(MongoMethod.SELECT,list.toString());
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<T> page(MyPage myPage, Class<?> clazz) {
        Integer pageSize=myPage.getPageSize()==null?10:myPage.getPageSize();
        Integer pageNum=myPage.getPageNum()==null?1:myPage.getPageNum();
        List<T> list;
        try{
            Query query=new Query(new Criteria());
            query.limit(pageSize);
            query.skip((pageNum - 1) * pageSize);
            list= (List<T>) mongoTemplate.find(query,clazz);
            mongoLogByConsole(MongoMethod.PAGE,list.toString());
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<T> page(MyPage myPage, Class<?> clazz, String collectionName) {
        Integer pageSize=myPage.getPageSize()==null?10:myPage.getPageSize();
        Integer pageNum=myPage.getPageNum()==null?1:myPage.getPageNum();
        List<T> list;
        try{
            Query query=new Query(new Criteria());
            query.limit(pageSize);
            query.skip((pageNum - 1) * pageSize);
            list= (List<T>) mongoTemplate.find(query,clazz,collectionName);
            mongoLogByConsole(MongoMethod.PAGE,list.toString());
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<T> page(MyPage myPage,String[] fields, Class<?> clazz) {
        Integer pageSize=myPage.getPageSize()==null?10:myPage.getPageSize();
        Integer pageNum=myPage.getPageNum()==null?1:myPage.getPageNum();
        List<T> list;
        try{
            Query query=new Query(new Criteria());
            query.limit(pageSize);
            query.skip((pageNum - 1) * pageSize);
            Field field=query.fields();
            field.include(fields);
            list= (List<T>) mongoTemplate.find(query,clazz);
            mongoLogByConsole(MongoMethod.PAGE,list.toString());
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<T> page(MyPage myPage,String[] fields, Class<?> clazz,String collectionName) {
        Integer pageSize=myPage.getPageSize()==null?10:myPage.getPageSize();
        Integer pageNum=myPage.getPageNum()==null?1:myPage.getPageNum();
        List<T> list;
        try{
            Query query=new Query(new Criteria());
            query.limit(pageSize);
            query.skip((pageNum - 1) * pageSize);
            Field field=query.fields();
            field.include(fields);
            list= (List<T>) mongoTemplate.find(query,clazz,collectionName);
            mongoLogByConsole(MongoMethod.PAGE,list.toString());
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public MongoPage page(MongoPage mongoPage, Class<?> clazz) {
        Integer pageSize=mongoPage.getPageSize()==null?10:mongoPage.getPageSize();
        Integer pageNum=mongoPage.getPageNum()==null?1:mongoPage.getPageNum();
        long total;
        List<T> list;
        try{
            Query query=new Query(new Criteria());
            //返回总记录数
            total=mongoTemplate.count(query,clazz);
            query.limit(pageSize);
            query.skip((pageNum - 1) * pageSize);
            list= (List<T>) mongoTemplate.find(query,clazz);
            mongoPage.setMongoPage(pageNum,pageSize,total,getTotalSum(total,pageSize),list);
            mongoLogByConsole(MongoMethod.MONGOPage,mongoPage.toString());
            return mongoPage;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public MongoPage page(MongoPage mongoPage, Class<?> clazz,String collectionName) {
        Integer pageSize=mongoPage.getPageSize()==null?10:mongoPage.getPageSize();
        Integer pageNum=mongoPage.getPageNum()==null?1:mongoPage.getPageNum();
        long total;
        List<T> list;
        try{
            Query query=new Query(new Criteria());
            //返回总记录数
            total=mongoTemplate.count(query,clazz);
            query.limit(pageSize);
            query.skip((pageNum - 1) * pageSize);
            list= (List<T>) mongoTemplate.find(query,clazz,collectionName);
            mongoPage.setMongoPage(pageNum,pageSize,total,getTotalSum(total,pageSize),list);
            mongoLogByConsole(MongoMethod.MONGOPage,mongoPage.toString());
            return mongoPage;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public MongoPage page(MongoPage mongoPage,String[] fields, Class<?> clazz) {
        Integer pageSize=mongoPage.getPageSize()==null?10:mongoPage.getPageSize();
        Integer pageNum=mongoPage.getPageNum()==null?1:mongoPage.getPageNum();
        long total;
        List<T> list;
        try{
            Query query=new Query(new Criteria());
            //返回总记录数
            total=mongoTemplate.count(query,clazz);
            //默认值为5，
            pageSize = pageSize < 0 ? 5 : pageSize;
            query.limit(pageSize);
            query.skip((pageNum - 1) * pageSize);
            Field field=query.fields();
            field.include(fields);
            list= (List<T>) mongoTemplate.find(query,clazz);
            mongoPage.setMongoPage(pageNum,pageSize,total,getTotalSum(total,pageSize),list);
            mongoLogByConsole(MongoMethod.MONGOPage,mongoPage.toString());
            return mongoPage;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public MongoPage page(MongoPage mongoPage,String[] fields, Class<?> clazz,String collectionName) {
        Integer pageSize=mongoPage.getPageSize()==null?10:mongoPage.getPageSize();
        Integer pageNum=mongoPage.getPageNum()==null?1:mongoPage.getPageNum();
        long total;
        List<T> list;
        try{
            Query query=new Query(new Criteria());
            //返回总记录数
            total=mongoTemplate.count(query,clazz);
            query.limit(pageSize);
            query.skip((pageNum - 1) * pageSize);
            Field field=query.fields();
            field.include(fields);
            list= (List<T>) mongoTemplate.find(query,clazz,collectionName);
            mongoPage.setMongoPage(pageNum,pageSize,total,getTotalSum(total,pageSize),list);
            mongoLogByConsole(MongoMethod.MONGOPage,mongoPage.toString());
            return mongoPage;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public MongoPageByMapVO pageByMap(MongoPageByMapVO mongoPageByMapVo, Class<?> clazz, String collectionName){
        Integer pageSize=mongoPageByMapVo.getPageSize()==null?10:mongoPageByMapVo.getPageSize();
        Integer pageNum=mongoPageByMapVo.getPageNum()==null?1:mongoPageByMapVo.getPageNum();
        long total;
        Criteria criteria = null;
        List<T> list;
        String[] keys=mongoPageByMapVo.getKeys();
        Object[] values=mongoPageByMapVo.getValues();
        try {
            for (int i = 0; i < keys.length; i++) {
                if (i == 0) {
                    criteria = Criteria.where(keys[i]).is(values[i]);
                } else {
                    criteria.and(keys[i]).is(values[i]);
                }
            }
            Query query = Query.query(criteria);
            //返回总记录数
            total=mongoTemplate.count(query,clazz);
            query.limit(pageSize);
            query.skip((pageNum - 1) * pageSize);
            list= (List<T>) mongoTemplate.find(query,clazz,collectionName);
            mongoPageByMapVo.setMongoPage(pageNum,pageSize,total,getTotalSum(total,pageSize),list);
            mongoLogByConsole(MongoMethod.MONGOPage,mongoPageByMapVo.toString());
            return mongoPageByMapVo;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<T> fuzzyPage(MyPage myPage,Class<?> clazz,String inputStr,String[] keys){
        Integer pageSize=myPage.getPageSize()==null?10:myPage.getPageSize();
        Integer pageNum=myPage.getPageNum()==null?1:myPage.getPageNum();
        List<T> list;
        try{
            if(judgeLength(keys.length)){
                return null;
            }
            Query query=new Query(getCriteria(inputStr,keys));
            query.limit(pageSize);
            query.skip((pageNum - 1) * pageSize);
            list= (List<T>) mongoTemplate.find(query,clazz);
            mongoLogByConsole(MongoMethod.fuzzyPage,list.toString());
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public List<T> fuzzyPage(MyPage myPage,Class<?> clazz,String collectionName,String inputStr,String[] keys){
        Integer pageSize=myPage.getPageSize()==null?10:myPage.getPageSize();
        Integer pageNum=myPage.getPageNum()==null?1:myPage.getPageNum();
        List<T> list;
        try{
            if(judgeLength(keys.length)){
                return null;
            }
            Query query=new Query(getCriteria(inputStr,keys));
            query.limit(pageSize);
            query.skip((pageNum - 1) * pageSize);
            list= (List<T>) mongoTemplate.find(query,clazz,collectionName);
            mongoLogByConsole(MongoMethod.fuzzyPage,list.toString());
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public List<T> fuzzyPage(MyPage myPage,Class<?> clazz, String inputStr,String[] keys,String[] fields){
        Integer pageSize=myPage.getPageSize()==null?10:myPage.getPageSize();
        Integer pageNum=myPage.getPageNum()==null?1:myPage.getPageNum();
        List<T> list;
        try{
            if(judgeLength(keys.length)){
                return null;
            }
            Query query=new Query(getCriteria(inputStr,keys));
            query.limit(pageSize);
            query.skip((pageNum - 1) * pageSize);
            Field field=query.fields();
            field.include(fields);
            list= (List<T>) mongoTemplate.find(query,clazz);
            mongoLogByConsole(MongoMethod.fuzzyPage,list.toString());
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public List<T> fuzzyPage(MyPage myPage,Class<?> clazz,String collectionName,String inputStr,String[] keys, String[] fields){
        Integer pageSize=myPage.getPageSize()==null?10:myPage.getPageSize();
        Integer pageNum=myPage.getPageNum()==null?1:myPage.getPageNum();
        List<T> list;
        try{
            if(judgeLength(keys.length)){
                return null;
            }
            Query query=new Query(getCriteria(inputStr,keys));
            query.limit(pageSize);
            query.skip((pageNum - 1) * pageSize);
            Field field=query.fields();
            field.include(fields);
            list= (List<T>) mongoTemplate.find(query,clazz,collectionName);
            mongoLogByConsole(MongoMethod.fuzzyPage,list.toString());
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public MongoPage fuzzyPage(MongoPage mongoPage, Class<?> clazz, String inputStr, String[] keys) {
        Integer pageSize=mongoPage.getPageSize()==null?10:mongoPage.getPageSize();
        Integer pageNum=mongoPage.getPageNum()==null?1:mongoPage.getPageNum();
        long total;
        List<T> list;
        try{
            if(judgeLength(keys.length)){
                return null;
            }
            Query query=new Query(getCriteria(inputStr,keys));
            //返回总记录数
            total=mongoTemplate.count(query,clazz);
            query.limit(pageSize);
            query.skip((pageNum - 1) * pageSize);
            list= (List<T>) mongoTemplate.find(query,clazz);
            mongoPage.setMongoPage(pageNum,pageSize,total,getTotalSum(total,pageSize),list);
            mongoLogByConsole(MongoMethod.fuzzyPage,mongoPage.toString());
            return mongoPage;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public MongoPage fuzzyPage(MongoPage mongoPage, Class<?> clazz, String collectionName, String inputStr, String[] keys) {
        Integer pageSize=mongoPage.getPageSize()==null?10:mongoPage.getPageSize();
        Integer pageNum=mongoPage.getPageNum()==null?1:mongoPage.getPageNum();
        long total;
        List<T> list;
        try{
            if(judgeLength(keys.length)){
                return null;
            }
            Query query=new Query(getCriteria(inputStr,keys));
            //返回总记录数
            total=mongoTemplate.count(query,clazz);
            query.limit(pageSize);
            query.skip((pageNum - 1) * pageSize);
            list= (List<T>) mongoTemplate.find(query,clazz,collectionName);
            mongoPage.setMongoPage(pageNum,pageSize,total,getTotalSum(total,pageSize),list);
            mongoLogByConsole(MongoMethod.fuzzyPage,mongoPage.toString());
            return mongoPage;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public MongoPage fuzzyPage(MongoPage mongoPage, Class<?> clazz, String inputStr, String[] keys, String[] fields) {
        Integer pageSize=mongoPage.getPageSize()==null?10:mongoPage.getPageSize();
        Integer pageNum=mongoPage.getPageNum()==null?1:mongoPage.getPageNum();
        long total;
        List<T> list;
        try{
            if(judgeLength(keys.length)){
                return null;
            }
            Query query=new Query(getCriteria(inputStr,keys));
            //返回总记录数
            total=mongoTemplate.count(query,clazz);
            query.limit(pageSize);
            query.skip((pageNum - 1) * pageSize);
            Field field=query.fields();
            field.include(fields);
            list= (List<T>) mongoTemplate.find(query,clazz);
            mongoPage.setMongoPage(pageNum,pageSize,total,getTotalSum(total,pageSize),list);
            mongoLogByConsole(MongoMethod.fuzzyPage,mongoPage.toString());
            return mongoPage;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public MongoPage fuzzyPage(MongoPage mongoPage, Class<?> clazz, String collectionName, String inputStr, String[] keys, String[] fields) {
        Integer pageSize=mongoPage.getPageSize()==null?10:mongoPage.getPageSize();
        Integer pageNum=mongoPage.getPageNum()==null?1:mongoPage.getPageNum();
        long total;
        List<T> list;
        try{
            if(judgeLength(keys.length)){
                return null;
            }
            Query query=new Query(getCriteria(inputStr,keys));
            //返回总记录数
            total=mongoTemplate.count(query,clazz);
            query.limit(pageSize);
            query.skip((pageNum - 1) * pageSize);
            Field field=query.fields();
            field.include(fields);
            list= (List<T>) mongoTemplate.find(query,clazz,collectionName);
            mongoPage.setMongoPage(pageNum,pageSize,total,getTotalSum(total,pageSize),list);
            mongoLogByConsole(MongoMethod.fuzzyPage,mongoPage.toString());
            return mongoPage;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 用于模糊查询忽略大小写
     * @param str
     * @return
     */
    private Pattern getPattern(String str) {
        Pattern pattern = Pattern.compile("^.*" + str + ".*$", Pattern.CASE_INSENSITIVE);
        return pattern;
    }

    /**
     * 用于模糊查询
     * @param keys 匹配的字段数组
     * @param inputStr 输入的字符
     * @return
     */
    private Criteria getCriteria(String inputStr,String[] keys){
        Pattern pattern=getPattern(inputStr);
        Criteria criteria=null;
        switch (keys.length){
            case 1:criteria = Criteria.where("").orOperator(
                    Criteria.where(keys[0]).regex(pattern));
                break;
            case 2:criteria = Criteria.where("").orOperator(
                    Criteria.where(keys[0]).regex(pattern),
                    Criteria.where(keys[1]).regex(pattern)
            );
                break;
            case 3:criteria = Criteria.where("").orOperator(
                    Criteria.where(keys[0]).regex(pattern),
                    Criteria.where(keys[1]).regex(pattern),
                    Criteria.where(keys[2]).regex(pattern)
            );
                break;
            case 4:criteria = Criteria.where("").orOperator(
                    Criteria.where(keys[0]).regex(pattern),
                    Criteria.where(keys[1]).regex(pattern),
                    Criteria.where(keys[2]).regex(pattern),
                    Criteria.where(keys[3]).regex(pattern)
            );
                break;
            case 5:criteria = Criteria.where("").orOperator(
                    Criteria.where(keys[0]).regex(pattern),
                    Criteria.where(keys[1]).regex(pattern),
                    Criteria.where(keys[2]).regex(pattern),
                    Criteria.where(keys[3]).regex(pattern),
                    Criteria.where(keys[4]).regex(pattern)
            );
                break;
            case 6:criteria = Criteria.where("").orOperator(
                    Criteria.where(keys[0]).regex(pattern),
                    Criteria.where(keys[1]).regex(pattern),
                    Criteria.where(keys[2]).regex(pattern),
                    Criteria.where(keys[3]).regex(pattern),
                    Criteria.where(keys[4]).regex(pattern),
                    Criteria.where(keys[5]).regex(pattern)
            );
                break;
            case 7:criteria = Criteria.where("").orOperator(
                    Criteria.where(keys[0]).regex(pattern),
                    Criteria.where(keys[1]).regex(pattern),
                    Criteria.where(keys[2]).regex(pattern),
                    Criteria.where(keys[3]).regex(pattern),
                    Criteria.where(keys[4]).regex(pattern),
                    Criteria.where(keys[5]).regex(pattern),
                    Criteria.where(keys[6]).regex(pattern)
            );
                break;
            case 8:criteria = Criteria.where("").orOperator(
                    Criteria.where(keys[0]).regex(pattern),
                    Criteria.where(keys[1]).regex(pattern),
                    Criteria.where(keys[2]).regex(pattern),
                    Criteria.where(keys[3]).regex(pattern),
                    Criteria.where(keys[4]).regex(pattern),
                    Criteria.where(keys[5]).regex(pattern),
                    Criteria.where(keys[6]).regex(pattern),
                    Criteria.where(keys[7]).regex(pattern)
            );
                break;
            case 9:criteria = Criteria.where("").orOperator(
                    Criteria.where(keys[0]).regex(pattern),
                    Criteria.where(keys[1]).regex(pattern),
                    Criteria.where(keys[2]).regex(pattern),
                    Criteria.where(keys[3]).regex(pattern),
                    Criteria.where(keys[4]).regex(pattern),
                    Criteria.where(keys[5]).regex(pattern),
                    Criteria.where(keys[6]).regex(pattern),
                    Criteria.where(keys[7]).regex(pattern),
                    Criteria.where(keys[8]).regex(pattern)
            );
                break;
            case 10:criteria = Criteria.where("").orOperator(
                    Criteria.where(keys[0]).regex(pattern),
                    Criteria.where(keys[1]).regex(pattern),
                    Criteria.where(keys[2]).regex(pattern),
                    Criteria.where(keys[3]).regex(pattern),
                    Criteria.where(keys[4]).regex(pattern),
                    Criteria.where(keys[5]).regex(pattern),
                    Criteria.where(keys[6]).regex(pattern),
                    Criteria.where(keys[7]).regex(pattern),
                    Criteria.where(keys[8]).regex(pattern),
                    Criteria.where(keys[9]).regex(pattern)
            );
                break;
        }
        return criteria;
    }

    /**
     * 用于判断keys长度
     */
    private boolean judgeLength(int length){
        if(length>10||length==0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 获取总页数
     * @param total
     * @param pageSize
     * @return
     */
    private long getTotalSum(long total,Integer pageSize){
        long totalSum=0;
        if(total%pageSize==0){//能够整除
            totalSum=total/pageSize;
        }else{
            totalSum=total/pageSize+1;
        }
        return totalSum;
    }
    /**
     * Mongodb控制台日志
     * @param method
     * @param toString
     */
    private void mongoLogByConsole(String method,String toString){
        String outPut="MongoDB";
        switch (method){
            case MongoMethod.INSERT:
                outPut=outPut+"  insert:"+toString;
                break;
            case MongoMethod.DELETE:
                outPut=outPut+"  delete:"+toString;
                break;
            case MongoMethod.UPDATE:
                outPut=outPut+"  update:"+toString;
                break;
            case MongoMethod.SELECT:
                outPut=outPut+"  select:"+toString;
                break;
            case MongoMethod.PAGE:
                outPut=outPut+"  page:"+toString;
                break;
            case MongoMethod.MONGOPage:
                outPut=outPut+"  mongoPage:"+toString;
                break;
            case MongoMethod.fuzzyPage:
                outPut=outPut+"  fuzzyPage:"+toString;
                break;
        }
        System.out.println(outPut);
    }
}

package com.usst.weapp.lostandfound.service.extention;

import com.usst.weapp.lostandfound.model.vo.MongoPage;
import com.usst.weapp.lostandfound.model.vo.MongoPageByMapVO;
import com.usst.weapp.lostandfound.model.vo.MyPage;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseService<T> {


        /**
         * 增
         */
        boolean save(Object entity);
        Object save(Object entity,String collectionName);
        boolean saveList(List<T> list);
        boolean saveList(List<T> list,String collectionName);

        /**
         * 删
         */
        boolean removeById(Object entity);
        boolean removeById(Object entity,String collectionName);
        /**
         * 改,都是通过id
         * @param keys 要修改的键名
         * @param values 要修改的键值
         * 相当于mysql中  update key1,key2,key3 set value1,value2,value3 where id=#{id} from collectionName
         */
        boolean updateById(String[] keys, Object[] values, Serializable id, Class<?> clazz);
        boolean updateById(String[] keys,Object[] values,Serializable id,String collectionName);
        boolean updateById(String[] keys,Object[] values,Serializable id,Class<?> clazz,String collectionName);
        boolean update(Map<String, Object> updateConditions, Map<String, Object> queryConditions, String collectionName);
        /**
         * 查
         * @finById 就是根据id查询单条记录
         * @findByMap 相当于条件查询,查询出符合条件的所有内容
         * @findAll 查询表中所有记录
         * @fields 表示需要查询的字段---参考mysql中 select key1,key2,key3 from collectionName
         */
        Object findByMongoId(Serializable id,Class<?> clazz);
        Object findByMongoId(Serializable id,Class<?> clazz,String collectionName);
        Object findByMongoId(Serializable id,String[] fields,Class<?> clazz);
        Object findByMongoId(Serializable id,String[] fields,Class<?> clazz,String collectionName);
        List<T> findByMap(String[] keys,Object[] values,Class<?> clazz);
        List<T> findByMap(String[] keys,Object[] values,Class<?> clazz,String collectionName);
        List<T> find(Map<String, Object> queryConditions, Class<T> clazz, String collectionName);
        List<T> findByMap(String[] keys,Object[] values,String[] fields,Class<?> clazz);
        List<T> findByMap(String[] keys,Object[] values,String[] fields,Class<?> clazz,String collectionName);
        List<T> findAll(Class<?> clazz);
        List<T> findAll(Class<?> clazz,String collectionName);
        List<T> findAll(String[] fields,Class<?> clazz);
        List<T> findAll(String[] fields,Class<?> clazz,String collectionName);
        /**
         * 分页---无总记录数，只查一次
         */
        List<T> page(MyPage myPage, Class<?> clazz);
        List<T> page(MyPage myPage, Class<T> clazz,String collectionName);
        List<T> page(MyPage myPage, String[] fields,Class<?> clazz);
        List<T> page(MyPage myPage, String[] fields,Class<?> clazz,String collectionName);
        /**
         * 分页--有总记录数，查两次
         */
        MongoPage page(MongoPage mongoPage, Class<?> clazz);
        MongoPage page(MongoPage mongoPage, Class<?> clazz,String collectionName);
        MongoPage page(MongoPage mongoPage,String[] fields,Class<?> clazz);
        MongoPage page(MongoPage mongoPage,String[] fields, Class<?> clazz,String collectionName);

        MongoPage<T> getFirstPage(MyPage myPage, Class<T> clazz,String collectionName);

        MongoPageByMapVO pageByMap(MongoPageByMapVO mongoPageByMapVo, Class<?> clazz, String collectionName);

        /**
         * 模糊查询---分页
         */
        List<T> fuzzyPage(MyPage myPage,Class<?> clazz,String inputStr,String[] keys);
        List<T> fuzzyPage(MyPage myPage,Class<?> clazz,String collectionName,String inputStr,String[] keys);
        List<T> fuzzyPage(MyPage myPage,Class<?> clazz, String inputStr,String[] keys,String[] fields);
        List<T> fuzzyPage(MyPage myPage,Class<?> clazz,String collectionName,String inputStr,String[] keys, String[] fields);

        /**
         * 模糊查询---分页--有总记录数，查两次
         */
        MongoPage fuzzyPage(MongoPage mongoPage,Class<?> clazz,String inputStr,String[] keys);
        MongoPage fuzzyPage(MongoPage mongoPage, Class<?> clazz,String collectionName,String inputStr,String[] keys);
        MongoPage fuzzyPage(MongoPage mongoPage,Class<?> clazz,String inputStr,String[] keys,String[] fields);
        MongoPage fuzzyPage(MongoPage mongoPage, Class<?> clazz,String collectionName,String inputStr,String[] keys,String[] fields);
}


package com.usst.weapp.lostandfound.model.vo;

import lombok.Data;

import java.util.Arrays;

/**
 * @Author Sunforge
 * @Date 2021-07-13 0:06
 * @Version V1.0.0
 * @Description
 */
@Data
public class MongoPageByMapVO<T> extends MongoPage{

    private String[] keys;
    private Object[] values;

    @Override
    public String toString() {
        return "MongoPageByMapVo{" +
                "keys=" + Arrays.toString(keys) +
                ", values=" + Arrays.toString(values) +
                ", total=" + total +
                ", totalSum=" + totalSum +
                ", list=" + list +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
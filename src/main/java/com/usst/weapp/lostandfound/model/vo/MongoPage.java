package com.usst.weapp.lostandfound.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

/**
 * @Author Sunforge
 * @Date 2021-07-13 0:04
 * @Version V1.0.0
 * @Description
 */
@Data
@NoArgsConstructor
public class MongoPage<T> extends MyPage{
    protected long total;//总记录数
    protected long totalSum;//总页数
    protected List<T> list;//分页结果
    public MongoPage(Integer pageNum,Integer pageSize,long total,long totalSum,List<T> list){
        this.pageNum = (pageNum == null) ? 1 : pageNum;
        this.pageSize = (pageSize == null) ? 10 : pageSize;
    }

    public MongoPage(Integer pageNum, Integer pageSize){
        this.pageNum = (pageNum == null) ? 1 : pageNum;
        this.pageSize = (pageSize == null) ? 10 : pageSize;
    }

    public void setMongoPage(long total,long totalSum,List<T> list){
        this.total=total;
        this.totalSum=totalSum;
        this.list=list;
    }
    public void setMongoPage(Integer pageNum,Integer pageSize,long total,long totalSum,List<T> list){
        this.pageNum=pageNum;
        this.pageSize=pageSize;
        this.total=total;
        this.totalSum=totalSum;
        this.list=list;
    }

    @Override
    public String toString() {
        return "MongoPage{" +
                "total=" + total +
                ", list=" + list +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
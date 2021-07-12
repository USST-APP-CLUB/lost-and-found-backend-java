package com.usst.weapp.lostandfound.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author Sunforge
 * @Date 2021-07-13 0:03
 * @Version V1.0.0
 * @Description
 */
@Data
@NoArgsConstructor
public class MyPage implements Serializable {
    private static final long serialVersionUID = 1L;
    //当前页码
    protected Integer pageNum;
    //页大小
    protected Integer pageSize;

    public MyPage(Integer pageNum,Integer pageSize){
        this.pageNum=pageNum==null?1:pageNum;
        this.pageSize=pageSize==null?10:pageSize;
    }
}

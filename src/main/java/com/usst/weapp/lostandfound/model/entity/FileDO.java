package com.usst.weapp.lostandfound.model.entity;

import lombok.Data;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

/**
 * @Author Sunforge
 * @Date 2021-07-14 16:26
 * @Version V1.0.0
 * @Description
 */
@Document
@Data
public class FileDO {

    @Id  // 主键
    private String id;
    private String name; // 文件名称
    private String contentType; // 文件类型
    private long size;
    private String uploadDate;
    private String md5;
    private Binary content; // 文件内容
    private String path; // 文件路径

    protected FileDO() {
    }


    public FileDO(String name, String contentType, long size,  Binary content) {
        this.name = name;
        this.contentType = contentType;
        this.size = size;
        this.content = content;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileDO)) return false;
        FileDO FileDO = (FileDO) o;
        return size == FileDO.size &&
                Objects.equals(id, FileDO.id) &&
                Objects.equals(name, FileDO.name) &&
                Objects.equals(contentType, FileDO.contentType) &&
                Objects.equals(uploadDate, FileDO.uploadDate) &&
                Objects.equals(md5, FileDO.md5) &&
                Objects.equals(content, FileDO.content) &&
                Objects.equals(path, FileDO.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, contentType, size, uploadDate, md5, content, path);
    }
}

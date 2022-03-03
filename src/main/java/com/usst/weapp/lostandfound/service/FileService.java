package com.usst.weapp.lostandfound.service;

import com.usst.weapp.lostandfound.model.entity.FileDO;

import java.util.List;
import java.util.Optional;

public interface FileService {
    /**
     * 保存文件
     */
    FileDO saveFile(FileDO file);

    /**
     * 删除文件
     */
    void removeFile(String id);

    /**
     * 根据id获取文件
     */
    Optional<FileDO> getFileById(String id);

    /**
     * 分页查询，按上传时间降序
     * @return
     */
    List<FileDO> listFilesByPage(int pageIndex, int pageSize);
}

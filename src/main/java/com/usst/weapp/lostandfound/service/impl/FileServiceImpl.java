package com.usst.weapp.lostandfound.service.impl;

import com.usst.weapp.lostandfound.dao.FileRepository;
import com.usst.weapp.lostandfound.model.entity.FileDO;
import com.usst.weapp.lostandfound.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Author Sunforge
 * @Date 2021-07-14 16:34
 * @Version V1.0.0
 * @Description
 */
@Service
public class FileServiceImpl  implements FileService {
    @Autowired
    private FileRepository fileRepository;

    @Override
    public FileDO saveFile(FileDO file) {
        return fileRepository.save(file);
    }

    @Override
    public void removeFile(String id) {
        fileRepository.deleteById(id);
    }

    @Override
    public Optional<FileDO> getFileById(String id) {
        return fileRepository.findById(id);
    }

    @Override
    public List<FileDO> listFilesByPage(int pageIndex, int pageSize) {
        Page<FileDO> page = null;
        List<FileDO> list = null;
        Sort sort = Sort.by(Sort.Direction.DESC,"uploadDate");
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
        page = fileRepository.findAll(pageable);
        list = page.getContent();
        return list;
    }
}

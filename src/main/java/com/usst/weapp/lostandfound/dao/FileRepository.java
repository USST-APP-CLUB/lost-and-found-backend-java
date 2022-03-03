package com.usst.weapp.lostandfound.dao;

import com.usst.weapp.lostandfound.model.entity.FileDO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends MongoRepository<FileDO,String> {
}

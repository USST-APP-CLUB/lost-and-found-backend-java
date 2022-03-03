package com.usst.weapp.lostandfound.controller;

import com.usst.weapp.lostandfound.model.common.Response;
import com.usst.weapp.lostandfound.service.FileService;
import com.usst.weapp.lostandfound.utils.FileUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author Sunforge
 * @Date 2021-07-15 12:20
 * @Version V1.0.0
 * @Description
 */
@RestController
@RequestMapping("/file")
public class MyFileController {

    @Autowired
    private FileUtilService FileUtil;

    @PostMapping("/upload")
    public Response upload(MultipartFile file){
        try {
            return Response.success(FileUtil.uploadOne("", file));
        }catch (Exception e){
            return Response.fail("err");
        }
    }
}

package com.usst.weapp.lostandfound.service.impl;

import com.usst.weapp.lostandfound.service.ImgService;
import com.usst.weapp.lostandfound.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.imageio.ImageIO;
import javax.print.DocFlavor;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @Author Sunforge
 * @Date 2021-07-15 14:35
 * @Version V1.0.0
 * @Description
 */
@Service
public class ImgServiceImpl implements ImgService {

    @Autowired
    HttpClientUtil httpClientUtil;

    @Override
    public byte[] getImage(String id) throws IOException {
        String url = "https://api.multiavatar.com/" + id;
        httpClientUtil.downloadImg(url);
//        boolean isSuccess =  ImageIO.write(ImageIO.read(url), formatName, localFile);
//        BufferedImage image = ImageIO.read(new FileInputStream(url));
        FileInputStream fileInputStream = new FileInputStream(url);
        return new byte[fileInputStream.available()];

//        return new byte[0];
    }
}

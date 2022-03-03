package com.usst.weapp.lostandfound.service;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface ImgService {
    byte[] getImage(String id) throws IOException;
}

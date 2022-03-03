package com.usst.weapp.lostandfound.utils;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUtilService {
	String uploadOne(String filePath, MultipartFile voFile);
	List<String> uploadList(String filePath, List<MultipartFile> voFileList);
	String uploadListReturnStr(String filePath, List<MultipartFile> voFileList);
	boolean deleteFile(String fileUrl);
	boolean deleteFileList(List<String> fileUrlList);
}

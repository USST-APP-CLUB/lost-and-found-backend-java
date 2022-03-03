package com.usst.weapp.lostandfound.utils;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileUtil implements FileUtilService{
	//服务器地址
	final String baseUrl="http://localhost:8081";
	//获取编译文件夹下的static路径
	//final String basePath = getClass().getClassLoader().getResource("static").getPath();
    final String basePath="/older/";
	/**
	 * 上传单图片
	 * @param filePath 上传地址上传地址（用类名即可，第一个字母改为小写，如一个类有单图片和多图片，多加个字段名）
	 * 	                例如：/account/img    or    /account/imgList
	 * @param voFile  上传图片
	 * @return
	 */
	@Override
	public String uploadOne(String filePath,MultipartFile voFile){
		File file=new File(basePath+filePath);
		if(!file.exists()){//文件夹不存在
			file.mkdirs();//创建文件夹
		}
		//文件输出流
		FileOutputStream fileOutputStream=null;
		//缓冲输出流
		BufferedOutputStream bufferedOutputStream=null;
		//前端限制好，图片为空不让上传
		//获取前端的图片文件
//		MultipartFile voFile=vo.getImg();
		//要上传的图片文件
		File imgFile=null;
		//要上传的图片名
		String imgName=null;
		//上传后图片的url
		String imgUrl=null;
		if(voFile==null) return null;
		try {
			//获取图片二进制数据
			byte[] bytes=voFile.getBytes();
			imgName= UUID.randomUUID()+".jpg";
			imgFile=new File(file,imgName);
			//文件输出流对象
			fileOutputStream=new FileOutputStream(imgFile);
			//缓冲流对象
			bufferedOutputStream=new BufferedOutputStream(fileOutputStream);
			//将图片数据写入文件
			bufferedOutputStream.write(bytes);
			//数据推出管道流
			bufferedOutputStream.flush();
			//关闭输出流
			bufferedOutputStream.close();
			//将图片的url写到类中
			imgUrl=baseUrl+filePath+"/"+imgName;
			return imgUrl;
		}catch (IOException e){//发生IO异常
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 上传列表图片
	 * @param filePath 上传地址（用类名即可，第一个字母改为小写，如一个类有单图片和多图片，多加个字段名）
	 *                 例如：/account/img    or    /account/imgList
	 * @param voFileList 上传图片列表
	 * @return
	 */
	@Override
	public List<String> uploadList(String filePath, List<MultipartFile> voFileList){
		File file=new File(basePath+filePath);
		if(!file.exists()){//文件夹不存在
			file.mkdirs();//创建文件夹
		}
		//文件输出流
		FileOutputStream fileOutputStream=null;
		//缓冲输出流
		BufferedOutputStream bufferedOutputStream=null;
		//前端限制好，图片为空不让上传
		//获取前端的图片文件
//		MultipartFile voFile=vo.getImg();
		//要上传的图片文件
		File imgFile=null;
		//要上传的图片名
		String imgName=null;
		//上传后图片的url
		String imgUrl=null;
		//单个文件
		MultipartFile voFile=null;
		//图片地址列表
		List<String> imgUrlList=new ArrayList<>();
		try {
			if(voFileList==null) return null;
			for (int i = 0; i < voFileList.size(); i++) {
				voFile=voFileList.get(i);
				//获取图片二进制数据
				byte[] bytes=voFile.getBytes();
				imgName= UUID.randomUUID()+".jpg";
				imgFile=new File(file,imgName);
				//文件输出流对象
				fileOutputStream=new FileOutputStream(imgFile);
				//缓冲流对象
				bufferedOutputStream=new BufferedOutputStream(fileOutputStream);
				//将图片数据写入文件
				bufferedOutputStream.write(bytes);
				//数据推出管道流
				bufferedOutputStream.flush();
				//关闭输出流
				bufferedOutputStream.close();
				//将图片的url写到类中
				imgUrl=baseUrl+filePath+"/"+imgName;
				imgUrlList.add(imgUrl);
			}
			return imgUrlList;
		}catch (IOException e){//发生IO异常
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 上传列表图片
	 * @param filePath 上传地址（用类名即可，第一个字母改为小写，如一个类有单图片和多图片，多加个字段名）
	 *                 例如：/account/img    or    /account/imgList
	 * @param voFileList 上传图片列表
	 * @return String类型，中间用;分割   注意！mysql表专用
	 */
	@Override
	public String uploadListReturnStr(String filePath, List<MultipartFile> voFileList){
		File file=new File(basePath+filePath);
		if(!file.exists()){//文件夹不存在
			file.mkdirs();//创建文件夹
		}
		//文件输出流
		FileOutputStream fileOutputStream=null;
		//缓冲输出流
		BufferedOutputStream bufferedOutputStream=null;
		//前端限制好，图片为空不让上传
		//获取前端的图片文件
//		MultipartFile voFile=vo.getImg();
		//要上传的图片文件
		File imgFile=null;
		//要上传的图片名
		String imgName=null;
		//上传后图片的url
		String imgUrl=null;
		//单个文件
		MultipartFile voFile=null;
		//图片地址列表
		String imgUrlList="";
		try {
			for (int i = 0; i < voFileList.size(); i++) {
				voFile=voFileList.get(i);
				//获取图片二进制数据
				byte[] bytes=voFile.getBytes();
				imgName= UUID.randomUUID()+".jpg";
				imgFile=new File(file,imgName);
				//文件输出流对象
				fileOutputStream=new FileOutputStream(imgFile);
				//缓冲流对象
				bufferedOutputStream=new BufferedOutputStream(fileOutputStream);
				//将图片数据写入文件
				bufferedOutputStream.write(bytes);
				//数据推出管道流
				bufferedOutputStream.flush();
				//关闭输出流
				bufferedOutputStream.close();
				//将图片的url写到类中
				imgUrl=baseUrl+filePath+"/"+imgName;
				imgUrlList=imgUrlList+";"+imgUrl;
			}
			return imgUrlList;
		}catch (IOException e){//发生IO异常
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据url删除单文件
	 * @param fileUrl 文件路径
	 * @return
	 */
	@Override
    public boolean deleteFile(String fileUrl){
        String path=getPath(fileUrl);
		File file=null;
		try{
			file=new File(path);
			file.delete();
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 根据文件url删除文件集合
	 * @param fileUrlList
	 * @return
	 */
	@Override
	public boolean deleteFileList(List<String> fileUrlList){
		File file=null;
		List<String> pathList=getPathList(fileUrlList);
		if(pathList==null){
			return false;
		}
		for (String path : pathList) {
			try{
				file=new File(path);
				file.delete();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		return true;
	}
	/**
	 * 根据url转换成文件路径
	 * @param url
	 * @return
	 */
	private String getPath(String url){
    	//找到端口号所在下标,并移位至端口号之后
    	int i=url.indexOf(":8989")+5;
    	//文件相对路径
    	String relativePath=url.substring(i);
    	String path=basePath+relativePath;
        return path;
	}

	/**
	 * 根据url集合返回path集合
	 * @param urlList
	 * @return
	 */
	private List<String> getPathList(List<String> urlList){
		List<String> pathList=new ArrayList<>();
		for (String url : urlList) {
			pathList.add(getPath(url));
		}
		return pathList;
	}
}

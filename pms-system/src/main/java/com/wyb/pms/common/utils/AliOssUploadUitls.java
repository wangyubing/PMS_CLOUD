package com.wyb.pms.common.utils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.PutObjectRequest;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.UUID;

/**
 * @todo oss图片上传工具
 * @author 郑霖
 * 2016年11月22日
 */
public class AliOssUploadUitls {
	
	private String endpoint = "";//上传地址
    private String accessKeyId = "";
    private String accessKeySecret = "";
    private String bucketName = "";//空间目录
    
    /**
     * 初始化
     * @param accessKeyId key
     * @param accessKeySecret Secret
     * @param bucketName 空间目录
     * @param endpoint  上传地址
     */
    public AliOssUploadUitls(String accessKeyId, String accessKeySecret, String bucketName, String endpoint){
    	this.accessKeyId=accessKeyId;
    	this.accessKeySecret=accessKeySecret;
    	this.endpoint=endpoint;
    	this.bucketName=bucketName;
    }
    
    /**
	 * byte类型图片上传
	 * @param fileByte
	 * @param fileName
	 * @return
	 */
	public boolean upload(byte[] fileByte,String fileName){
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		boolean exists = false;
		try {
			if (!ossClient.doesBucketExist(bucketName)) {//判断空间是否存在
	            ossClient.createBucket(bucketName);
	            CreateBucketRequest createBucketRequest= new CreateBucketRequest(bucketName);
	            createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
	            ossClient.createBucket(createBucketRequest);
	        }
			ossClient.putObject(new PutObjectRequest(bucketName, fileName,new ByteArrayInputStream(fileByte)));//图片上传
			exists = ossClient.doesObjectExist(bucketName, fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			ossClient.shutdown();
		}
		return exists;
	}
	
	/**
     * 文件类型图片上传
     * @param img
     * @param fileName
     * @return
     */
	public boolean upload(File img,String fileName){
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		boolean exists = false;
		try {
			if (!ossClient.doesBucketExist(bucketName)) {//判断空间是否存在
	            ossClient.createBucket(bucketName);
	            CreateBucketRequest createBucketRequest= new CreateBucketRequest(bucketName);
	            createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
	            ossClient.createBucket(createBucketRequest);
	        }
			ossClient.putObject(new PutObjectRequest(bucketName, fileName,img));//图片上传
			exists = ossClient.doesObjectExist(bucketName, fileName);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			ossClient.shutdown();
		}
		return exists;
	}
	
	/**
     * BASE64类型图片上传
     * @author lihw
     * @created 2016年7月25日 下午3:27:40
     * @param imgname
     * @param img
     * @return
     */
    public boolean upload(String imgname, String img) {
        if (img.contains(",")) {
            img = img.substring(img.indexOf(",") + 1);
        }
        byte[] fileByte = Base64.decodeBase64(img);
        
        String uuid = UUID.randomUUID().toString();
        String fileType = "";
        if (imgname.lastIndexOf(".") > -1) {
            fileType = imgname.substring(imgname.lastIndexOf(".") + 1);
        }
        String expectKey="open"+"_"+uuid + "." + fileType;//图片文件名
		boolean exists = upload(fileByte,expectKey);
		return exists;
    }
	
}

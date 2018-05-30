package com.wyb.pms.service;

import com.wyb.pms.common.result.GlobalErrorInfoEnum;
import com.wyb.pms.common.result.ResultBody;
import com.wyb.pms.common.utils.AliOssUploadUitls;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;


/**
 * 阿里巴巴OSS上传服务
 * @author htl
 * @时间  2016年11月23日下午3:22:17
 */
@Service("aliUoloadService")
public class AliUoloadService {
    
	private static final String ACCESSKEYID = "k9HHSeEXnS5LZYqF";
	private static final String ACCESSKEYSECRET = "QvY9GJ2YEFMwEK8v8873bUUgzXoiz9";
	private static final int BLOCK_SIZE = 4 * 1024;
	
	private static final String BUCKETNAME = "yunba";
	private static final String IMAGE_URL = "http://static01.yunba.com";
	
	/**
	 * 上传图片
	 * @author lihw
	 * @created 2016年11月24日 下午6:04:15
	 *
	 * @param imgname
	 * @param bytes
	 * @return
	 */
	public ResultBody uploadBytes(String imgname, byte[] bytes) {
        ResultBody rdf = new ResultBody();
        String uuid = UUID.randomUUID().toString();
        String fileType = "";
        if (imgname.lastIndexOf(".") > -1) {
            fileType = imgname.substring(imgname.lastIndexOf(".") + 1);
        }
        
        String expectKey= "open_" + uuid + "." + fileType;
        
        AliOssUploadUitls auu=new AliOssUploadUitls(ACCESSKEYID, ACCESSKEYSECRET, BUCKETNAME, IMAGE_URL);
        boolean status = auu.upload(bytes, expectKey);
        
        if(status){
            rdf.setCode(GlobalErrorInfoEnum.SUCCESS.getCode());
            rdf.setMessage("图片上传成功");
            rdf.setResult(IMAGE_URL + "/" + expectKey);
            return rdf;
        }else{
            rdf.setCode(GlobalErrorInfoEnum.ERROR_SERVER.getCode());
            rdf.setMessage("图片上传失败");
            return rdf;
        }
	}
	
    /**
     * 上传文件
     * 2016年11月23日下午2:53:59
     */
    public ResultBody uploadFile(String imgname, File img) {
        return uploadBytes(imgname, File2byte(img));
    }

    /**
     * base64上传文件
     * 2016年11月23日下午3:00:16
     */
    public ResultBody uploadBase64(String imgname, String img) {
        try {
            return uploadBytes(imgname, convertBase64ToBytes(img));
        } catch (IOException e) {
            ResultBody rdf = new ResultBody();
            rdf.setCode(GlobalErrorInfoEnum.ERROR_SERVER.getCode());
            rdf.setMessage("图片上传失败");
            return rdf;
        }
    }
    
    public static byte[] File2byte(File file) {
        byte[] bytes = new byte[0];
        byte[] buffer = new byte[BLOCK_SIZE];
        try {
            FileInputStream fis = new FileInputStream(file);
            int n;
            while ((n = fis.read(buffer)) != -1) {
                bytes = combineByteArrays(bytes, Arrays.copyOf(buffer, n));
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }
    
    /**
     * 连接数组
     * @author lihw
     * @created 2016年11月25日 上午10:57:06
     *
     * @param a
     * @param b
     * @return
     */
    private static byte[] combineByteArrays(byte[] a, byte[] b) {
        if (a.length == 0) {
            return b;
        }
        if (b.length == 0) {
            return a;
        }
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }
    
    
    public static byte[] convertBase64ToBytes(String base64Str) throws IOException {
        if (base64Str.contains("base64,")) {
            base64Str = base64Str.split("base64,")[1];
        }
//        System.out.println(base64Str);
        byte[] bytes = null;
        sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
        bytes = decoder.decodeBuffer(base64Str);
        return bytes;
    }
}

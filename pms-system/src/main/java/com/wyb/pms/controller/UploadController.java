package com.wyb.pms.controller;


import com.wyb.pms.common.result.GlobalErrorInfoEnum;
import com.wyb.pms.common.result.ResultBody;
import com.wyb.pms.service.AliUoloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

/**
 *
 * Created by Administrator on 2017-02-27.
 */
@RestController
@RequestMapping("/api/utils/upload")
@SuppressWarnings("unchecked")
public class UploadController {

    /*@Autowired
    private HttpServletRequest req;*/

    @Autowired
    private AliUoloadService aliUoloadService;

    @RequestMapping("/img/file")
    @ResponseBody
    public ResultBody upload(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, ModelMap model) {


        ResultBody rdf = new ResultBody();
        if(!file.isEmpty()){
            try {
                  /*
                   * 这段代码执行完毕之后，图片上传到了工程的跟路径；
                   * 大家自己扩散下思维，如果我们想把图片上传到 d:/files大家是否能实现呢？
                   * 等等;
                   * 这里只是简单一个例子,请自行参考，融入到实际中可能需要大家自己做一些思考，比如：
                   * 1、文件路径；
                   * 2、文件名；
                   * 3、文件格式;
                   * 4、文件大小的限制;
                   */

                String path = request.getSession().getServletContext().getRealPath("upload");
                File dir = new File(path);
                if(!dir.exists()){
                    dir.mkdir();
                }

                File newFile = new File(path + file.getOriginalFilename());
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(newFile));
                out.write(file.getBytes());
                out.flush();
                out.close();
                rdf = aliUoloadService.uploadFile(file.getOriginalFilename(), newFile);
                String imgPath = rdf.getResult().toString().replace("http://static01.yunba.com/","");
                rdf.setMessage(imgPath);
                newFile.delete();
                rdf.setCode(GlobalErrorInfoEnum.SUCCESS.getCode());
                return rdf;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                rdf.setCode(GlobalErrorInfoEnum.ERROR_SERVER.getCode());
                return rdf;
            } catch (IOException e) {
                e.printStackTrace();
                rdf.setCode(GlobalErrorInfoEnum.ERROR_SERVER.getCode());
                return rdf;
            }
        }else{
            rdf.setCode(GlobalErrorInfoEnum.ERROR_SERVER.getCode());
            rdf.setMessage("上传失败，因为文件是空的.");
            return rdf;
        }

    }

    /*@RequestMapping("/img/file")
    @ResponseBody
    public Object uploadImgFile(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        ResponseDataForm rdf = new ResponseDataForm();

        Map<String, Object> reqParams = getMutilpartRequestParams(req);
        ArrayList<File> files = new ArrayList<File>();
        if(reqParams.get("imgs")!=null){
            files = (ArrayList<File>) reqParams.get("imgs");
        }else{
            File img = (File) reqParams.get("img");
            files.add(img);
        }
        String imgPath = "";
        for (File img : files) {
            String imgname  = img.getName();

            if (StringUtils.isEmpty(imgname)) {
                rdf.setResult(ResponseDataForm.FAILURE);
                rdf.setResultInfo("图片上传业务必要参数为空");
                return rdf;
            }

            rdf = aliUoloadService.uploadFile(imgname, img);
            imgPath += "," + rdf.getResultObj();
        }
        rdf.setResult(ResponseDataForm.SUCCESS);
        rdf.setResultObj("".equals(imgPath)?"":imgPath.substring(1));
        return rdf;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getMutilpartRequestParams(HttpServletRequest req) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();

        DiskFileItemFactory dfif = new DiskFileItemFactory();
        dfif.setSizeThreshold(4 * 1024 * 1024);
        File tempDir = new File("/open/tmp/");
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        dfif.setRepository(tempDir);
        ServletFileUpload fileUpload = new ServletFileUpload(dfif);
        List<FileItem> items = fileUpload.parseRequest(req);
        ArrayList<File> files = new ArrayList<File>();
        for (FileItem item : items) {
            if (item.isFormField()) {
                String fieldName = item.getFieldName();
                params.put(fieldName, item.getString());
            } else {
                String fileName = item.getName();
                String fileType = "";
                if (fileName.lastIndexOf(".") > -1) {
                    fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
                }
                File file = new File(tempDir.getPath() + File.separator + UUID.randomUUID() + "." + fileType);
                item.write(file);
                //params.put("img", file);
                files.add(file);
            }
        }
        if(files.size() == 1){
            params.put("img", files.get(0));
        }else{
            params.put("imgs", files);
        }

        return params;
    }*/
}

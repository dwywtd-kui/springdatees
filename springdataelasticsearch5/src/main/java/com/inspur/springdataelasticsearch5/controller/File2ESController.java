package com.inspur.springdataelasticsearch5.controller;

import com.inspur.springdataelasticsearch5.entity.Article;
import com.inspur.springdataelasticsearch5.repository.ESRepository;
import com.inspur.springdataelasticsearch5.util.File2StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@Controller
@ResponseBody
public class File2ESController {

    @Autowired
    private ESRepository esRepository;
    private ElasticsearchTemplate esTemplate;

    @Value("${FILE_PATH}")
    private String FILE_PATH;

    @RequestMapping("/test2")
    public String test(String filepath){
        return filepath;
    }

    @RequestMapping("/upload")
    public String test2Es(@RequestParam(value = "uploadfile")  MultipartFile multipartFile, HttpServletRequest request) throws Exception {
        System.out.println("SpringMVC方式的文件上传...");
        // 先获取到要上传的文件目录
        String path =FILE_PATH;
        System.out.println(path);
        // 创建File对象，一会向该路径下上传文件
        File file = new File(path);
        // 判断路径是否存在，如果不存在，创建该路径
        if(!file.exists()) {
            file.mkdirs();
        }
        // 获取到上传文件的名称
        String filename = multipartFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        // 把文件的名称唯一化
        String fileid = uuid+"_"+filename;
        // 上传文件
        multipartFile.transferTo(new File(file,fileid));
        //上传到ES中去
        String file2String = File2StringUtil.file2String(path + fileid);
        Article article =new Article(fileid,filename,file2String);
        esRepository.save(article);
        return "success";
    }
    @RequestMapping("/download")
    private String downloadFile(@RequestParam("filename") String fileName,HttpServletResponse response){
        String filePath = FILE_PATH+fileName;//被下载的文件在服务器中的路径,
        System.out.println(fileName);
        File file = new File(filePath);
        if (file.exists()) {
            response.setContentType("application/force-download");// 设置强制下载不打开            
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream outputStream = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    outputStream.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                return "success";
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "error";
    }
}

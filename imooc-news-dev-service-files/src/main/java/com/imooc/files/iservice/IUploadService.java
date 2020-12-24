package com.imooc.files.iservice;


import org.springframework.web.multipart.MultipartFile;

/**
 * @author zimin
 * @fucntion 上传文件service
 */
public interface IUploadService {

    public String uploadFdfs(MultipartFile file, String fileExtName) throws Exception;


}

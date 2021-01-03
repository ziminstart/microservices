package com.imooc.files.iservice;


import org.springframework.web.multipart.MultipartFile;

/**
 * @author zimin
 * @fucntion 上传文件service
 */
public interface IUploadService {

    /**
     * 使用fastdfs 上传文件
     *
     * @param file
     * @param fileExtName
     * @return
     * @throws Exception
     */
    public String uploadFdfs(MultipartFile file, String fileExtName) throws Exception;


    /**
     * 使用Ali OSS上传文件
     *
     * @param file
     * @param fileExtName
     * @return
     * @throws Exception
     */
    public String uploadOSS(MultipartFile file, String userId, String fileExtName) throws Exception;

}

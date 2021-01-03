package com.imooc.files.service;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.imooc.files.iservice.IUploadService;
import com.imooc.files.resource.FileResource;
import com.imooc.utils.extend.AliyunResources;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @author zimin
 * @fucntion 上传文件service
 */
@Service
public class UploadServiceImpl implements IUploadService {

    @Autowired
    public FastFileStorageClient fastFileStorageClient;
    @Autowired
    private FileResource fileResource;
    @Autowired
    private AliyunResources aliyunResources;
    @Autowired
    private Sid sid;


    @Override
    public String uploadFdfs(MultipartFile file, String fileExtName) throws Exception {
        StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(),
                file.getSize(),
                fileExtName, null);
        return storePath.getFullPath();
    }

    @Override
    public String uploadOSS(MultipartFile file, String userId, String fileExtName) throws Exception {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = fileResource.getEndpoint();
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = aliyunResources.getAccessKeyID();
        String accessKeySecret = aliyunResources.getAccessKeySecret();
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        String fileName = sid.nextShort();
        String myObjectName = fileResource.getObjectName() + "/" + userId + "/" + fileName  + "." + fileExtName;
        // 上传网络流。
        InputStream inputStream = file.getInputStream();
        ossClient.putObject(fileResource.getBucketName(), myObjectName, inputStream);
        // 关闭OSSClient。
        ossClient.shutdown();
        return myObjectName;
    }

}

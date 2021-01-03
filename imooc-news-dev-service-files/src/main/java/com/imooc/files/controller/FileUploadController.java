package com.imooc.files.controller;

import com.imooc.api.controller.files.IFilesControllerApi;
import com.imooc.files.resource.FileResource;
import com.imooc.files.iservice.IUploadService;
import com.imooc.grace.result.R;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.utils.extend.AliImageReviewUtils;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zimin
 */
@RestController
@Slf4j
public class FileUploadController implements IFilesControllerApi {

    @Autowired
    private IUploadService uploadService;

    @Autowired
    private FileResource fileResource;
    @Autowired
    private AliImageReviewUtils aliImageReviewUtils;


    @Override
    public R uploadFace(String userId, MultipartFile file) throws Exception {
        String path = null;
        if (file != null) {
            //获取文件上传名称
            String fileName = file.getOriginalFilename();
            //判断文件名不能为空
            if (!StringUtils.isEmpty(fileName)) {
                String[] fileNameArr = fileName.split("\\.");
                //获得后缀
                String suffix = fileNameArr[fileNameArr.length - 1];
                //判断后缀符合我们的预定义的规范
                if (!suffix.equalsIgnoreCase("png")
                        && !suffix.equalsIgnoreCase("jpg")
                        && !suffix.equalsIgnoreCase("jpeg")){
                    return R.errorCustom(ResponseStatusEnum.FILE_FORMATTER_FAILD);
                }
//                path = uploadService.uploadFdfs(file, suffix);
                path = uploadService.uploadOSS(file,userId,suffix);
            } else {
                return R.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
            }
        } else {
            return R.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
        }
        String finalPath = "";
        if (StringUtils.isEmpty(path)){
            return R.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
        } else {
            finalPath = fileResource.getOssHost() +  path;
        }

        log.info("path:{}" ,finalPath);
        return R.ok(finalPath);
    }

    public static  final String FAILED_IMAGE_URL = "";
    private String doAliImageReview (String pendingImageUrl) {
        /**
         * 默认存在于内网，无法被阿里云内容管理服务检查到
         * 需要配置到俊刚都行
         * 1 内网穿透 natppp/花生壳/ngrok
         * 2 路由配置端口映射
         */
        boolean result = false;
        try {
            result = aliImageReviewUtils.reviewImage(pendingImageUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!result) {
            return FAILED_IMAGE_URL;
        }
        return pendingImageUrl;
    }

}

package com.imooc.files.controller;

import com.imooc.api.controller.files.IFilesControllerApi;
import com.imooc.exception.GraceException;
import com.imooc.files.iservice.IUploadService;
import com.imooc.grace.result.R;
import com.imooc.grace.result.ResponseStatusEnum;
import lombok.extern.slf4j.Slf4j;
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
                path = uploadService.uploadFdfs(file, suffix);
            } else {
                return R.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
            }
        } else {
            return R.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
        }
        log.info("path:{}" ,path);
        return R.ok(path);
    }
}

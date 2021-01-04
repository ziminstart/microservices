package com.imooc.files.controller;

import com.imooc.api.controller.files.IFilesControllerApi;
import com.imooc.exception.GraceException;
import com.imooc.files.iservice.IUploadService;
import com.imooc.files.resource.FileResource;
import com.imooc.grace.result.R;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.model.bo.NewAdminBO;
import com.imooc.utils.FileUtils;
import com.imooc.utils.extend.AliImageReviewUtils;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.model.Filters;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    private GridFSBucket gridFSBucket;


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
                        && !suffix.equalsIgnoreCase("jpeg")) {
                    return R.errorCustom(ResponseStatusEnum.FILE_FORMATTER_FAILD);
                }
//                path = uploadService.uploadFdfs(file, suffix);
                path = uploadService.uploadOSS(file, userId, suffix);
            } else {
                return R.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
            }
        } else {
            return R.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
        }
        String finalPath = "";
        if (StringUtils.isEmpty(path)) {
            return R.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
        } else {
            finalPath = fileResource.getOssHost() + path;
        }

        log.info("path:{}", finalPath);
        return R.ok(finalPath);
    }

    @Override
    public R uploadSomeFiles(String userId, MultipartFile[] files) throws Exception {
        //声明一个list,用于存放多个图片地址路径,返回到前端
        List<String> imageUrlList= new ArrayList<>();

        if (files != null && files.length > 0){
            for (MultipartFile file : files) {
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
                                && !suffix.equalsIgnoreCase("jpeg")) {
                            continue;
                        }
//                path = uploadService.uploadFdfs(file, suffix);
                        path = uploadService.uploadOSS(file, userId, suffix);
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
                String finalPath = "";
                if (StringUtils.isEmpty(path)) {
                    continue;
                } else {
                    finalPath = fileResource.getOssHost() + path;
                    //FIXME:放入到list之前对图片进行审核
                    imageUrlList.add(finalPath);
                }
            }
        }
        return R.ok(imageUrlList);
    }

    @Override
    public R uploadToGridFS(NewAdminBO newAdminBO) throws Exception {
        //获取图片的base64字符串
        String img64 = newAdminBO.getImg64();
        //将base64字符串转换为byte数组
        byte[] bytes = new BASE64Decoder().decodeBuffer(img64.trim());

        //转换为输入流
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        //上传到gridfs中
        ObjectId fileId = gridFSBucket.uploadFromStream(newAdminBO.getUsername() + ".png", byteArrayInputStream);
        //获得文件在gridfs中的主键
        String fileIdStr = fileId.toString();

        return R.ok(fileIdStr);
    }

    @Override
    public void readInGridFS(String faceId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 0 判断参数
        if (StringUtils.isEmpty(faceId) || faceId.equalsIgnoreCase("")) {
            GraceException.display(ResponseStatusEnum.FILE_NOT_EXIST_ERROR);
        }

        // 1 从gridfs中读取
        File adminFace = readGridFSByFaceId(faceId);
        // 2 把人脸图片输出到浏览器
        FileUtils.downloadFileByStream(response, adminFace);
    }

    @Override
    public R readFace64InGridFS(String faceId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        File myface = readGridFSByFaceId(faceId);
        String base64Face  = FileUtils.fileToBase64(myface);
        return R.ok(base64Face);
    }


    /**
     * 内容安全检测
     */
    public static final String FAILED_IMAGE_URL = "";

    private String doAliImageReview(String pendingImageUrl) {
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

    private File readGridFSByFaceId(String faceId) throws FileNotFoundException {
        GridFSFindIterable gridFsFiles = gridFSBucket.find(Filters.eq("_id", new ObjectId(faceId)));
        GridFSFile gridFS = gridFsFiles.first();
        if (gridFS == null) {
            GraceException.display(ResponseStatusEnum.FILE_NOT_EXIST_ERROR);
        }
        String fileName = gridFS.getFilename();
        System.out.println(fileName);

        //获取文件流，保存文件到本地或者到服务器的临时目录
        File fileTemp = new File("/Users/zimin/Desktop/img");
        if (!fileTemp.exists()) {
            fileTemp.mkdirs();
        }
        File myFile = new File("/Users/zimin/Desktop/img/" + fileName);
        //创建文件输出流
        OutputStream fileOutputStream = new FileOutputStream(myFile);
        //下载到服务器或者本地
        gridFSBucket.downloadToStream(new ObjectId(faceId), fileOutputStream);
        return myFile;
    }

}

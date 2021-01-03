package com.imooc.api.controller.files;


import com.imooc.grace.result.R;
import com.imooc.model.bo.NewAdminBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zimin
 * @function: api的作用
 * <p>
 * api 就相当于企业的领导，老板，部门经理
 * 其他的服务层都是实现，他们就相当于员工，只做事情
 * 所有的api接口就是统一在这理进行管理和调度的，微服务也是如此
 * <p>
 * 运作:
 * 现在所有的接口都在些暴露，实现都是在各自的微服务中
 * 本项目只写接口，不写实现，实现在各自的微服务工程中，因为以业务来划分的微服务有很多
 * controller也会在分散在各个微服务中，一旦多了就很难统一管理和查看
 * <p>
 * 其次，微服务之间的调用都是基于接口的
 * 如果不这么做，微服务之间的调用就需要相互依赖了
 * 耦合度也很高了，接口的目的为了能够提供解耦
 * <p>
 * 此外，本工程的接口其实就是一套规范。实现都是由各自的工程去做的处理
 */
@Api(value = "实现文件上传的controller", tags = {"实现文件上传的controller"})
@RequestMapping("fs")
public interface IFilesControllerApi {


    /**
     * @return
     */
    @ApiOperation(value = "上传用户头像", notes = "上传用户头像", httpMethod = "POST")
    @PostMapping("/uploadFace")
    R uploadFace(@RequestParam String userId, MultipartFile file) throws Exception;


    /**
     * 文件上传到mongodb的gridfs
     *
     * @param newAdminBO
     * @return
     * @throws Exception
     */
    @PostMapping("/uploadToGridFS")
    R uploadToGridFS(@RequestBody NewAdminBO newAdminBO) throws Exception;


    /**
     * 查询人脸图片
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/readInGridFS")
    void readInGridFS(@RequestParam String faceId,
                      HttpServletRequest request,
                      HttpServletResponse response) throws Exception;


    /**
     * 从gridfs中读取图片数据
     * @param faceId
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @GetMapping("/readFace64InGridFS")
    R readFace64InGridFS(@RequestParam String faceId,
                      HttpServletRequest request,
                      HttpServletResponse response) throws Exception;
}

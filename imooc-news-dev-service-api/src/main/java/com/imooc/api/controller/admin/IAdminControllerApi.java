package com.imooc.api.controller.admin;


import com.imooc.grace.result.R;
import com.imooc.model.bo.AdminLoginBO;
import com.imooc.model.bo.NewAdminBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
@Api(value = "管理员admin维护", tags = {"管理员admin维护的controller"})
@RequestMapping("adminMng")
public interface IAdminControllerApi {


    /**
     * @return
     */
    @ApiOperation(value = "上传用户头像", notes = "上传用户头像", httpMethod = "POST")
    @PostMapping("/adminLogin")
    R adminLogin(@RequestBody AdminLoginBO adminLoginBO,
                 HttpServletRequest request,
                 HttpServletResponse response);

    @ApiOperation(value = "查询admin用户是否存在", notes = "查询admin用户是否存在", httpMethod = "POST")
    @PostMapping("/adminIsExist")
    R adminIsExist(@RequestParam String username);

    @ApiOperation(value = "创建admin", notes = "创建admin", httpMethod = "POST")
    @PostMapping("/addNewAdmin")
    R addNewAdmin(@RequestBody @Valid NewAdminBO newAdminBO, BindingResult result,
                  HttpServletRequest request,
                  HttpServletResponse response);

    @ApiOperation(value = "查询admin列表", notes = "查询admin列表", httpMethod = "POST")
    @PostMapping("/getAdminList")
    R getAdminList(@ApiParam(name = "page", value = "查询下一页的第几页", required = false) @RequestParam Integer page,
                   @ApiParam(name = "pageSize", value = "一页展示多少条数据", required = false) @RequestParam Integer pageSize);

    @ApiOperation(value = "admin注销登录", notes = "admin注销登录", httpMethod = "POST")
    @PostMapping("/adminLogout")
    R adminLogout(@RequestParam String adminId,
                  HttpServletRequest request,
                  HttpServletResponse response);

    @ApiOperation(value = "admin管理员的人脸登录", notes = "admin管理员的人脸登录", httpMethod = "POST")
    @PostMapping("/adminFaceLogin")
    R adminFaceLogin(@RequestBody AdminLoginBO adminLoginBO,
                     HttpServletRequest request,
                     HttpServletResponse response);
}

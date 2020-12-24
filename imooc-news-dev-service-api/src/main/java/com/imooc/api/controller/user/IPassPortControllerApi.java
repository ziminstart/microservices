package com.imooc.api.controller.user;


import com.imooc.grace.result.R;
import com.imooc.files.bo.RegisterLoginBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Api(value = "用户注册登录", tags = {"用户注册登录的controller"})
@RequestMapping("passport")
public interface IPassPortControllerApi {


    /**
     * 获取短信的验证码
     *
     * @return
     */
    @ApiOperation(value = "获得短信的验证方法", notes = "获得短信的验证方法", httpMethod = "GET")
    @GetMapping("/getSMSCode")
    R getSmsCode(@RequestParam String mobile, HttpServletRequest request);

    /**
     * 一键登录和注册接口
     *
     * @param registerLoginBO
     * @return
     */
    @ApiOperation(value = "一键注册登录接口", notes = "一键注册登录接口", httpMethod = "POST")
    @PostMapping("/doLogin")
    R doLogin(@RequestBody @Valid RegisterLoginBO registerLoginBO,
              BindingResult result,
              HttpServletRequest request,
              HttpServletResponse response);


    /**
     * 注销登录
     *
     * @return
     */
    @ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    R logout(@RequestParam String userId,
             HttpServletRequest request,
             HttpServletResponse response);

}

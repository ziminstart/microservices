package com.imooc.api.controller.user;


import com.imooc.grace.result.R;
import com.imooc.model.bo.UpdateUserInfoBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author zimin
 * @function: api的作用
 */
@Api(value = "用户信息相关controller", tags = {"用户信息相关controller"})
@RequestMapping("user")
public interface IUserControllerApi {



    /**
     * 获得用户账户信息
     * @return
     */
    @ApiOperation(value = "获得用户的基本信息", notes = "获得用户的基本信息", httpMethod = "POST")
    @PostMapping("/getUserInfo")
    R getUserInfo(@RequestParam String userId);

    /**
     * 获得用户账户信息
     * @return
     */
    @ApiOperation(value = "获得用户的账户信息", notes = "获得用户的账户信息", httpMethod = "POST")
    @PostMapping("/getAccountInfo")
    R accountInfo(@RequestParam String userId);

    /**
     * 获得用户账户信息
     * @return
     */
    @ApiOperation(value = "修改/完善用户信息", notes = "修改/完善用户信息", httpMethod = "POST")
    @PostMapping("/updateUserInfo")
    R updateUserInfo(@RequestBody @Valid UpdateUserInfoBO updateUserInfoBO, BindingResult result);


    @ApiOperation(value = "根据用户的ids查询用户列表", notes = "根据用户的ids查询用户列表", httpMethod = "GET")
    @GetMapping("/queryByIds")
    R queryByIds(@RequestParam String userIds);


}

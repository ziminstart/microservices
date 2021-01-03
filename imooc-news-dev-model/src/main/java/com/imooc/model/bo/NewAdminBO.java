package com.imooc.model.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 添加管理人员的bo
 */
@Data
public class NewAdminBO {

    @NotBlank(message = "用户名不可为空")
    private String username;
    @NotBlank(message = "负责人不可为空")
    private String adminName;
    @NotBlank(message = "密码不可为空")
    private String password;
    @NotBlank(message = "确认密码不可为空")
    private String confirmPassword;
    private String img64;
    private String faceId;

}

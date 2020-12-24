package com.imooc.files.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author zimin
 */
@Data
public class RegisterLoginBO {

    @NotBlank(message = "手机号不可为空")
    private String mobile;

    @NotBlank(message = "验证码不可为空")
    private String smsCode;

}

package com.imooc.model.bo;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

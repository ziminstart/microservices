package com.imooc.model.bo;

import lombok.Data;

/**
 * 管理员登陆的BO
 */
@Data
public class AdminLoginBO {
    private String username;
    private String password;
    private String img64;
}

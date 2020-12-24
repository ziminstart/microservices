package com.imooc.files.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 网站用户
 * </p>
 *
 * @author 恒利
 * @since 2020-11-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserAccountInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 昵称，媒体号
     */
    private String nickname;
    /**
     * 头像
     */
    private String face;
    /**
     * 真实姓名
     */
    private String realname;
    /**
     * 邮箱地址
     */
    private String email;
    /**
     * 性别 1:男  0:女  2:保密
     */
    private Integer sex;
    /**
     * 生日
     */
    private LocalDate birthday;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * 区县
     */
    private String district;
}

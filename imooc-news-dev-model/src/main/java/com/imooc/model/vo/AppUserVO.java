package com.imooc.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

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
public class AppUserVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    /**
     * 昵称，媒体号
     */
    private String nickname;
    /**
     * 头像
     */
    private String face;

    /**
     * 用户状态：0：未激活。 1：已激活：基本信息是否完善，真实姓名，邮箱地址，性别，生日，住址等，如果没有完善，则用户不能在作家中心操作，不能关注。2：已冻结。
     */
    private Integer activeStatus;
}

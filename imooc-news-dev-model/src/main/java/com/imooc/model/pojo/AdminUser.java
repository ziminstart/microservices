package com.imooc.model.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 运营管理平台的admin级别用户
 * </p>
 *
 * @author 恒利
 * @since 2020-11-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("admin_user")
public class AdminUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 人脸入库图片信息，该信息保存到mongoDB的gridFS中
     */
    @TableField("face_id")
    private String faceId;

    /**
     * 管理人员的姓名
     */
    @TableField("admin_name")
    private String adminName;

    /**
     * 创建时间 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;


}

package com.imooc.model.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

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
@TableName("app_user")
public class AppUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;

    /**
     * 手机号
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 昵称，媒体号
     */
    @TableField("nickname")
    private String nickname;

    /**
     * 头像
     */
    @TableField("face")
    private String face;

    /**
     * 真实姓名
     */
    @TableField("realname")
    private String realname;

    /**
     * 邮箱地址
     */
    @TableField("email")
    private String email;

    /**
     * 性别 1:男  0:女  2:保密
     */
    @TableField("sex")
    private Integer sex;

    /**
     * 生日
     */
    @TableField("birthday")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    /**
     * 省份
     */
    @TableField("province")
    private String province;

    /**
     * 城市
     */
    @TableField("city")
    private String city;

    /**
     * 区县
     */
    @TableField("district")
    private String district;

    /**
     * 用户状态：0：未激活。 1：已激活：基本信息是否完善，真实姓名，邮箱地址，性别，生日，住址等，如果没有完善，则用户不能在作家中心操作，不能关注。2：已冻结。
     */
    @TableField("active_status")
    private Integer activeStatus;

    /**
     * 累计已结算的收入金额，也就是已经打款的金额，每次打款后再此累加
     */
    @TableField("total_income")
    private Integer totalIncome;

    /**
     * 创建时间 创建时间
     */
    @TableField("create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间 更新时间
     */
    @TableField("update_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;


}

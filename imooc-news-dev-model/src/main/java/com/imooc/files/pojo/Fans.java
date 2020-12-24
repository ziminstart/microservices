package com.imooc.files.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 粉丝表，用户与粉丝的关联关系，粉丝本质也是用户。
关联关系保存到es中，粉丝数方式和用户点赞收藏文章一样。累加累减都用redis来做。
字段与用户表有些冗余，主要用于数据可视化，数据一旦有了之后，用户修改性别和省份无法影响此表，只认第一次的数据。


 * </p>
 *
 * @author 恒利
 * @since 2020-11-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("fans")
public class Fans implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;

    /**
     * 作家用户id
     */
    @TableField("writer_id")
    private String writerId;

    /**
     * 粉丝用户id
     */
    @TableField("fan_id")
    private String fanId;

    /**
     * 粉丝头像
     */
    @TableField("face")
    private String face;

    /**
     * 粉丝昵称
     */
    @TableField("fan_nickname")
    private String fanNickname;

    /**
     * 粉丝性别
     */
    @TableField("sex")
    private Integer sex;

    /**
     * 省份
     */
    @TableField("province")
    private String province;


}

package com.imooc.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

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
public class IndexArticleVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String title;
    private String content;
    private Integer categoryId;
    private Integer articleType;
    private String articleCover;
    private Integer isAppoint;
    private Integer articleStatus;
    private String publishUserId;
    private LocalDateTime publishTime;
    private Integer readCounts;
    private Integer commentCounts;
    private String mongoFileId;
    private Integer isDelete;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private AppUserVO appUserVO;
}

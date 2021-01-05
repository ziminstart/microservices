package com.imooc.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imooc.model.pojo.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 文章资讯表 Mapper 接口
 * </p>
 *
 * @author 恒利
 * @since 2020-12-09
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    @Update("UPDATE  article SET is_appoint = 0 WHERE publish_time <= NOW() AND is_appoint = 1")
    void updateAppointToPublish();

}

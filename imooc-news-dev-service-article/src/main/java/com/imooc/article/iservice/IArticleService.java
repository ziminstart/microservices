package com.imooc.article.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.model.bo.NewArticleBO;
import com.imooc.model.pojo.Article;
import com.imooc.model.pojo.Category;

/**
 * <p>
 * 文章资讯表 服务类
 * </p>
 *
 * @author 恒利
 * @since 2020-12-09
 */
public interface IArticleService extends IService<Article> {

    /**
     * 发布文章
     * @param newArticleBO
     * @param category
     */
    void createArticle(NewArticleBO newArticleBO, Category category);

}

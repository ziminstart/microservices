package com.imooc.article.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.model.bo.NewArticleBO;
import com.imooc.model.pojo.Article;
import com.imooc.model.pojo.Category;
import com.imooc.utils.PagedGridResult;

/**
 * <p>
 * 文章资讯表 服务类
 * </p>
 *
 * @author 恒利
 * @since 2020-12-09
 */
public interface IArticlePortalService extends IService<Article> {

    /**
     * 首页查询文章列表
     * @param keyword
     * @param category
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult queryIndexArticleList(String keyword, Integer category, Integer page, Integer pageSize);

}

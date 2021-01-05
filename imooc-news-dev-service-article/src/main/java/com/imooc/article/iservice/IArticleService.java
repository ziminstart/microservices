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
public interface IArticleService extends IService<Article> {

    /**
     * 发布文章
     *
     * @param newArticleBO
     * @param category
     */
    void createArticle(NewArticleBO newArticleBO, Category category);

    /**
     * 更新定时发布
     */
    void updateAppointToPublish();

    /**
     * 用户中心 - 查询我的文章列表
     *
     * @return
     */
    PagedGridResult queryMyArticleList(String userId,
                                       String keyword,
                                       Integer status,
                                       String startDate,
                                       String endDate,
                                       Integer page,
                                       Integer pageSize);

    /**
     * 修改文章状态
     * @param articleId
     * @param pendingStatus
     */
    void updateArticleStatus(String articleId, Integer pendingStatus);

}

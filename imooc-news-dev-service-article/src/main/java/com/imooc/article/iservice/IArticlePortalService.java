package com.imooc.article.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.model.bo.NewArticleBO;
import com.imooc.model.pojo.Article;
import com.imooc.model.pojo.Category;
import com.imooc.model.vo.ArticleDetailVO;
import com.imooc.utils.PagedGridResult;

import java.util.List;

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
    PagedGridResult queryIndexArticleList(String keyword, Integer category, Integer page, Integer pageSize);


    /**
     * 首页查询热闻列表
     * @return
     */
    List<Article> queryHotArticleList();

    /**
     * 查询作家发布的所有文章列表
     */
    PagedGridResult queryArticleListOfWriter(String writerId,
                                                    Integer page,
                                                    Integer pageSize);

    /**
     * 作家页面查询近期佳文
     */
    PagedGridResult queryGoodArticleListOfWriter(String writerId);

    /**
     * 查询文章详情
     */
    ArticleDetailVO queryDetail(String articleId);
}

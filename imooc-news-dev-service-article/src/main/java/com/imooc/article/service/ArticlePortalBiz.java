package com.imooc.article.service;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.api.service.IBaseService;
import com.imooc.article.iservice.IArticlePortalService;
import com.imooc.article.mapper.ArticleMapper;
import com.imooc.enums.ArticleReviewStatus;
import com.imooc.enums.YesOrNo;
import com.imooc.model.pojo.Article;
import com.imooc.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章资讯表 服务实现类
 * </p>
 *
 * @author 恒利
 * @since 2020-12-09
 */
@Service
public class ArticlePortalBiz extends ServiceImpl<ArticleMapper, Article> implements IArticlePortalService, IBaseService {

    @Autowired
    private ArticleMapper  articleMapper;

    @Override
    public PagedGridResult queryIndexArticleList(String keyword, Integer category, Integer page, Integer pageSize) {
        Page<Article> articlePage = new Page<>();
        articlePage.setCurrent(page);
        articlePage.setSize(pageSize);
        /**
         * 查询首页文章的自带隐性查询条件
         * isApoint = 即时发布,表示文章已经直接发布,或者定时任务到点发布
         * isDelete = 未删除,表示文章只能够显示未删除
         * articleStatus = 审核通过,表示只有文章经过机审/人工审核之后才能展示
         */
        Page<Article> articlePageList = articleMapper.selectPage(articlePage, Wrappers.<Article>lambdaQuery()
                .eq(Article::getIsAppoint, YesOrNo.NO.type)
                .eq(Article::getIsDelete, YesOrNo.NO.type)
                .eq(Article::getArticleStatus, ArticleReviewStatus.SUCCESS.type)
                .eq(Article::getCategoryId, category)
                .like(StringUtils.isNotBlank(keyword), Article::getTitle, keyword)
                .orderByDesc(Article::getPublishTime));

        return setterPagedGrid(articlePageList);
    }
}

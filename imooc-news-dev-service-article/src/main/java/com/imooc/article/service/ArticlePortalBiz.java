package com.imooc.article.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
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
import com.imooc.model.vo.ArticleDetailVO;
import com.imooc.utils.PagedGridResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private ArticleMapper articleMapper;

    @Override
    public PagedGridResult queryIndexArticleList(String keyword, Integer category, Integer page, Integer pageSize) {
        Page<Article> articlePageList = articleMapper.selectPage(setterPage(page, pageSize), setCondition(category, keyword));
        return setterPagedGrid(articlePageList);
    }


    @Override
    public List<Article> queryHotArticleList() {
        Page<Article> articlePage = articleMapper.selectPage(setterPage(1, 5), setCondition(null, null));
        return articlePage.getRecords();
    }


    @Override
    public PagedGridResult queryArticleListOfWriter(String writerId, Integer page, Integer pageSize) {
        Page<Article> articlePage = articleMapper.selectPage(setterPage(page, pageSize), Wrappers.<Article>lambdaQuery()
                .eq(Article::getPublishUserId, writerId));
        return setterPagedGrid(articlePage);
    }

    @Override
    public PagedGridResult queryGoodArticleListOfWriter(String writerId) {
        Page<Article> articlePage = articleMapper.selectPage(setterPage(1, 5), Wrappers.<Article>lambdaQuery()
                .eq(Article::getPublishUserId, writerId).orderByDesc(Article::getPublishTime));
        return setterPagedGrid(articlePage);
    }

    @Override
    public ArticleDetailVO queryDetail(String articleId) {
        Article article = new Article();
        article.setId(articleId);
        article.setIsAppoint(YesOrNo.NO.type);
        article.setIsDelete(YesOrNo.NO.type);
        article.setArticleStatus(ArticleReviewStatus.SUCCESS.type);

        Article result = articleMapper.selectOne(Wrappers.<Article>lambdaQuery().eq(Article::getId, articleId)
                .eq(Article::getIsAppoint, YesOrNo.NO.type)
                .eq(Article::getIsDelete, YesOrNo.NO.type).eq(Article::getArticleStatus, ArticleReviewStatus.SUCCESS.type));
        ArticleDetailVO detailVO =ArticleDetailVO.builder().build();
        BeanUtils.copyProperties(result, detailVO);
        detailVO.setCover(result.getArticleCover());
        return detailVO;
    }


    public LambdaQueryWrapper<Article> setCondition(Integer category, String keyword) {

        /**
         * 查询首页文章的自带隐性查询条件
         * isApoint = 即时发布,表示文章已经直接发布,或者定时任务到点发布
         * isDelete = 未删除,表示文章只能够显示未删除
         * articleStatus = 审核通过,表示只有文章经过机审/人工审核之后才能展示
         */
        LambdaQueryWrapper<Article> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Article::getIsAppoint, YesOrNo.NO.type);
        queryWrapper.eq(Article::getIsDelete, YesOrNo.NO.type);
        queryWrapper.eq(Article::getArticleStatus, ArticleReviewStatus.SUCCESS.type);
        queryWrapper.eq(ObjectUtils.isNotEmpty(category), Article::getCategoryId, category);
        queryWrapper.like(StringUtils.isNotBlank(keyword), Article::getTitle, keyword);
        queryWrapper.orderByDesc(Article::getPublishTime);
        return queryWrapper;
    }
}

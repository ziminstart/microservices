package com.imooc.article.service;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.api.service.IBaseService;
import com.imooc.article.iservice.IArticleService;
import com.imooc.article.mapper.ArticleMapper;
import com.imooc.enums.ArticleAppointType;
import com.imooc.enums.ArticleReviewLevel;
import com.imooc.enums.ArticleReviewStatus;
import com.imooc.enums.YesOrNo;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.model.bo.NewArticleBO;
import com.imooc.model.pojo.Article;
import com.imooc.model.pojo.Category;
import com.imooc.utils.PagedGridResult;
import com.imooc.utils.extend.AliTextReviewUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 * 文章资讯表 服务实现类
 * </p>
 *
 * @author 恒利
 * @since 2020-12-09
 */
@Service
public class ArticleBiz extends ServiceImpl<ArticleMapper, Article> implements IArticleService, IBaseService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private AliTextReviewUtils aliTextReviewUtils;
    @Autowired
    private Sid sid;

    @Transactional
    @Override
    public void createArticle(NewArticleBO newArticleBO, Category category) {
        String id = sid.nextShort();
        Article article = new Article();
        BeanUtils.copyProperties(newArticleBO, article);
        article.setId(id);
        article.setCategoryId(category.getId());
        article.setArticleStatus(ArticleReviewStatus.REVIEWING.type);
        article.setCommentCounts(0);
        article.setReadCounts(0);
        article.setIsDelete(YesOrNo.NO.type);
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        if (article.getIsAppoint().equals(ArticleAppointType.TIMING.type)) {
            article.setPublishTime(newArticleBO.getPublishTime());
        } else if (article.getIsAppoint().equals(ArticleAppointType.IMMEDIATELY.type)) {
            article.setPublishTime(LocalDateTime.now());
        }
        int res = articleMapper.insert(article);
        if (res != 1) {
            GraceException.display(ResponseStatusEnum.ARTICLE_CREATE_ERROR);
        }
        //通过对阿里ai对文本内容进行自动检测(自动审核)
//        String reviewTextContentResult = aliTextReviewUtils.reviewTextContent(newArticleBO.getContent());
        String reviewTextContentResult = ArticleReviewLevel.REVIEW.type;//= aliTextReviewUtils.reviewTextContent(newArticleBO.getContent());
        //通过
        if (reviewTextContentResult.equalsIgnoreCase(ArticleReviewLevel.PASS.type)) {
            this.updateArticleStatus(article.getId(), ArticleReviewStatus.SUCCESS.type);
            //修改当前的文章，状态标记为审核通过
        } else if (reviewTextContentResult.equalsIgnoreCase(ArticleReviewLevel.REVIEW.type)) {
            this.updateArticleStatus(article.getId(), ArticleReviewStatus.WAITING_MANUAL.type);
            //修改当前的文章，状态标记为需要人工审核
        } else if (reviewTextContentResult.equalsIgnoreCase(ArticleReviewLevel.BLOCK.type)) {
            //修改当前的文章，状态标记为审核未通过
            this.updateArticleStatus(article.getId(), ArticleReviewStatus.FAILED.type);
        }

    }

    @Transactional
    @Override
    public void updateAppointToPublish() {
        articleMapper.updateAppointToPublish();
    }

    @Override
    public PagedGridResult queryMyArticleList(String userId,
                                              String keyword,
                                              Integer status,
                                              String startDate,
                                              String endDate,
                                              Integer page,
                                              Integer pageSize) {
        Page<Article> articlePage = new Page<>();
        articlePage.setCurrent(page);
        articlePage.setSize(pageSize);
        Page<Article> articlePageList = articleMapper.selectPage(articlePage, Wrappers.<Article>lambdaQuery()
                .eq(Article::getPublishUserId, userId)
                .eq(ArticleReviewStatus.isArticleStatusValid(status), Article::getArticleStatus, status)
                .ge(StringUtils.isNotBlank(startDate), Article::getPublishTime, startDate)
                .le(StringUtils.isNotBlank(endDate), Article::getPublishTime, endDate)
                .eq(Article::getIsDelete, YesOrNo.NO.type)
                .like(StringUtils.isNotBlank(keyword), Article::getTitle, keyword)
                .orderByDesc(Article::getCreateTime)
        );
        return setterPagedGrid(articlePageList);
    }

    @Override
    public void updateArticleStatus(String articleId, Integer pendingStatus) {
        int update = articleMapper.update(new Article(), Wrappers.<Article>lambdaUpdate()
                .set(Article::getArticleStatus, pendingStatus).eq(Article::getId, articleId));

    }
}

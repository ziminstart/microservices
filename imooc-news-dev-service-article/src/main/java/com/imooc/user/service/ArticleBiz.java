package com.imooc.user.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.model.pojo.Article;
import com.imooc.user.iservice.IArticleService;
import com.imooc.user.mapper.ArticleMapper;
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
public class ArticleBiz extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

}

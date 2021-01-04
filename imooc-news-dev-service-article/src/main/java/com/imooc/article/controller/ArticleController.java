package com.imooc.article.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.imooc.api.BaseController;
import com.imooc.api.controller.article.IArticleControllerApi;
import com.imooc.api.service.IBaseService;
import com.imooc.enums.ArticleCoverType;
import com.imooc.exception.MyCustomException;
import com.imooc.grace.result.R;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.model.bo.NewArticleBO;
import com.imooc.model.pojo.Category;
import com.imooc.article.iservice.IArticleService;
import com.imooc.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class ArticleController extends BaseController implements IBaseService, IArticleControllerApi {

    @Autowired
    private IArticleService articleService;

    @Override
    public R createArticle(@Valid NewArticleBO newArticleBO,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            Map<String, String> errors = getErrors(bindingResult);
            return R.errorMap(errors);
        }
        //判断文章封面类型,单图必填,纯文字则设置为空
         if(newArticleBO.getArticleType().equals(ArticleCoverType.ONE_IMAGE.type)){
             if (StringUtils.isBlank(newArticleBO.getArticleCover())){
                 return R.errorCustom(ResponseStatusEnum.ARTICLE_COVER_NOT_EXIST_ERROR);
             }
         } else if (newArticleBO.getArticleType().equals(ArticleCoverType.WORDS.type)) {
             newArticleBO.setArticleCover("");
         }
         //判断分类id是否存在
        String allCategoryJson = redis.get(REDIS_ALL_CATEGORY);
         if(StringUtils.isBlank(allCategoryJson)){
             return R.errorCustom(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
         } else {
             List<Category>  categoryList  = JsonUtils.jsonToList(allCategoryJson,Category.class);
             assert categoryList != null;
             Category cat = categoryList.parallelStream()
                     .filter(category -> category.getId().equals(newArticleBO.getCategoryId()))
                     .findFirst()
                     .orElseThrow(() -> new MyCustomException(ResponseStatusEnum.SYSTEM_RESPONSE_NO_INFO));

             articleService.createArticle(newArticleBO,cat);
         }
        return R.ok();
    }
}

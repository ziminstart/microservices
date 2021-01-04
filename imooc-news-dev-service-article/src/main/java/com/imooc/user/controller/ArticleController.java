package com.imooc.user.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.article.IArticleControllerApi;
import com.imooc.api.service.IBaseService;
import com.imooc.grace.result.R;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleController extends BaseController implements IBaseService, IArticleControllerApi {
    @Override
    public R hello() {
        System.out.println();
        return null;
    }
}

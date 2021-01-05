package com.imooc.article.controller;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.imooc.api.BaseController;
import com.imooc.api.controller.article.IArticleControllerApi;
import com.imooc.api.controller.article.IArticlePortalControllerApi;
import com.imooc.api.service.IBaseService;
import com.imooc.article.iservice.IArticlePortalService;
import com.imooc.article.iservice.IArticleService;
import com.imooc.enums.ArticleCoverType;
import com.imooc.enums.ArticleReviewStatus;
import com.imooc.enums.YesOrNo;
import com.imooc.exception.MyCustomException;
import com.imooc.grace.result.R;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.model.bo.NewArticleBO;
import com.imooc.model.pojo.Article;
import com.imooc.model.pojo.Category;
import com.imooc.model.vo.AppUserVO;
import com.imooc.model.vo.IndexArticleVO;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.PagedGridResult;
import io.swagger.models.auth.In;
import net.sf.jsqlparser.statement.create.table.Index;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
public class ArticlePortalController extends BaseController implements IBaseService, IArticlePortalControllerApi {

    @Autowired
    private IArticlePortalService articlePortalService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public R queryAllList(String keyword, Integer category, Integer page, Integer pageSize) {
        if (page == null) {
            page = COMMON_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult pagedGridResult = articlePortalService.queryIndexArticleList(keyword, category, page, pageSize);
        // START
        List<Article> rows = (List<Article>) pagedGridResult.getRows();
        Set<String> publishIdSet = rows.parallelStream().map(Article::getPublishUserId).collect(Collectors.toSet());

        //发起远程调用(restTemplate),请求用户服务获得用户(idSet,发布者)的列表
        String userServerUrlExecute = "http://user.imoocnews.com:8003/user/queryByIds?userIds=" + JsonUtils.objectToJson(publishIdSet);
        ResponseEntity<R> responseEntity = restTemplate.getForEntity(userServerUrlExecute, R.class);
        R response = responseEntity.getBody();
        List<AppUserVO> publishUserList = new ArrayList<>();
        if (response.getStatus() == 200) {
            String userVoJson = JsonUtils.objectToJson(response.getData());
            publishUserList = JsonUtils.jsonToList(userVoJson, AppUserVO.class);
        }
        Map<String, AppUserVO> collect = publishUserList.parallelStream().collect(Collectors.toMap(AppUserVO::getId, appUserVO -> appUserVO));
        //拼接两个list,重组文章列表
        List<IndexArticleVO> result = rows.parallelStream()
                .map(article -> setFunction(article,collect)).collect(Collectors.toList());
        pagedGridResult.setRows(result);
        //END
        return R.ok(pagedGridResult);
    }

    private IndexArticleVO setFunction(Article article, Map<String, AppUserVO> appUserVOMap){
        IndexArticleVO indexArticleVO = new IndexArticleVO();
        BeanUtils.copyProperties(article, indexArticleVO);
        if (ObjectUtils.isNotEmpty(appUserVOMap.get(article.getPublishUserId()))){
            AppUserVO userVO = appUserVOMap.get(article.getPublishUserId());
            indexArticleVO.setAppUserVO(userVO);
        }
        return indexArticleVO;
    }
}

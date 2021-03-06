package com.imooc.article.controller;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.imooc.api.BaseController;
import com.imooc.api.controller.article.IArticlePortalControllerApi;
import com.imooc.article.iservice.IArticlePortalService;
import com.imooc.grace.result.R;
import com.imooc.model.pojo.Article;
import com.imooc.model.vo.AppUserVO;
import com.imooc.model.vo.ArticleDetailVO;
import com.imooc.model.vo.IndexArticleVO;
import com.imooc.utils.IPUtil;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.PagedGridResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class ArticlePortalController extends BaseController implements   IArticlePortalControllerApi {

    @Autowired
    private IArticlePortalService articlePortalService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public R queryArticleListOfWriter(String writerId, Integer page, Integer pageSize) {

        System.out.println("writerId=" + writerId);

        if (page == null) {
            page = COMMON_START_PAGE;
        }

        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult gridResult = articlePortalService.queryArticleListOfWriter(writerId, page, pageSize);
        gridResult = rebuildArticleGrid(gridResult);
        return R.ok(gridResult);
    }

    @Override
    public R queryGoodArticleListOfWriter(String writerId) {
        PagedGridResult gridResult = articlePortalService.queryGoodArticleListOfWriter(writerId);
        return R.ok(gridResult);
    }

    @Override
    public R detail(String articleId) {
        ArticleDetailVO detailVO = articlePortalService.queryDetail(articleId);

        Set<String> idSet = new HashSet();
        idSet.add(detailVO.getPublishUserId());
        List<AppUserVO> publisherList = getPublisherList(idSet);

        if (!publisherList.isEmpty()) {
            detailVO.setPublishUserName(publisherList.get(0).getNickname());
        }

        detailVO.setReadCounts(getCountsFromRedis(REDIS_ARTICLE_READE_COUNTS + ":" + articleId));

        return R.ok(detailVO);
    }


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

        //??????????????????(restTemplate),??????????????????????????????(idSet,?????????)?????????
        String userServerUrlExecute = "http://user.imoocnews.com:8003/user/queryByIds?userIds=" + JsonUtils.objectToJson(publishIdSet);
        ResponseEntity<R> responseEntity = restTemplate.getForEntity(userServerUrlExecute, R.class);
        R response = responseEntity.getBody();
        List<AppUserVO> publishUserList = new ArrayList<>();
        if (response.getStatus() == 200) {
            String userVoJson = JsonUtils.objectToJson(response.getData());
            publishUserList = JsonUtils.jsonToList(userVoJson, AppUserVO.class);
        }
        Map<String, AppUserVO> collect = publishUserList.parallelStream().collect(Collectors.toMap(AppUserVO::getId, appUserVO -> appUserVO));
        //????????????list,??????????????????
        List<IndexArticleVO> result = rows.parallelStream()
                .map(article -> setFunction(article,collect)).collect(Collectors.toList());
        pagedGridResult.setRows(result);
        //END
        return R.ok(pagedGridResult);
    }

    @Override
    public R hotList() {
        return  R.ok(articlePortalService.queryHotArticleList());
    }

    @Override
    public R readArticle(String articleId, HttpServletRequest request) {
        String requestIp = IPUtil.getRequestIp(request);
        //????????????????????????ip??????????????????redis???,?????????redis????????????ip??????????????????????????????????????????????????????
        redis.setnx(REDIS_ALREADY_READ + ":"  + articleId + ":" + requestIp ,requestIp);
        redis.increment(REDIS_ARTICLE_READE_COUNTS + ":" + articleId,1);
        return R.ok();
    }

    private IndexArticleVO setFunction(Article article, Map<String, AppUserVO> appUserVOMap){
        IndexArticleVO indexArticleVO = new IndexArticleVO();
        BeanUtils.copyProperties(article, indexArticleVO);
        if (ObjectUtils.isNotEmpty(appUserVOMap.get(article.getPublishUserId()))){
            AppUserVO userVO = appUserVOMap.get(article.getPublishUserId());
            indexArticleVO.setPublisherVO(userVO);
        }
        return indexArticleVO;
    }


    private PagedGridResult rebuildArticleGrid(PagedGridResult gridResult) {
        // START

        List<Article> list = (List<Article>)gridResult.getRows();

        // 1. ???????????????id??????
        Set<String> idSet = new HashSet<>();
        List<String> idList = new ArrayList<>();
        for (Article a : list) {
//            System.out.println(a.getPublishUserId());
            // 1.1 ??????????????????set
            idSet.add(a.getPublishUserId());
            // 1.2 ????????????id???list
            idList.add(REDIS_ARTICLE_READE_COUNTS+ ":" + a.getId());
        }
        System.out.println(idSet.toString());
        // ??????redis???mget????????????api?????????????????????
        List<String> readCountsRedisList = redis.mget(idList);
        List<AppUserVO> publisherList = getPublisherList(idSet);
        // 3. ????????????list?????????????????????
        List<IndexArticleVO> indexArticleList = new ArrayList<>();
        for (int i = 0 ; i < list.size() ; i ++) {
            IndexArticleVO indexArticleVO = new IndexArticleVO();
            Article a = list.get(i);
            BeanUtils.copyProperties(a, indexArticleVO);

            // 3.1 ???publisherList?????????????????????????????????
            AppUserVO publisher  = getUserIfPublisher(a.getPublishUserId(), publisherList);
            indexArticleVO.setPublisherVO(publisher);

            // 3.2 ?????????????????????????????????????????????
            String redisCountsStr = readCountsRedisList.get(i);
            int readCounts = 0;
            if (StringUtils.isNotBlank(redisCountsStr)) {
                readCounts = Integer.valueOf(redisCountsStr);
            }
            indexArticleVO.setReadCounts(readCounts);

            indexArticleList.add(indexArticleVO);
        }


        gridResult.setRows(indexArticleList);
// END
        return gridResult;
    }


    private AppUserVO getUserIfPublisher(String publisherId,
                                         List<AppUserVO> publisherList) {
        for (AppUserVO user : publisherList) {
            if (user.getId().equalsIgnoreCase(publisherId)) {
                return user;
            }
        }
        return null;
    }



    // ????????????????????????????????????????????????
    private List<AppUserVO> getPublisherList(Set idSet) {
        String userServerUrlExecute
                = "http://user.imoocnews.com:8003/user/queryByIds?userIds=" + JsonUtils.objectToJson(idSet);
        ResponseEntity<R> responseEntity
                = restTemplate.getForEntity(userServerUrlExecute, R.class);
        R bodyResult = responseEntity.getBody();
        List<AppUserVO> publisherList = null;
        if (bodyResult.getStatus() == 200) {
            String userJson = JsonUtils.objectToJson(bodyResult.getData());
            publisherList = JsonUtils.jsonToList(userJson, AppUserVO.class);
        }
        return publisherList;
    }


}

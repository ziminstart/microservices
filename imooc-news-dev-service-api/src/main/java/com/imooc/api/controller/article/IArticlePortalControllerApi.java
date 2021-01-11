package com.imooc.api.controller.article;


import com.imooc.grace.result.R;
import com.imooc.model.bo.NewArticleBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author zimin
 * @function: api的作用
 * <p>
 * api 就相当于企业的领导，老板，部门经理
 * 其他的服务层都是实现，他们就相当于员工，只做事情
 * 所有的api接口就是统一在这理进行管理和调度的，微服务也是如此
 * <p>
 * 运作:
 * 现在所有的接口都在些暴露，实现都是在各自的微服务中
 * 本项目只写接口，不写实现，实现在各自的微服务工程中，因为以业务来划分的微服务有很多
 * controller也会在分散在各个微服务中，一旦多了就很难统一管理和查看
 * <p>
 * 其次，微服务之间的调用都是基于接口的
 * 如果不这么做，微服务之间的调用就需要相互依赖了
 * 耦合度也很高了，接口的目的为了能够提供解耦
 * <p>
 * 此外，本工程的接口其实就是一套规范。实现都是由各自的工程去做的处理
 */
@Api(value = "文章业务的controller", tags = {"文章业务的controller"})
@RequestMapping("portal/article")
public interface IArticlePortalControllerApi {


    @ApiOperation(value = "首页查询文章列表", notes = "首页查询文章列表", httpMethod = "GET")
    @GetMapping("/list")
    R queryAllList(@RequestParam String keyword,
                    @RequestParam Integer category,
                    @RequestParam Integer page,
                    @RequestParam Integer pageSize);



    @GetMapping("queryArticleListOfWriter")
    @ApiOperation(value = "查询作家发布的所有文章列表", notes = "查询作家发布的所有文章列表", httpMethod = "GET")
    R queryArticleListOfWriter(@RequestParam String writerId,
                                                    @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
                                                    @RequestParam Integer page,
                                                    @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
                                                    @RequestParam Integer pageSize);

    @GetMapping("queryGoodArticleListOfWriter")
    @ApiOperation(value = "作家页面查询近期佳文", notes = "作家页面查询近期佳文", httpMethod = "GET")
    R queryGoodArticleListOfWriter(@RequestParam String writerId);

    @GetMapping("detail")
    @ApiOperation(value = "文章详情查询", notes = "文章详情查询", httpMethod = "GET")
    R detail(@RequestParam String articleId);

    @ApiOperation(value = "首页查询热闻列表", notes = "首页查询热闻列表", httpMethod = "GET")
    @GetMapping("/hotList")
    R hotList();



    @ApiOperation(value = "阅读文章，文章阅读量累加", notes = "阅读文章，文章阅读量累加", httpMethod = "POST")
    @PostMapping("/readArticle")
    R readArticle(@RequestParam String articleId, HttpServletRequest request);


}



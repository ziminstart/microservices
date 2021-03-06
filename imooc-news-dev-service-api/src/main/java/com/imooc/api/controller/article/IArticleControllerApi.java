package com.imooc.api.controller.article;


import com.imooc.grace.result.R;
import com.imooc.model.bo.NewArticleBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("article")
public interface IArticleControllerApi {


    @ApiOperation(value = "创建文章的接口", notes = "创建文章的接口", httpMethod = "POST")
    @PostMapping("/createArticle")
    R createArticle(@RequestBody @Valid NewArticleBO newArticleBO, BindingResult bindingResult);


    @ApiOperation(value = "查询用户所有的文章列表", notes = "查询用户所有的文章列表", httpMethod = "POST")
    @PostMapping("/queryMyList")
    R queryMyList(@RequestParam String userId,
                  @RequestParam String keyword,
                  @RequestParam Integer status,
                  @RequestParam String startDate,
                  @RequestParam String endDate,
                  @RequestParam Integer page,
                  @RequestParam Integer pageSize);


    @ApiOperation(value = "管理员对文章审核通过或者失败", notes = "管理员对文章审核通过或者失败", httpMethod = "POST")
    @PostMapping("/doReview")
    R doReview(@RequestParam String articleId,
               @RequestParam Integer passOrNot);



}

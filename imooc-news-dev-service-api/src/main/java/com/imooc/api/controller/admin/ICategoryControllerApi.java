package com.imooc.api.controller.admin;


import com.imooc.grace.result.R;
import com.imooc.model.pojo.Category;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zimin
 * @function:
 * api的作用
 *
 * api 就相当于企业的领导，老板，部门经理
 * 其他的服务层都是实现，他们就相当于员工，只做事情
 * 所有的api接口就是统一在这理进行管理和调度的，微服务也是如此
 *
 * 运作:
 *  现在所有的接口都在些暴露，实现都是在各自的微服务中
 *  本项目只写接口，不写实现，实现在各自的微服务工程中，因为以业务来划分的微服务有很多
 *  controller也会在分散在各个微服务中，一旦多了就很难统一管理和查看
 *
 *  其次，微服务之间的调用都是基于接口的
 *  如果不这么做，微服务之间的调用就需要相互依赖了
 *  耦合度也很高了，接口的目的为了能够提供解耦
 *
 *  此外，本工程的接口其实就是一套规范。实现都是由各自的工程去做的处理
 */
@Api(value = "文章分类controller的api",tags = {"文章功能的controller"})
@RequestMapping("/categoryMng")
public interface ICategoryControllerApi {


    /**
     *
     * @return
     */
    @ApiOperation(value = "获取文章分类列表",notes = "获取文章分类列表",httpMethod = "POST")
    @PostMapping("/getCatList")
    R getCatList();


    @ApiOperation(value = "获取文章分类列表",notes = "获取文章分类列表",httpMethod = "POST")
    @PostMapping("/saveOrUpdateCategory")
    R saveOrUpdateCategory(@RequestBody Category category);

}

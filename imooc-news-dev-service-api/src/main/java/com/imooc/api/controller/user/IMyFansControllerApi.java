package com.imooc.api.controller.user;


import com.imooc.grace.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
@Api(value = "粉丝管理", tags = {"粉丝管理的controller"})
@RequestMapping("fans")
public interface IMyFansControllerApi {


    /**
     * @return
     */
    @ApiOperation(value = "查询当前用户是否关注作家", notes = "查询当前用户是否关注作家", httpMethod = "POST")
    @PostMapping("/isMeFollowThisWriter")
    R isMeFollowThisWriter(@RequestParam String writerId,
                           @RequestParam String fanId);


    @ApiOperation(value = "用户关注作家成为粉丝", notes = "用户关注作家成为粉丝", httpMethod = "POST")
    @PostMapping("/follow")
    R follow(@RequestParam String writerId,
             @RequestParam String fanId);

    @ApiOperation(value = "用户取消关注,作家损失粉丝", notes = "用户取消关注,作家损失粉丝", httpMethod = "POST")
    @PostMapping("/unfollow")
    R unfollow(@RequestParam String writerId,
               @RequestParam String fanId);


    @ApiOperation(value = "查询所有的粉丝列表", notes = "查询所有的粉丝列表", httpMethod = "POST")
    @PostMapping("/queryAll")
    R queryAll(@RequestParam String writerId,@RequestParam Integer page,@RequestParam Integer pageSize);


    @ApiOperation(value = "查询男女粉丝数量", notes = "查询男女粉丝数量", httpMethod = "POST")
    @PostMapping("/queryRatio")
    R queryRatio(@RequestParam String writerId);


    @ApiOperation(value = "查询地域男女粉丝数量", notes = "查询地域男女粉丝数量", httpMethod = "POST")
    @PostMapping("/queryRatioByRegion")
    R queryRatioByRegion(@RequestParam String writerId);


}

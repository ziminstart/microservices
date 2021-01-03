package com.imooc.api.interceptors;

import com.imooc.enums.UserStatus;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.model.pojo.AppUser;
import com.imooc.utils.JsonUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器
 * 用户激活状态检查拦截器
 * 发文章，修改文章，删除文章
 * 发表评论，查看评论等等
 * 这些接口都是需要在用户激活以后，才能进行
 * 否则需要提示用户前往「账号设置」去修改信息
 * @author zimin
 */
public class UserActiveInterceptor extends BaseInterceptor implements HandlerInterceptor {

    /**
     * 进入方法之前进行拦截
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId= request.getHeader("headerUserId");
        String userJson  = redis.get(REDIS_USER_INFO + ":" + userId);
        AppUser user = null;
        if (!StringUtils.isEmpty(userJson)){
            user = JsonUtils.jsonToPojo(userJson, AppUser.class);
        } else {
            GraceException.display(ResponseStatusEnum.USER_INACTIVE_ERROR);
            return false;
        }

        if (user.getActiveStatus() == null || !user.getActiveStatus().equals(UserStatus.ACTIVE.type)){
            GraceException.display(ResponseStatusEnum.USER_INACTIVE_ERROR);
            return false;
        }
        return true;
    }

    /**
     * 在请求访问到controller渲染视图之前
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 访问到controller之后，渲染视图之后
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}

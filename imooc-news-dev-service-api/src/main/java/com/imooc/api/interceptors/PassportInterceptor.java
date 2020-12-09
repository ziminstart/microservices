package com.imooc.api.interceptors;

import com.imooc.exception.GraceException;
import com.imooc.exception.MyCustomException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.utils.IPUtil;
import com.imooc.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器
 *
 * @author zimin
 */
public class PassportInterceptor implements HandlerInterceptor {


    /**
     * 注入redis
     */
    @Autowired
    public RedisOperator redisOperator;
    /**
     * 创建一个保存redis key的工具类
     */
    public static final String MOBILE_SMS_CODE = "mobile:smscode";

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
        String userIp = IPUtil.getRequestIp(request);
        boolean keyIsExist = redisOperator.keyIsExist(MOBILE_SMS_CODE + ":" + userIp);
        if (keyIsExist){
            GraceException.display(ResponseStatusEnum.SMS_NEED_WAIT_ERROR);
            //TODO
            return false;
        }
        // false 代表请求被拦截,true,请求通过验证，放行
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

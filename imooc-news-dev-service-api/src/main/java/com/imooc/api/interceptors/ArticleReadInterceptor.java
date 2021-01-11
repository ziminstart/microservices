package com.imooc.api.interceptors;

import com.imooc.utils.IPUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : 恒利
 * @date : 2021/1/11 8:12 下午
 * @function :
 */
public class ArticleReadInterceptor extends BaseInterceptor implements HandlerInterceptor {

    public static final String  REDIS_ALREADY_READ = "redis_already_read";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String articleId = request.getParameter("articleId");
        String requestIp = IPUtil.getRequestIp(request);
        //设置针对当前用户ip的永久存在在redis中,存入到redis，表示该ip的用户已经阅读过了，就不会再次累加了
        redis.setnx(REDIS_ALREADY_READ + ":"  + articleId + ":" + requestIp ,requestIp);
        boolean isExist = redis.keyIsExist(REDIS_ALREADY_READ  + ":" + requestIp);
        if(isExist){
            return false;
        }
        return true;
    }

}

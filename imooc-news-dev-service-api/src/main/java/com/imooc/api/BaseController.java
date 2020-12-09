package com.imooc.api;

import com.imooc.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 恒利
 */
public class BaseController {

    /**
     * 注入redis
     */
    @Autowired
    public RedisOperator redis;
    /**
     * 创建一个保存redis key的工具类
     */
    public static final String MOBILE_SMS_CODE = "mobile:smscode";

    /**
     * redis token
     */
    public static final String REDIS_USER_TOKEN = "redis_user_token";

    public static final Integer COOKIE_MONTH = 30 * 24 * 60 * 60;


    /**
     * 获取BO中的错误信息
     *
     * @param result
     */
    public Map<String, String> getErrors(BindingResult result) {
        Map<String, String> map = new HashMap<>();
        List<FieldError> errors = result.getFieldErrors();
        errors.parallelStream().forEach(fieldError -> {
            //发送验证错误的时候所对应的属性
            String field = fieldError.getField();
            //验证的错误消息
            String defaultMessage = fieldError.getDefaultMessage();
            map.put(field, defaultMessage);
        });
        return map;
    }

    public void setCookie(HttpServletRequest request,
                          HttpServletResponse response,
                          String cookieName,
                          String cookieValue,
                          Integer maxAge) {
        try {
            cookieValue = URLEncoder.encode(cookieValue, "utf-8");
            setCookieValue(request, response, cookieName, cookieValue, maxAge);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void setCookieValue(HttpServletRequest request,
                               HttpServletResponse response,
                               String cookieName,
                               String cookieValue,
                               Integer maxAge) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setMaxAge(maxAge);
        cookie.setDomain("imoocnews.com");
        cookie.setPath("/");
        response.addCookie(cookie);
    }

}

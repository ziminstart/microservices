package com.imooc.api;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.imooc.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    public static final String REDIS_USER_INFO = "redis_user_info";
    public static final String REDIS_ADMIN_TOKEN= "redis_admin_token";
    public static final String REDIS_ALL_CATEGORY= "redis_all_category";

    public static final String  REDIS_WRITER_FANS_COUNTS = "redis_writer_fans_counts";
    public static final String  REDIS_MY_FOLLOW_COUNTS = "redis_my_follow_counts";

    public static final String  REDIS_ARTICLE_READE_COUNTS = "redis_article_reade_counts";
    public static final String  REDIS_ALREADY_READ = "redis_already_read";

    public static final Integer COOKIE_MONTH = 30 * 24 * 60 * 60;
    public static final Integer COOKIE_DELETE= 0;


    public static final Integer COMMON_START_PAGE= 1;
    public static final Integer COMMON_PAGE_SIZE= 10;

    @Value("${website.domain-name}")
    public String DOMAIN_NAME;


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
//        cookie.setDomain("imoocnews.com");
        cookie.setDomain(DOMAIN_NAME);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 删除cookie
     * @param request
     * @param response
     * @param cookieName
     */
    public void deleteCookie(HttpServletRequest request,HttpServletResponse response,String cookieName){
        try {
            String deleteValue = URLEncoder.encode("","utf-8");
            setCookieValue(request,response,cookieName,deleteValue,COOKIE_DELETE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public Integer getCountsFromRedis(String key){
        String countsStr = redis.get(key);
        if (StringUtils.isNotBlank(key )){
            countsStr = "0";
        } else {
            countsStr  = redis.get(key);
        }

        return Integer.valueOf(countsStr);
    }
}

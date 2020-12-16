package com.imooc.api.interceptors;

import com.imooc.exception.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * @author zimin
 * @function 共公的interceptor方法
 */
public class BaseInterceptor {

    @Autowired
    public RedisOperator redis;

    public static final String REDIS_USER_TOKEN = "redis_user_token";
    public static final String REDIS_USER_INFO = "redis_user_info";


    public boolean verifyUserIdToken(String userId, String token, String redisKeyPrefix) {
        if (!StringUtils.isEmpty(userId) && !StringUtils.isEmpty(token)) {
            String redisToken = redis.get(redisKeyPrefix + ":" + userId);
            if (StringUtils.isEmpty(redisToken)) {
                GraceException.display(ResponseStatusEnum.UN_LOGIN);
                return false;
            } else {
                if (!redisToken.equalsIgnoreCase(token)) {
                    GraceException.display(ResponseStatusEnum.TICKET_INVALID);
                    return false;
                }
            }
        } else {
            GraceException.display(ResponseStatusEnum.UN_LOGIN);
            return false;
        }
        return true;
    }

}

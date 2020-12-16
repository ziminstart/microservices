package com.imooc.api.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author zimin
 * @function service切面
 */
@Aspect
@Component
@Slf4j
public class ServiceLogAspect {


    /**
     * AOP通知
     * 1. 前置通知
     * 2. 后置通知
     * 3. 环绕通知
     * 4. 异常通知
     * 5. 最终通知
     */
    @Around("execution( * com.imooc.*.service..*.*(..))")
    public Object recordTimeOfService(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("========开始执行{}.{}========", joinPoint.getTarget().getClass(), joinPoint.getSignature().getName());
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        long takeTime = end - start;
        if (takeTime > 3000) {
            log.error("当前执行耗时:{}", takeTime);
        } else if (takeTime > 2000) {
            log.warn("当前执行耗时:{}", takeTime);
        } else {
            log.info("当前执行耗时:{}", takeTime);
        }
        return result;
    }

}

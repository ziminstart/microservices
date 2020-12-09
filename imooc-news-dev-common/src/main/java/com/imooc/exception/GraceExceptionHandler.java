package com.imooc.exception;

import com.imooc.grace.result.R;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zimin
 * @function: 统一异常拦截，可以针对异常的类型捕获，然后返回json信息到前端
 */
@ControllerAdvice
public class GraceExceptionHandler {

    @ResponseBody
    @ExceptionHandler(MyCustomException.class)
    public R returnMyException(MyCustomException e) {
        //打印错误信息
        e.printStackTrace();
        return R.exception(e.getResponseStatusEnum());
    }

}

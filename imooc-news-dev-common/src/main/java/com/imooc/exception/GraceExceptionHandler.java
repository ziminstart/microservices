package com.imooc.exception;

import com.imooc.grace.result.R;
import com.imooc.grace.result.ResponseStatusEnum;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * @author zimin
 * @function: 统一异常拦截，可以针对异常的类型捕获，然后返回json信息到前端
 */
@RestControllerAdvice
public class GraceExceptionHandler {

    @ExceptionHandler(MyCustomException.class)
    public R returnMyException(MyCustomException e) {
        //打印错误信息
        e.printStackTrace();
        return R.exception(e.getResponseStatusEnum());
    }


    /**
     * 图片大小控制的统一异常
     * @param e
     * @return
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public R returnMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        //打印错误信息
        return R.errorCustom(ResponseStatusEnum.FILE_MAX_SIZE_ERROR);
    }

}

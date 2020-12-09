package com.imooc.exception;

import com.imooc.grace.result.ResponseStatusEnum;

/**
 * 统一封装自定义异常
 * @author zimin
 */
public class GraceException {

    public static  void display(ResponseStatusEnum responseStatusEnum){
        throw new MyCustomException(responseStatusEnum);
    }
}

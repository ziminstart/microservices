package com.imooc.api.function;

import org.springframework.beans.BeanUtils;

public class InjectFunction<T, R> {

    public R setFunction(T t, Class<?> clazz) {
        try {
            Object o = clazz.newInstance();
            BeanUtils.copyProperties(t, o);
            return (R) o;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

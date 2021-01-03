package com.imooc.admin.controller;

import com.imooc.api.controller.user.IHelloControllerApi;
import com.imooc.grace.result.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zimin
 */
@RestController
public class HelloController implements IHelloControllerApi {

    final static Logger logger = LoggerFactory.getLogger(HelloController.class);

    @Override
    public R hello() {
        logger.info("info","hello~");
        logger.warn("warn","hello~");
        logger.error("error","hello~");
        return R.ok();
    }
}

package com.imooc.api.config;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author zimin
 * @function swagger2的配置类
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    // http://localhost:8080/swagger-ui.html 原路径
    // http://localhost:8088/doc.html //新路径

    /**
     * 配置swagger2核心配置
     *
     * @return
     */
    @Bean
    public Docket createRestApi() {
        Predicate<RequestHandler> adminPredicate = RequestHandlerSelectors.basePackage("com.imooc.admin.controller");
//        Predicate<RequestHandler> articlePredicate = RequestHandlerSelectors.basePackage("com.imooc.article.controller");
        Predicate<RequestHandler> userPredicate = RequestHandlerSelectors.basePackage("com.imooc.user.controller");
        Predicate<RequestHandler> filesPredicate = RequestHandlerSelectors.basePackage("com.imooc.files.controller");

        return
                //指定api类型为swagger2
                new Docket(DocumentationType.SWAGGER_2)
                        //用于定义api文档汇总信息
                        .apiInfo(apiInfo())
                        .select()
                        .apis(Predicates.or(userPredicate, adminPredicate, filesPredicate))
//                .apis(Predicates.or(userPredicate,adminPredicate,filesPredicate));
                        //所有的controller
                        .paths(PathSelectors.any())
                        .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //文档标题
                .title("慕课新闻-自媒体接口api")
                //联系人信息
                .contact(new Contact("imooc", "http://www.imooc.com", "ziminstart@gmail.com"))
                //详细信息
                .description("专为慕课新闻-自媒体平台提供的api文档")
                //文档版本号
                .version("1.0.1")
                //网站地址
                .termsOfServiceUrl("http://www.imooc.com")
                .build();
    }

}

package com.imooc.article.task;

import com.imooc.article.iservice.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author zimin
 */
@Configuration
//@EnableScheduling
public class TaskPublishArticles {

    @Autowired
    private IArticleService articleService;

    /**
     * 添加定时任务，注明定时任务的表达式
     */
    @Scheduled(cron = "0/3 * * * * ?")
    private void publishArticles(){
        //调用文章service 把当前时间应该发布的文章改为即时发送
        articleService.updateAppointToPublish();
    }


}

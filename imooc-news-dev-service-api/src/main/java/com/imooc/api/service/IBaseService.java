package com.imooc.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imooc.model.pojo.Article;
import com.imooc.utils.PagedGridResult;
import com.imooc.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.jca.context.SpringContextResourceAdapter;
import sun.security.krb5.Config;

public interface IBaseService {

    String REDIS_WRITER_FANS_COUNTS = "redis_writer_fans_counts";
    String REDIS_MY_FOLLOW_COUNTS = "redis_my_follow_counts";

    /**
     * 设置表格的回显数据
     *
     * @param pageLists
     * @return
     */
    default PagedGridResult setterPagedGrid(Page<?> pageLists) {
        PagedGridResult pagedGridResult = new PagedGridResult();
        pagedGridResult.setRows(pageLists.getRecords());
        pagedGridResult.setPage(pageLists.getCurrent());
        pagedGridResult.setTotal(pageLists.getPages());
        pagedGridResult.setRecords(pageLists.getTotal());
        return pagedGridResult;
    }

    /**
     * 设置分页对象
     *
     * @param current 当前页
     * @param size    一页展示多少条数据
     * @return
     */
    default <T> Page<T> setterPage(Integer current, Integer size) {
        Page<T> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);
        return page;

    }
}

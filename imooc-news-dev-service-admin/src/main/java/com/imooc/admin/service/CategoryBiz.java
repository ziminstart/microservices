package com.imooc.admin.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.admin.iservice.ICategoryService;
import com.imooc.admin.mapper.CategoryMapper;
import com.imooc.model.pojo.Category;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 新闻资讯文章的分类（或者称之为领域） 服务实现类
 * </p>
 *
 * @author 恒利
 * @since 2020-12-09
 */
@Service
public class CategoryBiz extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

}

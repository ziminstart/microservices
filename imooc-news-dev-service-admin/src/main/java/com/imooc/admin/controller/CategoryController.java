package com.imooc.admin.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.imooc.admin.iservice.ICategoryService;
import com.imooc.admin.iservice.IFriendLinkService;
import com.imooc.api.BaseController;
import com.imooc.api.controller.admin.ICategoryControllerApi;
import com.imooc.api.controller.admin.IFriendLinkControllerApi;
import com.imooc.api.service.IBaseService;
import com.imooc.grace.result.R;
import com.imooc.model.bo.SaveFriendLinkBO;
import com.imooc.model.mo.FriendLinkMO;
import com.imooc.model.pojo.Category;
import com.imooc.utils.JsonUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author zimin
 */
@RestController
public class CategoryController extends BaseController implements ICategoryControllerApi, IBaseService {

    @Autowired
    private ICategoryService categoryService;

    @Override
    public R getCatList() {
        List<Category> list = categoryService.list();
        return R.ok(list);
    }

    @Override
    public R getCats() {
        //先从redis中查询,如果有直接返回,如果没有从数据库中查询并放入缓存中
        String allCategoryJson = redis.get(REDIS_ALL_CATEGORY);
        List<Category> categoryList = null;
        if (StringUtils.isBlank(allCategoryJson)){
             categoryList = categoryService.list();
             redis.set(REDIS_ALL_CATEGORY, JsonUtils.objectToJson(categoryList));
        } else {
            categoryList = JsonUtils.jsonToList(allCategoryJson,Category.class);
        }
        return R.ok(categoryList);
    }


    @Transactional
    @Override
    public R saveOrUpdateCategory(Category category) {
        categoryService.saveOrUpdate(category);
        redis.del(REDIS_ALL_CATEGORY);
        return R.ok();
    }
}

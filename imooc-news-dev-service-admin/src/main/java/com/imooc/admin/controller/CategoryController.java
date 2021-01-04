package com.imooc.admin.controller;

import com.imooc.admin.iservice.ICategoryService;
import com.imooc.admin.iservice.IFriendLinkService;
import com.imooc.api.BaseController;
import com.imooc.api.controller.admin.ICategoryControllerApi;
import com.imooc.api.controller.admin.IFriendLinkControllerApi;
import com.imooc.grace.result.R;
import com.imooc.model.bo.SaveFriendLinkBO;
import com.imooc.model.mo.FriendLinkMO;
import com.imooc.model.pojo.Category;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CategoryController extends BaseController implements ICategoryControllerApi {

    @Autowired
    private ICategoryService categoryService;

    @Override
    public R getCatList() {
        List<Category> list = categoryService.list();
        return R.ok(list);
    }

    @Override
    public R saveOrUpdateCategory(Category category) {
        categoryService.saveOrUpdate(category);
        return R.ok();
    }
}

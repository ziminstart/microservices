package com.imooc.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imooc.model.pojo.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 新闻资讯文章的分类（或者称之为领域） Mapper 接口
 * </p>
 *
 * @author 恒利
 * @since 2020-12-09
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}

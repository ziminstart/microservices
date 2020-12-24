package com.imooc.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imooc.files.pojo.AppUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 网站用户 Mapper 接口
 * </p>
 *
 * @author 恒利
 * @since 2020-12-09
 */
@Mapper
public interface AppUserMapper extends BaseMapper<AppUser> {

}

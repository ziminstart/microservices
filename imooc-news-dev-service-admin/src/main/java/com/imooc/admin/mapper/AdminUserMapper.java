package com.imooc.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imooc.model.pojo.AdminUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 运营管理平台的admin级别用户 Mapper 接口
 * </p>
 *
 * @author 恒利
 * @since 2020-12-09
 */
@Mapper
public interface AdminUserMapper extends BaseMapper<AdminUser> {

}

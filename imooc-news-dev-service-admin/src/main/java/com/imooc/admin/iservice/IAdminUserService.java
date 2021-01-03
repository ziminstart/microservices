package com.imooc.admin.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.model.bo.NewAdminBO;
import com.imooc.model.pojo.AdminUser;
import com.imooc.utils.PagedGridResult;

/**
 * <p>
 * 运营管理平台的admin级别用户 服务类
 * </p>
 *
 * @author 恒利
 * @since 2020-12-09
 */
public interface IAdminUserService extends IService<AdminUser> {

    /**
     * 获得管理员的信息
     * @param username
     * @return
     */
    AdminUser queryAdminByUserName(String username);


    /**
     * 添加管理员
     * @param newAdminBO
     */
    void createAdminUser(NewAdminBO newAdminBO);

    /**
     * 分页查询admin列表
      * @param page
     * @param pageSize
     */
    PagedGridResult queryAdminList(Integer page, Integer pageSize);

}

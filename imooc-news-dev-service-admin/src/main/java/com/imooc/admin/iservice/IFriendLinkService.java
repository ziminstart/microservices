package com.imooc.admin.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.model.bo.NewAdminBO;
import com.imooc.model.mo.FriendLinkMO;
import com.imooc.model.pojo.AdminUser;
import com.imooc.utils.PagedGridResult;

import java.util.List;

/**
 * <p>
 * 运营管理平台的admin级别用户 服务类
 * </p>
 *
 * @author 恒利
 * @since 2020-12-09
 */
public interface IFriendLinkService {

    /**
     * 增加或者修改友情链接
     * @param friendLinkMO
     */
    void saveOrUpdateFriendLink(FriendLinkMO friendLinkMO);

    /**
     * 查询友情链接
     * @return
     */
    List<FriendLinkMO> queryAllFriendLinkList();

    /**
     * 删除友情链接
     */
    void delete
    (String linkId);
}

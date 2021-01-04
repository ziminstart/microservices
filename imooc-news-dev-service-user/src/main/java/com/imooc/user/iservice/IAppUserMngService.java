package com.imooc.user.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.model.bo.UpdateUserInfoBO;
import com.imooc.model.pojo.AppUser;
import com.imooc.utils.PagedGridResult;

/**
 * <p>
 * 网站用户 服务类
 * </p>
 *
 * @author 恒利
 * @since 2020-12-09
 */
public interface IAppUserMngService extends IService<AppUser> {

    /**
     *  查询管理员列表
     * @param nickname
     * @param status
     * @param startDate
     * @param endDate
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult queryAllUserList(String nickname,
                                     Integer status,
                                     String startDate,
                                     String endDate,
                                     Integer page,
                                     Integer pageSize);


    /**
     * 冻结用户账号或者解除冻结状态
     * @param userId
     * @param doStatus
     */
    void freezeUserOrNot(String userId,Integer doStatus);
}

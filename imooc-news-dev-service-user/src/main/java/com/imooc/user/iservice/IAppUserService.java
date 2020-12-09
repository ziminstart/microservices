package com.imooc.user.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.model.pojo.AppUser;

/**
 * <p>
 * 网站用户 服务类
 * </p>
 *
 * @author 恒利
 * @since 2020-12-09
 */
public interface IAppUserService extends IService<AppUser> {

    /**
     * 判断用户是否存在，如果存在返回user信息
     * @param mobile
     * @return
     */
    AppUser queryMobileIsExist(String mobile);

    /**
     *  创建用户，新增记录到数据库
     * @param mobile
     * @return
     */
    AppUser createUser(String mobile);

}

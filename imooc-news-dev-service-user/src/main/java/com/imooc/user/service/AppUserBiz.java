package com.imooc.user.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.enums.Sex;
import com.imooc.enums.UserStatus;
import com.imooc.model.pojo.AppUser;
import com.imooc.user.iservice.IAppUserService;
import com.imooc.user.mapper.AppUserMapper;
import com.imooc.utils.DesensitizationUtil;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 网站用户 服务实现类
 * </p>
 *
 * @author 恒利
 * @since 2020-12-09
 */
@Service
public class AppUserBiz extends ServiceImpl<AppUserMapper, AppUser> implements IAppUserService {

    @Autowired
    private AppUserMapper appUserMapper;

    @Autowired
    public Sid sid;

    /**
     * 判断用户是否存在，如果存在返回user信息
     * @param mobile
     * @return
     */
    @Override
    public AppUser queryMobileIsExist(String mobile) {
        return appUserMapper.selectOne(Wrappers.<AppUser>lambdaQuery().eq(AppUser::getMobile, mobile));
    }

    /**
     *  创建用户，新增记录到数据库
     * @param mobile
     * @return
     */
    @Transactional
    @Override
    public AppUser createUser(String mobile) {
        /**
         * 互联网项目都要考虑可扩展性
         * 如果未来的业务激增，那么就需要分库分表
         * 那么数据库表主键id必须保证全局唯一，不得重复
         */
        AppUser appUser = new AppUser();
        String userId = sid.nextShort();
        appUser.setId(userId);
        appUser.setMobile(mobile);
        appUser.setNickname("用户:" + DesensitizationUtil.commonDisplay(mobile));
        appUser.setFace("");
        appUser.setBirthday(LocalDate.now());
        appUser.setSex(Sex.secret.type);
        appUser.setActiveStatus(UserStatus.INACTIVE.type);
        appUser.setCreateTime(LocalDateTime.now());
        appUser.setUpdateTime(LocalDateTime.now());
        appUserMapper.insert(appUser);
        return appUser;
    }
}

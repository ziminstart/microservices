package com.imooc.user.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.enums.Sex;
import com.imooc.enums.UserStatus;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.model.bo.UpdateUserInfoBO;
import com.imooc.model.pojo.AppUser;
import com.imooc.user.iservice.IAppUserService;
import com.imooc.user.mapper.AppUserMapper;
import com.imooc.utils.DesensitizationUtil;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
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

    @Autowired
    private RedisOperator redis;

    public static final String REDIS_USER_INFO = "redis_user_info";

    /**
     * 判断用户是否存在，如果存在返回user信息
     *
     * @param mobile
     * @return
     */
    @Override
    public AppUser queryMobileIsExist(String mobile) {
        return appUserMapper.selectOne(Wrappers.<AppUser>lambdaQuery().eq(AppUser::getMobile, mobile));
    }

    /**
     * 创建用户，新增记录到数据库
     *
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

    /**
     * 根据用户主键Id查询用户信息
     *
     * @param userId
     * @return
     */

    @Override
    public AppUser getUser(String userId) {
        return appUserMapper.selectById(userId);
    }

    /**
     * 用户修改信息，完善资料，并且激活
     */
    @Override
    public void updateUserInfo(UpdateUserInfoBO updateUserInfoBO) {
        String userId = updateUserInfoBO.getId();
        //保证双写一致，先删除redis中删除数据，后更新数据库
        AppUser appUser = new AppUser();
        BeanUtils.copyProperties(updateUserInfoBO, appUser);
        appUser.setUpdateTime(LocalDateTime.now());
        appUser.setActiveStatus(UserStatus.ACTIVE.type);
        int result = appUserMapper.updateById(appUser);
        if (result != 1) {
            GraceException.display(ResponseStatusEnum.USER_UPDATE_ERROR);
        }
        //再次查询用户的最新信息，放入redis中
        AppUser user = getUser(userId);
        redis.set(REDIS_USER_INFO + ":" + userId, JsonUtils.objectToJson(user));
        //缓存双删策略
        try {
            Thread.sleep(100);
            redis.del(REDIS_USER_INFO + ":" + userId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

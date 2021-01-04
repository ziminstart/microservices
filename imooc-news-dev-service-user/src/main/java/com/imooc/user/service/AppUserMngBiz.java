package com.imooc.user.service;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.api.service.IBaseService;
import com.imooc.enums.Sex;
import com.imooc.enums.UserStatus;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.model.bo.UpdateUserInfoBO;
import com.imooc.model.pojo.AppUser;
import com.imooc.user.iservice.IAppUserMngService;
import com.imooc.user.iservice.IAppUserService;
import com.imooc.user.mapper.AppUserMapper;
import com.imooc.utils.DesensitizationUtil;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.PagedGridResult;
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
public class AppUserMngBiz extends ServiceImpl<AppUserMapper, AppUser> implements IAppUserMngService, IBaseService {

    @Autowired
    private AppUserMapper appUserMapper;

    @Override
    public PagedGridResult queryAllUserList(String nickname, Integer status, String startDate, String endDate, Integer page, Integer pageSize) {
        Page<AppUser> appUserPage = new Page<>();
        appUserPage.setCurrent(page);
        appUserPage.setSize(pageSize);
        Page<AppUser> appUsers= appUserMapper.selectPage(appUserPage, Wrappers.<AppUser>lambdaQuery()
                .like(StringUtils.isNotBlank(nickname), AppUser::getNickname, nickname)
                .eq(UserStatus.isUserStatusValid(status), AppUser::getActiveStatus, status)
                .ge(StringUtils.isNotBlank(startDate), AppUser::getCreateTime, startDate)
                .le(StringUtils.isNotBlank(endDate), AppUser::getUpdateTime, endDate)
                .orderByDesc(AppUser::getCreateTime));
        return setterPagedGrid(appUsers);
    }

    @Override
    public void freezeUserOrNot(String userId, Integer doStatus) {
        AppUser appUser = new AppUser();
        appUser.setId(userId);
        appUser.setActiveStatus(doStatus);
        appUserMapper.updateById(appUser);
    }
}

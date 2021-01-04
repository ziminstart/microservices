package com.imooc.user.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.user.IAppUserMngControllerApi;
import com.imooc.enums.UserStatus;
import com.imooc.grace.result.R;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.user.iservice.IAppUserMngService;
import com.imooc.user.iservice.IAppUserService;
import com.imooc.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zimin
 */
@RestController
public class AppUserMngController extends BaseController implements IAppUserMngControllerApi {

    @Autowired
    private IAppUserMngService appUserMngService;
    @Autowired
    private IAppUserService appUserService;

    @Override
    public R queryAll(String nickname,
                      Integer status,
                      String startDate,
                      String endDate,
                      Integer page,
                      Integer pageSize) {

        if (page == null){
            page = COMMON_START_PAGE;
        }
        if (pageSize == null){
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult pagedGridResult = appUserMngService.queryAllUserList(nickname, status, startDate, endDate, page, pageSize);
        return R.ok(pagedGridResult);
    }

    @Override
    public R userDetail(String userId) {
        return  R.ok(appUserService.getUser(userId));
    }

    @Transactional
    @Override
    public R freezeUserOrNot(String userId,
                             Integer doStatus) {
        if(!UserStatus.isUserStatusValid((doStatus))){
            return R.errorCustom(ResponseStatusEnum.USER_STATUS_ERROR);
        }
        appUserMngService.freezeUserOrNot(userId,doStatus);
        // 刷新用户状态
        // 1 删除用户会话，从而保障用户需要重新登录以后再来刷新它的会话状态
        // 2 查询最新用户的信息，重新放入redis中，做一次更新
        redis.del(REDIS_USER_INFO + ":" + userId);
        return R.ok();
    }
}

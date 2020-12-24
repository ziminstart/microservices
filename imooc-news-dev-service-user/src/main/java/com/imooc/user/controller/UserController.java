package com.imooc.user.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.user.IUserControllerApi;
import com.imooc.grace.result.R;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.files.bo.UpdateUserInfoBO;
import com.imooc.files.pojo.AppUser;
import com.imooc.files.vo.AppUserVO;
import com.imooc.files.vo.UserAccountInfoVO;
import com.imooc.user.iservice.IAppUserService;
import com.imooc.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author zimin
 */
@RestController
@Slf4j
public class UserController extends BaseController implements IUserControllerApi {

    @Autowired
    private IAppUserService iAppUserService;


    @Override
    public R getUserInfo(String userId) {
        //判断参数不能为空
        if (StringUtils.isEmpty(userId)) {
            return R.errorCustom(ResponseStatusEnum.UN_LOGIN);
        }
        //根据userId查询用户信息
        AppUser user = getUser(userId);
        AppUserVO userVO = new AppUserVO();
        BeanUtils.copyProperties(user, userVO);
        return R.ok(userVO);
    }

    /**
     * @param userId
     * @return
     */
    @Override
    public R accountInfo(String userId) {
        //判断参数不能为空
        if (StringUtils.isEmpty(userId)) {
            return R.errorCustom(ResponseStatusEnum.UN_LOGIN);
        }
        //根据userId查询用户信息
        AppUser user = getUser(userId);
        //返回用户信息
        if (ObjectUtils.isEmpty(user)) {
            return R.error();
        }
        UserAccountInfoVO accountInfoVO = new UserAccountInfoVO();
        BeanUtils.copyProperties(user, accountInfoVO);
        return R.ok(accountInfoVO);
    }

    @Override
    public R updateUserInfo(@Valid UpdateUserInfoBO updateUserInfoBO, BindingResult result) {
        // 校验BO
        if (result.hasErrors()) {
            Map<String, String> errors = getErrors(result);
            return R.errorMap(errors);
        }
        //更新
        iAppUserService.updateUserInfo(updateUserInfoBO);
        return R.ok();
    }

    public AppUser getUser(String userId) {
        //查询判断redis中是否包含用户信息，如果包含，则直接查询后返回
        String userJson = redis.get(REDIS_USER_INFO + ":" + userId);
        AppUser user;
        if (!StringUtils.isEmpty(userJson)) {
            user = JsonUtils.jsonToPojo(userJson, AppUser.class);
        } else {
            // 由于用户信息不怎么会变动，对于一些千万级别的网站来说，这类信息不会去直接查询数据库
            // 那么完全可以依靠redis，直接把查询的数据存入到redis中
            user = iAppUserService.getUser(userId);
            redis.set(REDIS_USER_INFO + ":" + userId, JsonUtils.objectToJson(user));
        }
        return user;
    }

}

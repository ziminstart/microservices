package com.imooc.user.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.user.IPassPortControllerApi;
import com.imooc.enums.UserStatus;
import com.imooc.grace.result.R;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.model.bo.RegisterLoginBO;
import com.imooc.model.pojo.AppUser;
import com.imooc.user.iservice.IAppUserService;
import com.imooc.utils.IPUtil;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.SMSUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;
import java.util.UUID;

/**
 * @author zimin
 */
@Slf4j
@RestController
public class PassPortController extends BaseController implements IPassPortControllerApi {

    @Autowired
    private SMSUtil smsUtil;
    @Autowired
    private IAppUserService iAppUserService;


    /**
     * 获取短信的验证码
     *
     * @return
     */
    @Override
    public R getSmsCode(String mobile, HttpServletRequest request) {
        //获取用户ip
        String ip = IPUtil.getRequestIp(request);
        //根据用户Id进行限制，限制用户60s内只能获得一次验证码
        redis.setnx60s(MOBILE_SMS_CODE + ":" + ip, ip);
        //生成随机验证码并且发送短信
        String random = (int) ((Math.random() * 9 + 1) * 100000) + "";
        smsUtil.sendSMS(mobile, "太叔雨", "139201813", random);
        //把验证码存入redis,用于后续的验证
        redis.set(MOBILE_SMS_CODE + ":" + mobile, random, 30 * 60);
        return R.ok();
    }

    /**
     * 一键登录和注册接口
     *
     * @param registerLoginBO
     * @return
     */
    @Override
    public R doLogin(@Valid RegisterLoginBO registerLoginBO, BindingResult result,
                     HttpServletRequest request, HttpServletResponse response) {
        //判断BindResult中是否保存错误的验证信息，如果有，刚需要返回
        if (result.hasErrors()) {
            Map<String, String> errors = getErrors(result);
            return R.errorMap(errors);
        }
        // 校验验证码是否匹配
        String smsCode = registerLoginBO.getSmsCode();
        String mobile = registerLoginBO.getMobile();
        String redisSmsCode = redis.get(MOBILE_SMS_CODE + ":" + mobile);
        if (StringUtils.isEmpty(redisSmsCode) || !redisSmsCode.equalsIgnoreCase(smsCode)) {
            return R.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
        }

        //查询数据库，判断该用户是否注册
        AppUser user = iAppUserService.queryMobileIsExist(mobile);
        if (!ObjectUtils.isEmpty(user) && user.getActiveStatus().equals(UserStatus.FROZEN.type)) {
            //如果用户不空， 并且状态冻结，刚直接抛出异常，禁止登录
            return R.errorCustom(ResponseStatusEnum.USER_FROZEN);
        } else if (ObjectUtils.isEmpty(user)) {
            //如果用户没有注册过，则为null，需要注册信息入库
            user = iAppUserService.createUser(mobile);
        }

        // 保存用户分布式会话的相关操作
        int activeStatus = user.getActiveStatus();
        if (activeStatus != UserStatus.FROZEN.type) {
            //保存token到redis
            String uToken = UUID.randomUUID().toString();
            redis.set(REDIS_USER_TOKEN + ":" + user.getId(), uToken);
            redis.set(REDIS_USER_INFO + ":" + user.getId(), JsonUtils.objectToJson(user));
            //保存用户id和token到cookie中
            setCookie(request, response, "utoken", uToken, COOKIE_MONTH);
            setCookie(request, response, "uid", user.getId(), COOKIE_MONTH);
        }
        // 用户登录成功或注册以后，需要删除redis中缓存的验证码
        redis.del(MOBILE_SMS_CODE + ":" + mobile);
        //返回用户状态
        return R.ok(activeStatus);
    }


    /**
     * 注销登录
     *
     * @return
     */
    @Override
    public R logout(String userId, HttpServletRequest request, HttpServletResponse response) {
        //清除redis中的数据
        redis.del(REDIS_USER_TOKEN + ":" + userId);
        setCookie(request,response,"utoken","",COOKIE_DELETE);
        setCookie(request,response,"uid","",COOKIE_DELETE);
        return R.ok();
    }


}

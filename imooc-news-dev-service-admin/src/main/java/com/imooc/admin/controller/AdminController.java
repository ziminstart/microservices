package com.imooc.admin.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.imooc.admin.iservice.IAdminUserService;
import com.imooc.api.BaseController;
import com.imooc.api.controller.admin.IAdminControllerApi;
import com.imooc.enums.FaceVerifyType;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.R;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.model.bo.AdminLoginBO;
import com.imooc.model.bo.NewAdminBO;
import com.imooc.model.pojo.AdminUser;
import com.imooc.utils.FaceVerifyUtils;
import com.imooc.utils.PagedGridResult;
import com.imooc.utils.RedisOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;
import java.util.UUID;

/**
 * @author zimin
 */
@RestController
public class AdminController extends BaseController implements IAdminControllerApi {

    final static Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private IAdminUserService adminUserService;
    @Autowired
    private RedisOperator redis;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private FaceVerifyUtils faceVerifyUtils;

    @Override
    public R adminLogin(AdminLoginBO adminLoginBO,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        // 0 TODO  验证BO中的用户名和密码不为空

        // 1 查询admin用户信息
        AdminUser adminUser = adminUserService.queryAdminByUserName(adminLoginBO.getUsername());
        // 2 判断admin不为空，如果为空则登录失败
        if (adminUser == null) {
            return R.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
        }
        // 3 判断密码是否匹配
        boolean isPwdMatch = BCrypt.checkpw(adminLoginBO.getPassword(), adminUser.getPassword());
        if (isPwdMatch) {
            doLoginSettings(adminUser, request, response);
            return R.ok();
        } else {
            return R.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
        }
    }

    @Override
    public R adminIsExist(String username) {
        checkAdminExist(username);
        return R.ok();
    }

    @Override
    public R addNewAdmin(@Valid NewAdminBO newAdminBO,
                         BindingResult result,
                         HttpServletRequest request,
                         HttpServletResponse response) {
        // 0 验证BO中的用户名和密码不为空
        if (result.hasErrors()) {
            Map<String, String> errors = getErrors(result);
            return R.errorMap(errors);
        }
        // 1 base64不为空，刚代表人脸入库，否则需要用户输入密码和确认密码
        if (StringUtils.isBlank(newAdminBO.getImg64())) {
            if (StringUtils.isBlank(newAdminBO.getPassword())
                    || StringUtils.isBlank(newAdminBO.getConfirmPassword())) {
                return R.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_NULL_ERROR);
            }
        }
        // 2 密码不为空刚必须判断两次输入的是否一致
        if (StringUtils.isNotBlank(newAdminBO.getPassword())) {
            if (!newAdminBO.getPassword()
                    .equalsIgnoreCase(newAdminBO.getConfirmPassword())) {
                return R.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_ERROR);
            }

        }
        // 3 校验用户名唯一
        checkAdminExist(newAdminBO.getUsername());
        // 4 调用service存入admin信息
        adminUserService.createAdminUser(newAdminBO);
        return R.ok();
    }

    @Override
    public R getAdminList(Integer page, Integer pageSize) {
        if (page == null) {
            page = COMMON_START_PAGE;
        }

        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult result = adminUserService.queryAdminList(page, pageSize);
        return R.ok(result);
    }

    @Override
    public R adminLogout(String adminId, HttpServletRequest request, HttpServletResponse response) {
        //从redis中删除admin的会话token
        redis.del(REDIS_ADMIN_TOKEN + ":" + adminId);
        //从cookie中清理admin的登录信息
        deleteCookie(request, response, "atoken");
        deleteCookie(request, response, "aid");
        deleteCookie(request, response, "aname");
        return R.ok();
    }

    @Override
    public R adminFaceLogin(AdminLoginBO adminLoginBO, HttpServletRequest request, HttpServletResponse response) {
        // 0 判断用户名和人脸信息不能为空
        if (StringUtils.isBlank(adminLoginBO.getUsername())){
            return R.errorCustom(ResponseStatusEnum.ADMIN_USERNAME_NULL_ERROR);
        }
        String tempFace64 = adminLoginBO.getImg64();
        if(StringUtils.isBlank(tempFace64)){
            return R.errorCustom(ResponseStatusEnum.ADMIN_FACE_NULL_ERROR);
        }
        // 1 从数据库中查询出faceId
        AdminUser adminUser = adminUserService.queryAdminByUserName(adminLoginBO.getUsername());
        String adminFaceId = adminUser.getFaceId();
        if(StringUtils.isBlank(adminFaceId)){
            return R.errorCustom(ResponseStatusEnum.ADMIN_FACE_LOGIN_ERROR);
        }
        // 2 请求文件服务，获得人脸数据的base64数据
        String fileServerUrlExecute = "http://files.imoocnews.com:8004/fs/readFace64InGridFS?faceId=" + adminFaceId;
        ResponseEntity<R> responseEntity = restTemplate.getForEntity(fileServerUrlExecute, R.class);
        R bodyResult = responseEntity.getBody();
        String base64DB = (String) bodyResult.getData();
        // 3 调用阿里ai进行人脸识别，判断可信度，从面实现人脸登录
        boolean result = faceVerifyUtils.faceVerify(FaceVerifyType.BASE64.type, tempFace64, base64DB, 60);
        if(!result){
            return R.errorCustom(ResponseStatusEnum.FACE_VERIFY_LOGIN_ERROR);
        }
        // 4 admin登录后的设置，redis和cookie
        doLoginSettings(adminUser,request,response);
        return R.ok();
    }

    /**
     * 用于admin用户登录后的设置
     *
     * @param request
     * @param response
     */
    private void doLoginSettings(AdminUser admin, HttpServletRequest request,
                                 HttpServletResponse response) {
        String token = UUID.randomUUID().toString();
        //保存token放入到redis中
        redis.set(REDIS_ADMIN_TOKEN + ":" + admin.getId(), token);
        //保存admin登录基本token信息到cookie中
        setCookie(request, response, "atoken", token, COOKIE_MONTH);
        setCookie(request, response, "aid", admin.getId(), COOKIE_MONTH);
        setCookie(request, response, "aname", admin.getAdminName(), COOKIE_MONTH);
    }


    /**
     * 检查admin是否存在
     *
     * @param username
     */
    private void checkAdminExist(String username) {
        AdminUser admin = adminUserService.queryAdminByUserName(username);
        if (admin != null) {
            GraceException.display(ResponseStatusEnum.ADMIN_USERNAME_EXIST_ERROR);
        }

    }

}


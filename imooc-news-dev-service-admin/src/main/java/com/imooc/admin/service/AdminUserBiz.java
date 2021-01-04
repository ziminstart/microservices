package com.imooc.admin.service;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.admin.iservice.IAdminUserService;
import com.imooc.admin.mapper.AdminUserMapper;
import com.imooc.api.service.IBaseService;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.model.bo.NewAdminBO;
import com.imooc.model.pojo.AdminUser;
import com.imooc.utils.PagedGridResult;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 * 运营管理平台的admin级别用户 服务实现类
 * </p>
 *
 * @author 恒利
 * @since 2020-12-09
 */
@Service
public class AdminUserBiz extends ServiceImpl<AdminUserMapper, AdminUser> implements IAdminUserService, IBaseService {

    @Autowired
    public AdminUserMapper adminUserMapper;
    @Autowired
    private Sid sid;

    @Override
    public AdminUser queryAdminByUserName(String username) {
        return adminUserMapper.selectOne(Wrappers.<AdminUser>lambdaQuery().eq(AdminUser::getUsername, username));
    }

    @Transactional
    @Override
    public void createAdminUser(NewAdminBO newAdminBO) {
        String adminId = sid.nextShort();
        AdminUser adminUser = new AdminUser();
        adminUser.setId(adminId);
        adminUser.setUsername(newAdminBO.getUsername());
        adminUser.setAdminName(newAdminBO.getAdminName());

        //如果密码不为空,则需要加密密码，存入数据库
        if (StringUtils.isNotBlank(newAdminBO.getPassword())){
            String pwd = BCrypt.hashpw(newAdminBO.getPassword(),BCrypt.gensalt());
            adminUser.setPassword(pwd);
        }
        //如果人脸上传以后，则有faceId,需要和admin关联存储入库
        if (StringUtils.isNotBlank(newAdminBO.getFaceId())){
            adminUser.setFaceId(newAdminBO.getFaceId());
        }

        adminUser.setCreateTime(LocalDateTime.now());
        adminUser.setUpdateTime(LocalDateTime.now());
        int insert = adminUserMapper.insert(adminUser);
        if (insert != 1){
            GraceException.display(ResponseStatusEnum.ADMIN_CREATE_ERROR);
        }

    }

    @Override
    public PagedGridResult queryAdminList(Integer page, Integer pageSize) {
        Page<AdminUser> adminUserPage = new Page<>();
        adminUserPage.setCurrent(page);
        adminUserPage.setSize(pageSize);
        Page<AdminUser> adminUserPages = adminUserMapper.selectPage(adminUserPage, Wrappers.<AdminUser>lambdaQuery().orderByDesc(AdminUser::getCreateTime));
        return setterPagedGrid(adminUserPages);
    }

}

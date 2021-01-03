package com.imooc.admin.controller;

import com.imooc.admin.repository.FriendLinkRepository;
import com.imooc.admin.service.FriendLinkBiz;
import com.imooc.api.BaseController;
import com.imooc.api.controller.admin.IFriendLinkControllerApi;
import com.imooc.grace.result.R;
import com.imooc.model.bo.SaveFriendLinkBO;
import com.imooc.model.mo.FriendLinkMO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author zimin
 */
@RestController
public class FriendLinkController extends BaseController implements IFriendLinkControllerApi {

    @Autowired
    private FriendLinkBiz friendLinkBiz;

    @Override
    public R saveOrUpdateFriendLink(@Valid SaveFriendLinkBO saveFriendLinkBO,
                                    BindingResult result) {
        if (result.hasErrors()){
            Map<String, String> errors = getErrors(result);
            return R.errorMap(errors);
        }
        FriendLinkMO friendLinkMO = new FriendLinkMO();
        BeanUtils.copyProperties(saveFriendLinkBO,friendLinkMO);
        friendLinkMO.setCreateTime(LocalDateTime.now());
        friendLinkMO.setUpdateTime(LocalDateTime.now());
        friendLinkBiz.saveOrUpdateFriendLink(friendLinkMO);
        return R.ok();
    }
}

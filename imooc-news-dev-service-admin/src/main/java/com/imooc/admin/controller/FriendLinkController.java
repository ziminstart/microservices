package com.imooc.admin.controller;

import com.imooc.admin.iservice.IFriendLinkService;
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
import java.util.List;
import java.util.Map;

/**
 * @author zimin
 */
@RestController
public class FriendLinkController extends BaseController implements IFriendLinkControllerApi {

    @Autowired
    private IFriendLinkService friendLinkService;

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
        friendLinkService.saveOrUpdateFriendLink(friendLinkMO);
        return R.ok();
    }

    @Override
    public R getFriendLinkList() {
        List<FriendLinkMO> friendLinkMOS = friendLinkService.queryAllFriendLinkList();
        return R.ok(friendLinkMOS);
    }

    @Override
    public R delete(String linkId) {
        friendLinkService.delete(linkId);
        return R.ok();
    }

    @Override
    public R queryPortalAllFriendLinkList() {
        List<FriendLinkMO> friendLinkMOS = friendLinkService.queryPortalAllFriendLinkList();
        return R.ok(friendLinkMOS);
    }
}

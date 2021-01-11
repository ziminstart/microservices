package com.imooc.user.controller;


import com.imooc.api.BaseController;
import com.imooc.api.controller.user.IAppUserMngControllerApi;
import com.imooc.api.controller.user.IMyFansControllerApi;
import com.imooc.enums.Sex;
import com.imooc.grace.result.R;
import com.imooc.model.pojo.Fans;
import com.imooc.model.vo.RegionRatioVO;
import com.imooc.user.iservice.IFansService;
import com.imooc.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 粉丝表，用户与粉丝的关联关系，粉丝本质也是用户。
关联关系保存到es中，粉丝数方式和用户点赞收藏文章一样。累加累减都用redis来做。
字段与用户表有些冗余，主要用于数据可视化，数据一旦有了之后，用户修改性别和省份无法影响此表，只认第一次的数据。

 前端控制器
 * </p>
 *
 * @author 恒利
 * @since 2020-12-09
 */
@RestController
public class FansController extends BaseController implements IMyFansControllerApi {

    @Autowired
    private IFansService fansService;

    @Override
    public R isMeFollowThisWriter(String writerId, String fanId) {
        boolean res = fansService.isMeFollowThisWrite(writerId, fanId);
        return R.ok(res);
    }

    @Override
    public R follow(String writerId, String fanId) {
        fansService.follow(writerId,fanId);
        return R.ok();
    }

    @Override
    public R unfollow(String writerId, String fanId) {
        fansService.unfollow(writerId,fanId);
        return null;
    }

    @Override
    public R queryAll(String writerId, Integer page, Integer pageSize) {
        if(page == null){
            page = COMMON_START_PAGE;
        }

        if(pageSize== null){
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult result =  fansService.queryMyFansList(writerId,page,pageSize);
        return R.ok(result);
    }

    @Override
    public R queryRatio(String writerId) {
        Integer manCounts = fansService.queryFansCount(writerId, Sex.man);
        Integer womanCounts = fansService.queryFansCount(writerId, Sex.woman);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("manCounts",manCounts);
        resultMap.put("womanCounts",womanCounts);
        return R.ok(resultMap);
    }

    @Override
    public R queryRatioByRegion(String writerId) {
        List<RegionRatioVO> fansList = fansService.queryRatioByRegion(writerId);
        return R.ok(fansList);
    }
}

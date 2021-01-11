package com.imooc.user.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.api.service.IBaseService;
import com.imooc.enums.Sex;
import com.imooc.model.pojo.AppUser;
import com.imooc.model.pojo.Fans;
import com.imooc.model.vo.RegionRatioVO;
import com.imooc.user.iservice.IAppUserService;
import com.imooc.user.iservice.IFansService;
import com.imooc.user.mapper.FansMapper;
import com.imooc.utils.PagedGridResult;
import com.imooc.utils.RedisOperator;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 粉丝表，用户与粉丝的关联关系，粉丝本质也是用户。
关联关系保存到es中，粉丝数方式和用户点赞收藏文章一样。累加累减都用redis来做。
字段与用户表有些冗余，主要用于数据可视化，数据一旦有了之后，用户修改性别和省份无法影响此表，只认第一次的数据。

 服务实现类
 * </p>
 *
 * @author 恒利
 * @since 2020-12-09
 */
@Service
public class FansBiz extends ServiceImpl<FansMapper, Fans> implements IFansService, IBaseService {

    @Autowired
    private FansMapper fansMapper;
    @Autowired
    private IAppUserService userService;
    @Autowired
    private Sid sid;
    @Autowired
    private RedisOperator reids;

    @Override
    public boolean isMeFollowThisWrite(String writerId, String fanId) {
        Integer count = fansMapper.selectCount(Wrappers.<Fans>lambdaQuery().eq(Fans::getFanId, fanId).eq(Fans::getWriterId, writerId));
        return count > 0;
    }

    @Transactional
    @Override
    public void follow(String writerId, String fanId) {
        //获得粉丝用户的信息
        AppUser fanInfo = userService.getUser(fanId);
        String fanPkId = sid.nextShort();
        Fans fans = new Fans();
        fans.setId(fanPkId);
        fans.setFace(fanId);
        fans.setWriterId(writerId);
        fans.setFace(fanInfo.getFace());
        fans.setFanNickname(fanInfo.getNickname());
        fans.setSex(fanInfo.getSex());
        fans.setProvince(fanInfo.getProvince());
        fansMapper.insert(fans);
        //redis 作家粉丝数累加
        reids.increment(REDIS_WRITER_FANS_COUNTS + ":" + writerId,1);
        //我的关注数累加
        reids.increment(REDIS_MY_FOLLOW_COUNTS+ ":" + fanId,1);
    }

    @Transactional
    @Override
    public void unfollow(String writerId, String fanId) {
        Map<String, Object> deleteCondition= new HashMap<>();
        deleteCondition.put("writerId",writerId);
        deleteCondition.put("fanId",fanId);
        fansMapper.deleteByMap(deleteCondition);
        //redis 作家粉丝数累减
        reids.decrement(REDIS_WRITER_FANS_COUNTS + ":" + writerId,1);
        //我的关注数累加
        reids.decrement(REDIS_MY_FOLLOW_COUNTS+ ":" + fanId,1);
    }

    @Override
    public PagedGridResult queryMyFansList(String writerId,Integer page, Integer pageSize) {
        Page<Fans> fansPage = fansMapper.selectPage(setterPage(page, pageSize), Wrappers.<Fans>lambdaQuery().eq(Fans::getWriterId, writerId));
        return setterPagedGrid(fansPage);
    }

    @Override
    public Integer queryFansCount(String writerId, Sex sex) {
        return fansMapper.selectCount(Wrappers.<Fans>lambdaQuery().eq(Fans::getWriterId, writerId).eq(Fans::getSex, sex));
    }

    public static final String[] regions = {"北京", "天津", "上海", "重庆",
            "河北", "山西", "辽宁", "吉林", "黑龙江", "江苏", "浙江", "安徽", "福建", "江西", "山东",
            "河南", "湖北", "湖南", "广东", "海南", "四川", "贵州", "云南", "陕西", "甘肃", "青海", "台湾",
            "内蒙古", "广西", "西藏", "宁夏", "新疆",
            "香港", "澳门"};

    @Override
    public List<RegionRatioVO> queryRatioByRegion(String writerId) {
        List<RegionRatioVO> lists = Arrays.stream(regions).parallel().map(s -> setterRegionRation(s, writerId)).collect(Collectors.toList());
        return lists;
    }

    private RegionRatioVO setterRegionRation(String province,String writerId){
        Integer fans = fansMapper.selectCount(Wrappers.<Fans>lambdaQuery()
                .eq(Fans::getProvince, province)
                .eq(Fans::getWriterId, writerId));
        RegionRatioVO build = RegionRatioVO.builder().name(province).value(fans).build();
        return build;
    }

}

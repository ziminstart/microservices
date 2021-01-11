package com.imooc.user.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.enums.Sex;
import com.imooc.model.pojo.Fans;
import com.imooc.model.vo.RegionRatioVO;
import com.imooc.utils.PagedGridResult;

import java.util.List;

/**
 * <p>
 * 粉丝表，用户与粉丝的关联关系，粉丝本质也是用户。
关联关系保存到es中，粉丝数方式和用户点赞收藏文章一样。累加累减都用redis来做。
字段与用户表有些冗余，主要用于数据可视化，数据一旦有了之后，用户修改性别和省份无法影响此表，只认第一次的数据。

 服务类
 * </p>
 *
 * @author 恒利
 * @since 2020-12-09
 */
public interface IFansService extends IService<Fans> {

    /**
     * 查询用户有没有关注作者
     * @param writerId
     * @param fanId
     * @return
     */
    boolean isMeFollowThisWrite(String writerId, String fanId);

    /**
     * 关注作家成为粉丝
     * @param writerId
     * @param fanId
     * @return
     */
    void follow(String writerId, String fanId);

    /**
     * 粉丝取消关注
     * @param writerId
     * @param fanId
     */
    void unfollow(String writerId, String fanId);

    /**
     * 查询我的粉丝数
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult queryMyFansList(String writerId,Integer page, Integer pageSize);

    /**
     * 查询男女的数量
     * @param writerId
     * @param sex
     */
    Integer queryFansCount(String writerId, Sex sex);

    /**
     * 查询地域请求
     * @param writerId
     * @return
     */
    List<RegionRatioVO> queryRatioByRegion(String writerId);
}

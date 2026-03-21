package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.cwork.IndentAndMerchants;
import com.cheji.web.modular.cwork.MyTeam;
import com.cheji.web.modular.cwork.PromoteList;
import com.cheji.web.modular.domain.UserEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-08-26
 */
public interface UserMapper extends BaseMapper<UserEntity> {

    //根据id查询到用户信息
    IndentAndMerchants userMerchantsById(String userId);

    //根据id查询到parentid
    Integer findTeamCount(String userId);

    //查询粉丝
    List<UserEntity> findFansByUserid(@Param("userid") Integer userid, @Param("pagesize")Integer pagesize);

    //查询团队列表
    List<MyTeam> findMyTeamListById(String id);

    //查询到我的余额根据userId
    String findWallet(String userId);

    List<PromoteList> findPromote(@Param("id")Integer id,@Param("pagesize")Integer pagesize);

    Integer findAccState(Integer id);

    Integer findProple(Integer id);

    String findVip(Long id);

    void openVip(Integer userId, String vipLv);
}

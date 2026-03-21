package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.cwork.AmountList;
import com.cheji.web.modular.cwork.FansVideoDto;
import com.cheji.web.modular.domain.AppUserEntity;
import com.cheji.web.modular.domain.UserEntity;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 用户 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-08-13
 */
public interface AppUserMapper extends BaseMapper<AppUserEntity> {


    AppUserEntity selectUser(Integer userId);

    List<AmountList> findChangeList(@Param("id")Integer id, @Param("pagesize")Integer pagesize);

    List<AmountList> findChangeListAndDate(@Param("id")Integer id, @Param("pagesize")Integer pagesize, @Param("date")String date);

    UserEntity selectWxuser(String wxuserId);

    BigDecimal findIncome(@Param("operationMonth")String operationMonth, @Param("id")Integer id);

    BigDecimal findSpend(@Param("operationMonth")String operationMonth, @Param("id")Integer id);

    List<Integer> selectId();

    AppUserEntity findUserById(Integer parentId);

    List<String> findFans(Integer id);

    List<FansVideoDto> findVideo(@Param("users") List<String> users, @Param("pagesize") Integer pagesize, @Param("beginTime")String beginTime, @Param("endTime")String endTime);
}

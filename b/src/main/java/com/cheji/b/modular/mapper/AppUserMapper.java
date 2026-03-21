package com.cheji.b.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.b.modular.domain.AppUserEntity;
import com.cheji.b.modular.dto.ChangeListDto;
import com.cheji.b.modular.dto.ServicDisplayDto;
import com.cheji.b.modular.dto.StoresDto;
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


    Integer findEarningsAndIndent(Integer userId);

    BigDecimal findTodayEarning(Integer user_b_id);


    StoresDto findStore(Integer userBId);

    ServicDisplayDto getNameBrandScore(Integer userBId);

    List<ChangeListDto> findChangeList(@Param("userBId") Integer userBId, @Param("pagesize")Integer pagesize);

    BigDecimal findIncome(@Param("operationMonth")String operationMonth, @Param("userBId")Integer userBId);

    BigDecimal findspennd(@Param("operationMonth")String operationMonth,@Param("userBId") Integer userBId);

    List<ChangeListDto> findChangeListAndMonth(@Param("userBId")Integer userBId, @Param("pagesize")Integer pagesize, @Param("date")String date);

    List<String> findBrand(Integer userBId);

    BigDecimal findCleanIncome(@Param("operationMonth")String operationMonth, @Param("userBId")Integer userBId);

    BigDecimal findTodayCleanEarning(Integer id);

    Integer findTodayCleanIndent(@Param("id")Integer id, @Param("bMessageid")Integer bMessageid);

    BigDecimal findRescueIncome(@Param("operationMonth")String operationMonth, @Param("userBId")Integer userBId);

    BigDecimal findTodayRescueEarning(Integer id);

    BigDecimal findTodaySprayPaintEarning(Integer id);

    Integer findTodaySprayIndent(Integer id);

    String findUpName(String member);

    AppUserEntity selectByIdupMerchats(String member);

    void updateUpMerMess(String userBId);

    AppUserEntity selectWxuser(String wxuserId);


//    String huannxinUsernaem(String member);
//
//    String huanxinPassward(String member);
//
//    Integer unreadMess(String member);
}

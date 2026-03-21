package com.cheji.b.modular.mapper;

import com.cheji.b.modular.domain.IndentEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.b.modular.dto.*;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 订单信息表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-09-25
 */
public interface IndentMapper extends BaseMapper<IndentEntity> {

    List<EarningListDto> findListByMerchants(@Param("userBId") Integer userBId, @Param("pagesize") Integer pagesize);

    List<EarningListDto> findListEffectiveOrder(@Param("userBId") Integer userBId,@Param("pagesize")  Integer pagesize);

    List<IndentListDto> findMesg(@Param("userBId") Integer userBId, @Param("type") Integer type, @Param("pageSize") Integer pageSize,@Param("searchText") String searchText);

    List<IndentListDto> findAllmesg(@Param("userBId")Integer userBId, @Param("pageSize")Integer pageSize);

    MineDto findIndentCount(Integer user_b_id);

    List<EarningListDto> findConsumptionDeductions(@Param("userBId")Integer userBId, @Param("pageSize")Integer pageSize);

    IndentDetailsDto findDetailsById(Integer indentId);

    BigDecimal findPayAmount(Integer id);

    List<IndentListDto> find4mesg(@Param("userBId")Integer userBId, @Param("pageSize")Integer pageSize);

    CenterDetailsDto selectIndentCenterMes(Integer userBId);

    List<IndentList> findIndentListByuseridAndState(@Param("id") String id,@Param("year")String year,  @Param("month")String month,  @Param("state") String state, @Param("pagesize") Integer pagesize,@Param("upId") String upId);


    Integer findNumByState(@Param("id") Integer id, @Param("upId")String upId, @Param("state")String state);

    IndentDetails findIndentDetilsByIndentId(String indentId);

    Integer selectAllIndent(@Param("id")String id, @Param("upId")String upId);

    Integer selectEstimatedOutput(@Param("id")String id, @Param("upId")String upId);

    Integer selectTrueAmount(@Param("id")String id, @Param("upId")String upId);

    Integer selectPayCommission(@Param("id")String id,@Param("upId") String upId);

    BigDecimal findAllSerMoney(@Param("id")Integer id, @Param("year")String year, @Param("month")String month);

}

package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.cwork.IndentDetails;
import com.cheji.web.modular.cwork.IndentList;
import com.cheji.web.modular.cwork.Personal;
import com.cheji.web.modular.domain.IndentEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 订单信息表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-08-26
 */
public interface IndentMapper extends BaseMapper<IndentEntity> {

    List<IndentList> findIndentListByuserid(@Param("id")String id,@Param("pagesize")Integer pagesize);

    List<IndentList> findIndentListByuseridAndState(@Param("id") String id,@Param("year")String year,  @Param("month")String month,  @Param("state") String state, @Param("pagesize") Integer pagesize);

    IndentDetails findIndentDetilsByIndentId(String indentId);

    String findFixlossUserByuserId(String userId);

    Personal findIndentCount(String userId);

    List<IndentList> findFourAndFive(@Param("id") String id,@Param("pagesize") Integer pagesize);

    List<String> findSendUnit();

    List<String> findEmployee();

    IndentEntity findTime(@Param("dateString1")String dateString1, @Param("userId")Integer userId);

}

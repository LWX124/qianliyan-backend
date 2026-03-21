package com.stylefeng.guns.modular.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.datascope.DataScope;
import com.stylefeng.guns.modular.system.model.OpenClaim;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 开放平台理赔单 Mapper 接口
 * </p>
 *
 * @author kosans
 * @since 2018-09-04
 */
public interface OpenClaimMapper extends BaseMapper<OpenClaim> {
    String getOpenClaimOrderNo();
    List<Map<String, Object>> selectByCondition(@Param("page") Page<OpenClaim> page, @Param("dataScope") DataScope dataScope, @Param("openClaim") OpenClaim openClaim, @Param("condition") String condition, @Param("createStartTime") String createStartTime, @Param("createEndTime") String createEndTime, @Param("isComplete") Integer isComplete, @Param("orderByField") String orderByField, @Param("isAsc") boolean isAsc);
    List<Map<String, Object>> selectByCondition2(@Param("page") Page<OpenClaim> page, @Param("dataScope") DataScope dataScope, @Param("openClaim") OpenClaim openClaim, @Param("condition") String condition, @Param("createStartTime") String createStartTime, @Param("createEndTime") String createEndTime, @Param("isComplete") Integer isComplete, @Param("orderByField") String orderByField, @Param("isAsc") boolean isAsc);



    List<Map<String, Object>> selectByConditionForRest(@Param("page") Page<OpenClaim> page, @Param("dataScope") DataScope dataScope, @Param("openClaim") OpenClaim openClaim, @Param("condition") String condition, @Param("createStartTime") String createStartTime, @Param("createEndTime") String createEndTime, @Param("isComplete") Integer isComplete, @Param("orderByField") String orderByField, @Param("isAsc") boolean isAsc);

    List<Map<String, Object>> selectByConditionForExport(@Param("openClaim") OpenClaim openClaim, @Param("condition") String condition, @Param("createStartTime") String createStartTime, @Param("createEndTime") String createEndTime, @Param("isComplete") Integer isComplete);

    BigDecimal queryFixLossSumByCondition(@Param("openClaim") OpenClaim openClaim, @Param("condition") String condition, @Param("createStartTime") String createStartTime, @Param("createEndTime") String createEndTime, @Param("isComplete") Integer isComplete);


    List<Map<String, Object>> selectCountByStatus(@Param("deptId") int deptId);


    /**
     *
     * 根据条件统计数量
     */
    long countByCondition(@Param("openClaim") OpenClaim openClaim, @Param("condition") String condition, @Param("createStartTime") String createStartTime, @Param("createEndTime") String createEndTime, @Param("isComplete") Integer isComplete);
    BigDecimal getOpenClaimOrdersIncome(@Param("openid")String openid);

    BigDecimal getOpenClaimOrdersIncomeByAccount(@Param("account")String account);




    Integer selectCountOpenOrder(Integer status);

    String selectCountMoney();

    void updateStatusById(@Param("id") Integer id, @Param("status") Integer status);
}


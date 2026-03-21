package com.stylefeng.guns.modular.system.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.datascope.DataScope;
import com.stylefeng.guns.modular.system.model.OpenClaim;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 开放平台理赔单 服务类
 * </p>
 *
 * @author kosans
 * @since 2018-09-04
 */
public interface IOpenClaimService extends IService<OpenClaim> {
    String getOpenClaimOrderNo();

    /**
     * 根据条件查询列表
     */
    List<Map<String, Object>> selectByCondition(@Param("page") Page<OpenClaim> page, @Param("dataScope") DataScope dataScope, @Param("openClaim") OpenClaim openClaim, @Param("condition") String condition, @Param("createStartTime") String createStartTime, @Param("createEndTime") String createEndTime, @Param("isComplete") Integer isComplete, @Param("orderByField") String orderByField, @Param("isAsc") boolean isAsc);



    /**
     * 根据条件查询列表
     */
    List<Map<String, Object>> selectByCondition2(@Param("page") Page<OpenClaim> page, @Param("dataScope") DataScope dataScope, @Param("openClaim") OpenClaim openClaim, @Param("condition") String condition, @Param("createStartTime") String createStartTime, @Param("createEndTime") String createEndTime, @Param("isComplete") Integer isComplete, @Param("orderByField") String orderByField, @Param("isAsc") boolean isAsc);


    /**
     * 根据条件查询列表
     */
    List<Map<String, Object>> selectByConditionForRest(@Param("page") Page<OpenClaim> page, @Param("dataScope") DataScope dataScope, @Param("openClaim") OpenClaim openClaim, @Param("condition") String condition, @Param("createStartTime") String createStartTime, @Param("createEndTime") String createEndTime, @Param("isComplete") Integer isComplete, @Param("orderByField") String orderByField, @Param("isAsc") boolean isAsc);

    /**
     * 根据条件查询列表
     */
    List<Map<String, Object>> selectByConditionForExport(@Param("openClaim") OpenClaim openClaim, @Param("condition") String condition, @Param("createStartTime") String createStartTime, @Param("createEndTime") String createEndTime, @Param("isComplete") Integer isComplete);

    /**
     * 根据条件查询列表
     */
    BigDecimal queryFixLossSumByCondition(@Param("openClaim") OpenClaim openClaim, @Param("condition") String condition, @Param("createStartTime") String createStartTime, @Param("createEndTime") String createEndTime, @Param("isComplete") Integer isComplete);

    /**
     * 根据条件查询列表
     */
    long countByCondition(@Param("openClaim") OpenClaim openClaim, @Param("condition") String condition, @Param("createStartTime") String createStartTime, @Param("createEndTime") String createEndTime, @Param("isComplete") Integer isComplete);


    /**
     * 查询开放平台理赔顾问累计预计收益
     */
    BigDecimal getOpenClaimOrdersIncome(String openid);

    /**
     * 查询开放平台理赔单的各种状态
     */
    List<Map<String, Object>>  selectCountByStatus(int deptId);


    /**
     * 查询开放平台理赔顾问累计预计收益
     */
    BigDecimal getOpenClaimOrdersIncomeByAccount(String account);

    /**
     * 开放平台订单异常反馈
     * @param openClaim
     * @param exMgs
     */
    void reportExMsg(OpenClaim openClaim, String exMgs, String exImgUrls);

    /**
     * 统计数据
     * @return
     */
    Object countData();

    void update(OpenClaim openClaim1);
}

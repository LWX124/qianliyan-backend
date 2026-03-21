package com.stylefeng.guns.modular.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.datascope.DataScope;
import com.stylefeng.guns.modular.system.model.Accident;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 事故上报表 Mapper 接口
 * </p>
 *
 * @author kosans
 * @since 2017-07-11
 */
public interface AccdMapper extends BaseMapper<Accident> {

    /**
     * 修改事故上报状态
     */
    int setStatus(@Param("accdId") Integer accdId, @Param("status") int status, @Param("checkId") String checkId, @Param("checkTime") Date checkTime, @Param("reason") String reason);


    /**
     * 根据条件查询事故列表 分页
     */
    List<Map<String, Object>> selectAccident(@Param("page") Page<Accident> page, @Param("dataScope") DataScope dataScope, @Param("openid") String openid, @Param("createStartTime") String createStartTime,
                                             @Param("createEndTime") String createEndTime, @Param("checkStartTime") String checkStartTime, @Param("checkEndTime") String checkEndTime, @Param("checkStatus") Integer checkStatus,
                                             @Param("orderByField") String orderByField, @Param("isAsc") boolean isAsc, @Param("name") String name);


    List<Map<String, Object>> countAccidentRedPackByGroup(@Param("openid") String openid, @Param("createStartTime") String createStartTime, @Param("createEndTime") String createEndTime,
                                                          @Param("checkStartTime") String checkStartTime, @Param("checkEndTime") String checkEndTime, @Param("checkStatus") Integer checkStatus,
                                                          @Param("pushStatus") Integer pushStatus, @Param("name") String name);


    BigDecimal countAccidentRedPack(@Param("openid") String openid, @Param("createStartTime") String createStartTime, @Param("createEndTime") String createEndTime, @Param("checkStartTime") String checkStartTime,
                                    @Param("checkEndTime") String checkEndTime, @Param("checkStatus") Integer checkStatus, @Param("pushStatus") Integer pushStatus, @Param("name") String name);

    /**
     * 根据条件查询事故列表 分页
     */
    List<Map<String, Object>> selectAccident(@Param("page") Page<Accident> page, @Param("dataScope") DataScope dataScope, @Param("openid") String openid, @Param("createStartTime") String createStartTime,
                                             @Param("createEndTime") String createEndTime, @Param("checkStartTime") String checkStartTime, @Param("checkEndTime") String checkEndTime, @Param("checkStatus") Integer checkStatus,
                                             @Param("pushStatus") Integer pushStatus, @Param("orderByField") String orderByField, @Param("isAsc") boolean isAsc, @Param("name") String name);

    /**
     * 根据条件查询事故列表 分页
     */
    List<Map<String, Object>> selectAccidentForApi(@Param("page") Page<Accident> page, @Param("dataScope") DataScope dataScope, @Param("openid") String openid, @Param("createStartTime") String createStartTime, @Param("createEndTime") String createEndTime, @Param("checkStartTime") String checkStartTime, @Param("checkEndTime") String checkEndTime, @Param("orderByField") String orderByField, @Param("isAsc") boolean isAsc);

    /**
     * 查询总数
     */
    int selectsum(String account);

    /**
     * 根据条件查询事故列表 不分页
     */
    List<Map<String, Object>> selectAccident(@Param("dataScope") DataScope dataScope, @Param("openid") String openid, @Param("createStartTime") String createStartTime, @Param("createEndTime") String createEndTime,
                                             @Param("checkStartTime") String checkStartTime, @Param("checkEndTime") String checkEndTime, @Param("name") String name);

    /**
     * 新增事故
     */
    Integer insertAccid(@Param("accident") Accident accident);

    /**
     * 获取上传视频缩略图
     *
     * @param openId openId
     * @param accId  事故id
     * @return 事故缩略图url
     */
    String findUrlByAccIdAndOpenId(@Param("openId") String openId, @Param("accId") Integer accId);
}
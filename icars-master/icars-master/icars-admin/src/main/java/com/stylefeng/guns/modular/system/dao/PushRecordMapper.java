package com.stylefeng.guns.modular.system.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.modular.system.model.PushRecord;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 事故推送记录 Mapper 接口
 * </p>
 *
 * @author kosans
 * @since 2018-07-26
 */
public interface PushRecordMapper extends BaseMapper<PushRecord> {
    int setStatus(@Param("id") Integer id, @Param("status") int status, @Param("modifyTime") Date modifyTime);

    /**
     * 获取事故推送记录
     */
    List<Map<String, Object>> getPushRecords(@Param("page") Page<PushRecord> page, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("account") String account, @Param("orderByField") String orderByField, @Param("isAsc") boolean isAsc);

    /**
     * 获取事故推送记录
     */
    List<Map<String, Object>> getPushRecords2(@Param("page") Page<PushRecord> page, @Param("realness") int realness, @Param("account") String account);

    /**
     * 获取事故推送记录
     */
    List<Map<String, Object>> getPushRecords3(@Param("accid") int accid, @Param("account") String account);


    /**
     * 获取4S事故推送记录
     */
    List<Map<String, Object>> getPushFSRecords(@Param("page") Page<PushRecord> page, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("deptid") Integer deptid, @Param("orderByField") String orderByField, @Param("isAsc") boolean isAsc);

    /**
     * 根据业务员账号获取事故推送记录
     */
    List<Map<String, Object>> getPushRecordsByAccount(@Param("page") Page<PushRecord> page, @Param("account") String account, @Param("orderByField") String orderByField, @Param("isAsc") boolean isAsc);

    List<Map<String, Object>> getAppHomeAccidentWait(@Param("account")String account);

    List<Map<String, Object>> getAppHomeAccidentNoWait(@Param("account")String account);
}

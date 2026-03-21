package com.stylefeng.guns.modular.system.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.modular.system.model.PushRecord;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 事故推送记录 服务类
 * </p>
 *
 * @author kosans
 * @since 2018-07-26
 */
public interface IPushRecordService extends IService<PushRecord> {

    /**
     * 获取事故推送记录列表
     */
    List<Map<String, Object>> getPushRecords2(@Param("page") Page<PushRecord> page,@Param("realness") int realness, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("account") String account, @Param("orderByField") String orderByField, @Param("isAsc") boolean isAsc);

    /**
     * 获取事故推送记录
     */
    List<Map<String, Object>> getPushRecords(@Param("page") Page<PushRecord> page,@Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("account") String account, @Param("orderByField") String orderByField, @Param("isAsc") boolean isAsc);

    /**
     * app首页事故
     */
    Map<String, Object> getAppHomeAccident(String account);

    /**
     * 获取事故推送详情
     */
    List<Map<String, Object>> getPushRecords3(@Param("accid") int accid, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("account") String account, @Param("orderByField") String orderByField, @Param("isAsc") boolean isAsc);




    /**
     * 获取事故推送记录
     */
    List<Map<String, Object>> getPushFSRecords(@Param("page") Page<PushRecord> page, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("deptid") Integer deptid, @Param("orderByField") String orderByField, @Param("isAsc") boolean isAsc);

    /**
     * 通过业务员账户号获取事故推送记录
     */
    List<Map<String, Object>> getPushRecordsByAccount(@Param("page") Page<PushRecord> page, @Param("account") String account, @Param("orderByField") String orderByField, @Param("isAsc") boolean isAsc);

    /**
     * 修改状态
     * @param id
     * @param status
     * @param modifyTime
     * @return
     */
    int setStatus(@Param("id") Integer id, @Param("status") int status, @Param("modifyTime") Date modifyTime);
    /**
     * 扣理赔顾问的钱
     */
    void pushClaims(List<PushRecord> pushRecords, String account, BigDecimal accLevelValue);
}

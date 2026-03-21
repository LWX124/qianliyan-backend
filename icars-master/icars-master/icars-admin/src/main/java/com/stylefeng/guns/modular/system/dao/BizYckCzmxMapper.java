package com.stylefeng.guns.modular.system.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.modular.system.model.BizYckCzmx;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 外部理赔用户预存款操作明细表 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-15
 */
public interface BizYckCzmxMapper extends BaseMapper<BizYckCzmx> {
    List<Map<String, Object>> selectList(@Param("page") Page<BizYckCzmx> page, @Param("condition") String condition, @Param("createStartTime") String createStartTime, @Param("createEndTime") String createEndTime, @Param("detailType") Integer detailType, @Param("orderByField") String orderByField, @Param("isAsc") boolean isAsc);
    List<Map<String, Object>> getOrderWaste(@Param("page") Page<BizYckCzmx> page, @Param("account") String account);
}

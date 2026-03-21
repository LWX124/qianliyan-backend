package com.stylefeng.guns.modular.system.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.modular.system.model.AlipayActivity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 支付宝营销红包活动表 Mapper 接口
 * </p>
 *
 * @author kosans
 * @since 2018-07-31
 */
public interface AlipayActivityMapper extends BaseMapper<AlipayActivity> {
    /**
     * 根据条件查询事故列表 分页
     */
    List<Map<String, Object>> selectListForPage(@Param("page") Page<AlipayActivity> page, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("orderByField") String orderByField, @Param("isAsc") boolean isAsc);

    AlipayActivity selectMaxNew();
}

package com.stylefeng.guns.modular.system.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.datascope.DataScope;
import com.stylefeng.guns.modular.system.model.BizWxpayOrder;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-10
 */
public interface BizWxpayOrderMapper extends BaseMapper<BizWxpayOrder> {
    /**
     * 根据条件查询事故列表  分页
     */
    List<Map<String, Object>> selectList(@Param("page") Page<BizWxpayOrder> page, @Param("dataScope") DataScope dataScope, @Param("condition") String condition, @Param("outTradeNo") String outTradeNo, @Param("createStartTime") String createStartTime, @Param("createEndTime") String createEndTime, @Param("status") Integer status, @Param("orderByField") String orderByField, @Param("isAsc") boolean isAsc);

    BizWxpayOrder selectByOutTradeNo(@Param("out_trade_no") String out_trade_no);
}

package com.stylefeng.guns.modular.system.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.modular.system.model.BizAlipayBill;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 支付宝营销红包支付主表 Mapper 接口
 * </p>
 *
 * @author kosan
 * @since 2018-08-01
 */
public interface BizAlipayBillMapper extends BaseMapper<BizAlipayBill> {
    /**
     * 根据条件查询事故列表 分页
     */
    List<Map<String, Object>> selectListForPage(@Param("page") Page<BizAlipayBill> page, @Param("payStatus") Integer payStatus, @Param("alipayAccount") String alipayAccount, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 根据openid查询红包记录
     */
    List<Map<String, Object>> selectListForPageByOpenid(@Param("page") Page<BizAlipayBill> page, @Param("openid") String openid);

    Integer add(@Param("bizAlipayBill") BizAlipayBill bizAlipayBill);

    /**
     * 根据openid查询红包记录
     */
    List<BizAlipayBill> selectListForRePay(@Param("status") Integer status);
}

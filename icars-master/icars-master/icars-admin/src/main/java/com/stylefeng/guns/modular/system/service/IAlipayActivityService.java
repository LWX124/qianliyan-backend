package com.stylefeng.guns.modular.system.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.modular.system.model.AlipayActivity;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 支付宝营销红包活动表 服务类
 * </p>
 *
 * @author kosans
 * @since 2018-07-31
 */
@Transactional
public interface IAlipayActivityService extends IService<AlipayActivity> {
    List<Map<String, Object>> selectListForPage(@Param("page") Page<AlipayActivity> page, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("orderByField") String orderByField, @Param("isAsc") boolean isAsc);

    List<Map<String, Object>> selectListForPageFromApi(int pageSize, int pageIndex, String campStatus);

    void add(AlipayActivity alipayActivity);

    AlipayActivity selectMaxNew();
}

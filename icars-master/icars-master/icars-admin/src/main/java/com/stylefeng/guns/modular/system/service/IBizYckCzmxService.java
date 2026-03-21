package com.stylefeng.guns.modular.system.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.stylefeng.guns.modular.system.model.BizYckCzmx;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 外部理赔用户预存款操作明细表 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-15
 */
public interface IBizYckCzmxService extends IService<BizYckCzmx> {
    List<Map<String, Object>> selectList(Page<BizYckCzmx> page, String condition, String createStartTime, String createEndTime, Integer detailType, String orderByField, boolean isAsc);

    List<Map<String, Object>> getOrderWaste(Page<BizYckCzmx> page, String account);


}

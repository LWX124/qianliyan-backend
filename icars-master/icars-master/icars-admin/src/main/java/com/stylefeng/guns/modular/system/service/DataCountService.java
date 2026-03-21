package com.stylefeng.guns.modular.system.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.modular.system.model.Accident;
import com.stylefeng.guns.modular.system.model.WxCouponCountModel;

import java.util.List;
import java.util.Map;

/**
 * <ul>
 * <li>文件包名 : com.stylefeng.guns.modular.system.service</li>
 * <li>创建时间 : 2019-03-14 16:21</li>
 * <li>修改记录 : 无</li>
 * </ul>
 * 类说明：
 *
 * @author duanhong
 * @version 2.0.0
 */
public interface DataCountService {


    /**
     * 统计红包数据
     *
     * @return
     */
    List<WxCouponCountModel> countData(String startTime, String endTime);

    /**
     * 理赔员、4s员工数据统计
     */
    List<Map> userCount(String startTime, String endTime, String name, String department);

    Object xcxUserCount(Page<Accident> page);
}

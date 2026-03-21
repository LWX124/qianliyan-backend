package com.stylefeng.guns.modular.system.warpper;

import com.stylefeng.guns.core.base.warpper.BaseControllerWarpper;
import com.stylefeng.guns.core.common.constant.factory.ConstantFactory;

import java.util.List;
import java.util.Map;

/**
 * 支付宝红包活动管理的包装类
 *
 * @author kosan
 * @date 2018年7月31日 下午10:47:03
 */
public class AlipayActivityWarpper extends BaseControllerWarpper {

    public AlipayActivityWarpper(List<Map<String, Object>> list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
        map.put("campStatus", ConstantFactory.me().getAlipayActivityStatusName(map.get("campStatus").toString()));
    }

}

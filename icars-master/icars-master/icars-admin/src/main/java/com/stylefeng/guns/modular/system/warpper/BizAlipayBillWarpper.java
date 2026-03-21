package com.stylefeng.guns.modular.system.warpper;

import com.stylefeng.guns.core.base.warpper.BaseControllerWarpper;
import com.stylefeng.guns.core.common.constant.factory.ConstantFactory;

import java.util.List;
import java.util.Map;

/**
 * 支付宝支付结果管理的包装类
 *
 * @author kosan
 * @date 2018年7月31日 下午10:47:03
 */
public class BizAlipayBillWarpper extends BaseControllerWarpper {

    public BizAlipayBillWarpper(List<Map<String, Object>> list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
        map.put("status", ConstantFactory.me().getBizAlipayBillStatusName((Integer)map.get("status")));
    }

}

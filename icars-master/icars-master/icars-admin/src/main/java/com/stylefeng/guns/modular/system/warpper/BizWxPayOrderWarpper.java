package com.stylefeng.guns.modular.system.warpper;

import com.stylefeng.guns.core.base.warpper.BaseControllerWarpper;
import com.stylefeng.guns.core.common.constant.factory.ConstantFactory;

import java.util.List;
import java.util.Map;

/**
 * 事故上报管理的包装类
 *
 * @author kosan
 * @date 2017年2月13日 下午10:47:03
 */
public class BizWxPayOrderWarpper extends BaseControllerWarpper {

    public BizWxPayOrderWarpper(List<Map<String, Object>> list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
        map.put("statusName", ConstantFactory.me().getWxPayStatusName((Integer) map.get("status")));
    }

}

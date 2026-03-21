package com.stylefeng.guns.modular.system.warpper;

import com.stylefeng.guns.core.base.warpper.BaseControllerWarpper;
import com.stylefeng.guns.core.common.constant.factory.ConstantFactory;

import java.util.List;
import java.util.Map;

/**
 * 理赔单管理的包装类
 *
 * @author kosan
 * @date 2018年7月31日 下午10:47:03
 */
public class BizClaimWarpper extends BaseControllerWarpper {

    public BizClaimWarpper(List<Map<String, Object>> list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
        map.put("status", ConstantFactory.me().getBizClaimStatusName((Integer)map.get("status")));
        map.put("type", ConstantFactory.me().getBizClaimTypeName((Integer)map.get("type")));
        map.put("mapUrl","https://apis.map.qq.com/uri/v1/geocoder?coord="+map.get("lat")+","+map.get("lng")+"&coord_type=1&refere=myapp&coord_type=1");
    }

}

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
public class AccdWarpper extends BaseControllerWarpper {

    public AccdWarpper(List<Map<String, Object>> list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
        map.put("statusName", ConstantFactory.me().getAccdStatusName((Integer) map.get("status")));
        map.put("realness", map.get("realness") == null ? "未知" : getRealnessName((Integer)(map.get("realness"))));
        map.put("mapUrl","https://apis.map.qq.com/uri/v1/geocoder?coord="+map.get("lat")+","+map.get("lng")+"&coord_type=1&refere=myapp&coord_type=1");
    }

    private String getRealnessName(Integer realness){
        if(realness == 0){
            return "有效";
        }else if (realness == 1){
            return "无效";
        }
        return "未知";
    }
}

package com.stylefeng.guns.modular.system.warpper;

import com.stylefeng.guns.core.base.warpper.BaseControllerWarpper;
import com.stylefeng.guns.core.common.constant.factory.ConstantFactory;
import com.stylefeng.guns.modular.system.constant.OpenClaimOrderStatus;

import java.util.List;
import java.util.Map;

/**
 * 事故上报管理的包装类
 *
 * @author kosan
 * @date 2017年2月13日 下午10:47:03
 */
public class OpenClaimWarpper extends BaseControllerWarpper {

    public OpenClaimWarpper(List<Map<String, Object>> list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
        if((int)map.get("status") == OpenClaimOrderStatus.INIT.getCode() && map.get("cph") == null){
            map.put("statusNameForWx", "推修订单");
        }
        map.put("statusName", ConstantFactory.me().getOpenClaimOrderStatusName((Integer) map.get("status")));
        map.put("claimImgList", map.get("claimImg") == null ? null : map.get("claimImg").toString().split("\\|"));
        map.put("detailImgList", map.get("detailImg") == null ? null : map.get("detailImg").toString().split("\\|"));

            String[] b=new String[0];
        if( map.get("claimImg")==null||"".equals(map.get("claimImg"))||"null".equals(map.get("claimImg"))){
            map.put("claimImgList",b);
        }
        if( map.get("detailImg")==null||"".equals(map.get("detailImg"))||"null".equals(map.get("detailImg"))){
            map.put("detailImgList",b);
        }


        map.put("address", map.get("address").toString().replace("& #40;","(").replace("& #41",")"));
        map.put("mapUrl","https://apis.map.qq.com/uri/v1/geocoder?coord="+map.get("lat")+","+map.get("lng")+"&coord_type=1&refere=myapp&coord_type=1");
    }
}

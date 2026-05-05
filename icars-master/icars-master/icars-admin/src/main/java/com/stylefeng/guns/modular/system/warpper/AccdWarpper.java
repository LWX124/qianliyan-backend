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
        // 来源标识映射为可读名称
        Object sourceObj = map.get("source");
        map.put("source", getSourceName(sourceObj != null ? sourceObj.toString() : null));
        // 视频URL通过Nginx反代中转（解决CDN域名SSL证书问题）
        // Nginx配置: location /qiniu/ { proxy_pass http://cdn.meisaizhixing.cn/; }
        Object videoObj = map.get("video");
        if (videoObj != null) {
            String videoUrl = videoObj.toString();
            if (videoUrl.startsWith("https://cdn.meisaizhixing.cn/")) {
                map.put("video", "/qiniu/" + videoUrl.substring("https://cdn.meisaizhixing.cn/".length()));
            }
        }
    }

    private String getSourceName(String source) {
        if (source == null || source.isEmpty()) {
            return "一起拍事故"; // 默认来源（历史数据无source字段）
        }
        switch (source.toUpperCase()) {
            case "SSP": return "一起拍事故";
            case "TTP": return "天天拍";
            default: return source;
        }
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

package com.stylefeng.guns.modular.system.constant.dictmap;

import com.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 用户的字典
 *
 * @author kosan
 * @date 2017-05-06 15:01
 */
public class AccdDict extends AbstractDictMap {

    @Override
    public void init() {
        put("accdId","事故id");
        put("openid","上报用户ID");
        put("video","视频url");
        put("lng","事故上报经度");
        put("lat","事故上报纬度");
        put("createTime","事故上报时间");
        put("checkId","审核人id");
        put("checkTime","审核时间");
        put("status","审核状态");
    }

    @Override
    protected void initBeWrapped() {
    }
}

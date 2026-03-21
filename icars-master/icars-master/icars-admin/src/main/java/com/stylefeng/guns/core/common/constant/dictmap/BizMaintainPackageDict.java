package com.stylefeng.guns.core.common.constant.dictmap;

import com.stylefeng.guns.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 保养套餐的字典
 *
 * @author kosan
 * @date 2017-05-06 15:01
 */
public class BizMaintainPackageDict extends AbstractDictMap {

    @Override
    public void init() {
        put("id","保养套餐id");
        put("packageName","套餐名称");
        put("detail","套餐明细");
        put("price","价格");
    }

    @Override
    protected void initBeWrapped() {
        putFieldWrapperMethodName("id","getId");
        putFieldWrapperMethodName("packageName","getPackageName");
        putFieldWrapperMethodName("detail","getDetail");
        putFieldWrapperMethodName("price","getPrice");
    }
}

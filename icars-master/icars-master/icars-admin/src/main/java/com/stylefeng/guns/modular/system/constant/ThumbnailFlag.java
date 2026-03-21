package com.stylefeng.guns.modular.system.constant;

import com.stylefeng.guns.core.common.annotion.CnName;

public interface ThumbnailFlag {

    @CnName("已处理")
    Integer PROCESSED=1;

    @CnName("未处理")
    Integer UNTREATED=2;
}

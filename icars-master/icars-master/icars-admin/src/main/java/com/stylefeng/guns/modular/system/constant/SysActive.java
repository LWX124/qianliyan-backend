package com.stylefeng.guns.modular.system.constant;

import com.stylefeng.guns.core.common.annotion.CnName;

public interface SysActive {

    @CnName("本地环境")
    String LOCAL="local";

    @CnName("测试环境")
    String TEST="test";

    @CnName("生产环境")
    String PRO="prod";
}

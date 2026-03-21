package com.stylefeng.guns.modular.system.constant;

import com.stylefeng.guns.core.common.annotion.CnName;

/**
 * 事故等级
 */
public interface AccidentProperty {

    @CnName("待处理")
    int WAIT=-1;

    @CnName("真实")
    int REAL=0;

    @CnName("不真实")
    int NOT_REAL=1;

    @CnName("已撤离")
    int EVACUATE=2;

    @CnName("非事故")
    int NOT_ACCIDENT=3;


}

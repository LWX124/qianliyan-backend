package com.stylefeng.guns.modular.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.stylefeng.guns.modular.system.model.BizWxUserGzh;

public interface IBizWxUserGzhService extends IService<BizWxUserGzh> {
    BizWxUserGzh selectBizWxUserGzh(String wxOpenId);
}

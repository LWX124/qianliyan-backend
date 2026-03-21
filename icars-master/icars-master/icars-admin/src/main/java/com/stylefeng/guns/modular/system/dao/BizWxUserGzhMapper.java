package com.stylefeng.guns.modular.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.stylefeng.guns.modular.system.model.BizWxUserGzh;

public interface BizWxUserGzhMapper  extends BaseMapper<BizWxUserGzh> {
    BizWxUserGzh selectBizWxUserByOpenId(String wxOpenId);
}

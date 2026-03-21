package com.stylefeng.guns.modular.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.stylefeng.guns.modular.system.dao.BizWxUserGzhMapper;
import com.stylefeng.guns.modular.system.dao.BizWxUserMapper;
import com.stylefeng.guns.modular.system.model.BizWxUserGzh;
import com.stylefeng.guns.modular.system.service.IBizWxUserGzhService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class BizWxUserGzhServiceImpl extends ServiceImpl<BizWxUserGzhMapper, BizWxUserGzh> implements IBizWxUserGzhService {
    @Resource
    private BizWxUserGzhMapper bizWxUserGzhMapper;
    @Override
    public BizWxUserGzh selectBizWxUserGzh(String wxOpenId) {
        return bizWxUserGzhMapper.selectBizWxUserByOpenId(wxOpenId);
    }
}

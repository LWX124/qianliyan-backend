package com.stylefeng.guns.modular.system.service.impl;

import com.stylefeng.guns.modular.system.model.BizClaimerShow;
import com.stylefeng.guns.modular.system.dao.BizClaimerShowMapper;
import com.stylefeng.guns.modular.system.service.IBizClaimerShowService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 模范理赔顾问表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-08-28
 */
@Service
public class BizClaimerShowServiceImpl extends ServiceImpl<BizClaimerShowMapper, BizClaimerShow> implements IBizClaimerShowService {

    @Override
    public List<Map<String, Object>> selectClaimerShows(String name, Integer status) {
        return this.baseMapper.selectClaimerShows(name, status);
    }

    @Override
    public List<Map<String, Object>> selectClaimerShowsYes() {
        return this.baseMapper.selectClaimerShowsYes();
    }

    @Override
    public List<Map<String, Object>> getThreeModel() {
        return this.baseMapper.getThreeModel();
    }
}

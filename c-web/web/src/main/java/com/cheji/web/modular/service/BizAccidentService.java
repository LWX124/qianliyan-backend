package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.cheji.web.modular.domain.BizAccidentEntity;
import com.cheji.web.modular.mapper.BizAccidentMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 事故上报信息表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2021-01-12
 */
@Service
public class BizAccidentService extends ServiceImpl<BizAccidentMapper, BizAccidentEntity> implements IService<BizAccidentEntity> {
    @Resource
    private BizAccidentMapper bizAccidentMapper;

    public BizAccidentEntity findById(Integer accid) {
        return bizAccidentMapper.findById(accid);
    }
}

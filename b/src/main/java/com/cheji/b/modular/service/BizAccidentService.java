package com.cheji.b.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.domain.BizAccident;
import com.cheji.b.modular.dto.ListDetailsDto;
import com.cheji.b.modular.mapper.BizAccidentMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 事故上报信息表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-10-30
 */
@Service
public class BizAccidentService extends ServiceImpl<BizAccidentMapper, BizAccident> implements IService<BizAccident> {
    @Resource
    private BizAccidentMapper bizAccidentMapper;

    public ListDetailsDto findBizAccident(String accid) {
        return bizAccidentMapper.findBizAccident(accid);
    }

    public BizAccident findById(Integer accid) {
        return bizAccidentMapper.findById(accid);
    }
}

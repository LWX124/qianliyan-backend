package com.cheji.b.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.cheji.b.modular.domain.CdPartsDetailsEntity;
import com.cheji.b.modular.domain.CdRepairOrderEntity;
import com.cheji.b.modular.mapper.CdRepairOrderMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * <p>
 * 工单表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2022-01-20
 */
@Service
public class CdRepairOrderService extends ServiceImpl<CdRepairOrderMapper, CdRepairOrderEntity> implements IService<CdRepairOrderEntity> {

    @Resource
    private CdRepairOrderMapper cdRepairOrderMapper;

    public CdRepairOrderEntity selectIndentByType(Integer indentId, Integer type) {
        return cdRepairOrderMapper.selectIndentByType(indentId,type);
    }

    public Integer findSaleFood(Integer indentId) {
        return cdRepairOrderMapper.findSaleFood(indentId);
    }

    public BigDecimal findOrderMoney(Integer indentId, int i) {
        return cdRepairOrderMapper.findOrderMoney(indentId,i);
    }

    public BigDecimal selectWorkPrice(Integer indentId) {
        return cdRepairOrderMapper.selectWorkPrice(indentId);
    }
}

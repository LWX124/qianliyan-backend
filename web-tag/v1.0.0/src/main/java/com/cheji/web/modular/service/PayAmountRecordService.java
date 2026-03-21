package com.cheji.web.modular.service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cheji.web.modular.cwork.BillList;
import com.cheji.web.modular.cwork.BillListDetail;
import com.cheji.web.modular.domain.PayAmountRecordEntity;
import com.cheji.web.modular.mapper.PayAmountRecordMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 红包金额记录表 服务实现类
 * </p>
 *
 * @author Ashes
 * @since 2019-09-02
 */
@Service
public class PayAmountRecordService extends ServiceImpl<PayAmountRecordMapper, PayAmountRecordEntity> implements IService<PayAmountRecordEntity> {

    @Resource
    private PayAmountRecordMapper payAmountRecordMapper;

    public List<BillListDetail> findRecordByid(String id) {
        return payAmountRecordMapper.findRecordByid(id);
    }

    public List<BillList> findListByid(String id) {
        return payAmountRecordMapper.findListByid(id);
    }

    public List<BillList> findListByidAndDate(String id, String date) {
        return payAmountRecordMapper.findListByidAndDate(id,date);
    }

    public BigDecimal findtodayPayAmount(Integer id) {
        return payAmountRecordMapper.findtodayPayAmount(id);
    }

    public BigDecimal findMonthPayAMount(Integer id) {
        return payAmountRecordMapper.findMonthPayAMount(id);
    }

    public BigDecimal findPayAmount(Integer id) {
        return payAmountRecordMapper.findPayAmount(id);
    }
}

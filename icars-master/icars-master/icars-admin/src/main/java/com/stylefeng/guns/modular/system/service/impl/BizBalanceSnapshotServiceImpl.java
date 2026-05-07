package com.stylefeng.guns.modular.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.stylefeng.guns.modular.system.dao.BizBalanceSnapshotMapper;
import com.stylefeng.guns.modular.system.model.BizBalanceSnapshot;
import com.stylefeng.guns.modular.system.service.IBizBalanceSnapshotService;
import com.stylefeng.guns.modular.system.service.IBizWxpayBillService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Service
public class BizBalanceSnapshotServiceImpl extends ServiceImpl<BizBalanceSnapshotMapper, BizBalanceSnapshot>
        implements IBizBalanceSnapshotService {

    @Resource
    private IBizWxpayBillService bizWxpayBillService;

    @Override
    public BizBalanceSnapshot getLatestSnapshot() {
        return this.baseMapper.selectLatest();
    }

    @Override
    public Map<String, Object> getEstimatedBalance() {
        Map<String, Object> result = new HashMap<>();
        BizBalanceSnapshot snapshot = getLatestSnapshot();
        if (snapshot == null) {
            result.put("hasSnapshot", false);
            result.put("balanceStr", "暂无余额数据，等待每日自动获取");
            return result;
        }

        Map<String, Object> deducted = bizWxpayBillService.sumDeductedSinceSnapshot(snapshot.getCreateTime());
        long deductedCount = ((Number) deducted.get("deductedCount")).longValue();
        // SUM(amount) returns yuan (BigDecimal), convert to fen for calculation with snapshot balance
        BigDecimal deductedAmountYuan = new BigDecimal(deducted.get("deductedAmount").toString());
        long deductedAmountFen = deductedAmountYuan.multiply(new BigDecimal("100")).longValue();
        long estimatedBalance = snapshot.getBalance() - deductedAmountFen;

        BigDecimal balanceYuan = new BigDecimal(estimatedBalance).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal deductedYuan = deductedAmountYuan.setScale(2, RoundingMode.HALF_UP);
        BigDecimal thresholdYuan = new BigDecimal(snapshot.getAlertThreshold()).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(snapshot.getBillDate());

        boolean isAlert = estimatedBalance < snapshot.getAlertThreshold();

        String balanceStr;
        if (deductedCount > 0) {
            balanceStr = "预估余额：" + balanceYuan.toPlainString() + " 元（基于 " + dateStr + " 日终，已扣减 " + deductedCount + " 笔共 " + deductedYuan.toPlainString() + " 元）";
        } else {
            balanceStr = "预估余额：" + balanceYuan.toPlainString() + " 元（基于 " + dateStr + " 日终余额）";
        }
        if (isAlert) {
            balanceStr += " [低于报警阈值 " + thresholdYuan.toPlainString() + " 元]";
        }

        result.put("hasSnapshot", true);
        result.put("balanceStr", balanceStr);
        result.put("estimatedBalance", estimatedBalance);
        result.put("alertThreshold", snapshot.getAlertThreshold());
        result.put("isAlert", isAlert);
        result.put("snapshotDate", dateStr);
        result.put("deductedCount", deductedCount);
        result.put("deductedAmount", deductedAmountFen);
        return result;
    }

    @Override
    public void updateThreshold(long thresholdFen) {
        BizBalanceSnapshot snapshot = getLatestSnapshot();
        if (snapshot != null) {
            snapshot.setAlertThreshold(thresholdFen);
            this.baseMapper.updateById(snapshot);
        }
    }
}

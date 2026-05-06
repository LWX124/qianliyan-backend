package com.stylefeng.guns.modular.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.stylefeng.guns.modular.system.model.BizBalanceSnapshot;

import java.util.Map;

public interface IBizBalanceSnapshotService extends IService<BizBalanceSnapshot> {

    BizBalanceSnapshot getLatestSnapshot();

    Map<String, Object> getEstimatedBalance();

    void updateThreshold(long thresholdFen);
}

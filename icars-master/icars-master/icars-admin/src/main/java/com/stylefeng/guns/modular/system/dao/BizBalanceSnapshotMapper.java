package com.stylefeng.guns.modular.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.stylefeng.guns.modular.system.model.BizBalanceSnapshot;
import org.apache.ibatis.annotations.Param;

public interface BizBalanceSnapshotMapper extends BaseMapper<BizBalanceSnapshot> {

    BizBalanceSnapshot selectLatest();

    BizBalanceSnapshot selectByBillDate(@Param("billDate") String billDate);
}

package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.cwork.ChangeBill;
import com.cheji.web.modular.cwork.WithdDetails;
import com.cheji.web.modular.domain.AppWxCashOutRecordEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppWxCashOutRecordEntityMapper extends BaseMapper<AppWxCashOutRecordEntity> {

    List<ChangeBill> findMonthAndAmountByuserId(String userId);

    WithdDetails findDetailsByPartnerTradeNo(String id);

    List<ChangeBill> findMonthAndAmountByuserIdAndDate(@Param("userId")String userId, @Param("date")String date);

    List<Integer> findAgoWxCashOut(Integer id);

    Integer selectCountByUserId(Integer userId);

}

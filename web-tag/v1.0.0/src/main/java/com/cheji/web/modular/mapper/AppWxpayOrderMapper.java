package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.cwork.ChangeBill;
import com.cheji.web.modular.cwork.ChangeList;
import com.cheji.web.modular.domain.AppWxpayOrderEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppWxpayOrderMapper extends BaseMapper<AppWxpayOrderEntity> {

    List<ChangeBill> findMonthAndAmountByuserId(String userId);

    List<ChangeList> findListByMonth(@Param("userId")String userId, @Param("thisMonth")String thisMonth);

    List<ChangeBill> findMonthAndAmountByuserIdAndDate(@Param("userId")String userId,@Param("date") String date);

    void updateStatus(AppWxpayOrderEntity appWxpayOrderEntity);

}

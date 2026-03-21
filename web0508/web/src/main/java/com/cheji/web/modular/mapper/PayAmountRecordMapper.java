package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.cwork.BillList;
import com.cheji.web.modular.cwork.BillListDetail;
import com.cheji.web.modular.domain.PayAmountRecordEntity;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 红包金额记录表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-09-02
 */
public interface PayAmountRecordMapper extends BaseMapper<PayAmountRecordEntity> {

    List<BillListDetail> findRecordByid(String id);

   // List<BillListDetail> findRecordByidAndDate(@Param("id")String id,@Param("date")String date);

    List<BillList> findListByid(String id);

    List<BillList> findListByidAndDate(@Param("id")String id,@Param("date") String date);

    BigDecimal findtodayPayAmount(Integer id);

    BigDecimal findMonthPayAMount(Integer id);

    BigDecimal findPayAmount(Integer id);
}

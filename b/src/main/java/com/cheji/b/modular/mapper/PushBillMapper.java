package com.cheji.b.modular.mapper;

import com.cheji.b.modular.domain.PushBillEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.b.modular.dto.CheckImgList;
import com.cheji.b.modular.dto.MineDto;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 用户扣费记录表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-09-25
 */
public interface PushBillMapper extends BaseMapper<PushBillEntity> {

    List<PushBillEntity> findScreenBill(@Param("id") Integer id, @Param("pagesize") Integer pagesize, @Param("date") String date);

    BigDecimal findPayAmount(@Param("userId")Long userId,@Param("yearmonth") String yearmonth);

    List<PushBillEntity> selectAllBillList(@Param("id")Integer id, @Param("pagesize")Integer pagesize);

    PushBillEntity findAccident(@Param("id")Integer id, @Param("s")String s);

    List<PushBillEntity> selectAllBillList2(@Param("id")Integer id, @Param("pagesize")Integer pagesize,@Param("state")Integer state);

    List<PushBillEntity> select234BillList(@Param("id")Integer id, @Param("pagesize")Integer pagesize,@Param("state")Integer state);

    Integer findAllNewTask(String id);

    Integer findComeScene(String id);

    Integer findPbTrackAgain(String id);

    Integer findPushSuccess(String id);

    List<PushBillEntity> selectAllBillListbySearch(@Param("id")Integer id, @Param("pagesize")Integer pagesize,@Param("text") String text);

    List<CheckImgList> selectSceneImg(Long id);

    MineDto findAllCount(@Param("id")Integer id, @Param("year")String year, @Param("month")String month);

}

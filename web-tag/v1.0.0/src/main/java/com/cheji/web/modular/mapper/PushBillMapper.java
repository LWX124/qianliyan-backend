package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.cwork.BillList;
import com.cheji.web.modular.cwork.BillListDetail;
import com.cheji.web.modular.cwork.CheckImgList;
import com.cheji.web.modular.cwork.TrackStateNumber;
import com.cheji.web.modular.domain.PushBillEntity;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 用户扣费记录表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-09-02
 */
public interface PushBillMapper extends BaseMapper<PushBillEntity> {

    List<BillList> findBillListByid(String id);

    List<BillListDetail> findRecordByid(String id);

    List<BillList> findBillListByidAndDate(@Param("id")String id,@Param("date")String date);

    // List<BillListDetail> findRecordByidAndDate(@Param("id")String id,@Param("date") String date);

    BigDecimal findPayAmount(@Param("userId")Long userId, @Param("yearmonth") String yearmonth);

    List<PushBillEntity> selectAllBillList(@Param("id")Integer id, @Param("pagesize")Integer pagesize,@Param("state")Integer state);


    TrackStateNumber selectStateNumberBySOS(String bid);


    TrackStateNumber selectStateNumberByPB(String bid);

    List<CheckImgList> selectSceneImg(Long id);

    List<PushBillEntity> selectAllBillListbySearch(@Param("id")Integer id, @Param("pagesize")Integer pagesize,@Param("text") String text);

    List<PushBillEntity> select234BillList(@Param("id")Integer id, @Param("pagesize")Integer pagesize,@Param("state")Integer state);

    Integer findAllNewTask(String bid);

    Integer findPbAgree(String bid);

    Integer findPbTrackAgain(String bid);

    Integer findPbRefuse(String bid);

    Integer findSosAgree(String bid);

    Integer findSosTrackAgain(String bid);

    Integer findSosRefuse(String bid);

    Integer findComeScene(String bid);

    Integer findNotToScene(String bid);

    Integer findNoCar(String bid);

    Integer findPushSuccess(String bid);

    Integer findSosPushSuccess(String bid);

    PushBillEntity findAccident(@Param("id")Integer id, @Param("s")String s);
}

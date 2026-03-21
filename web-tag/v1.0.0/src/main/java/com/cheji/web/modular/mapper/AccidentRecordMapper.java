package com.cheji.web.modular.mapper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cheji.web.modular.cwork.AccidentDetails;
import com.cheji.web.modular.cwork.AccidentList;
import com.cheji.web.modular.cwork.AccidentReward;
import com.cheji.web.modular.cwork.WorkList;
import com.cheji.web.modular.domain.AccidentRecordEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * app上报事故信息表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-08-28
 */
public interface AccidentRecordMapper extends BaseMapper<AccidentRecordEntity> {

    AccidentList findAccMesById(@Param("accid")Integer accid, @Param("state") Integer state);

    AccidentDetails findDeatilsByAccid(String accid);

    List<AccidentList> findAccidListByIdAndDate(@Param("userid")Integer userid,@Param("pagesize")Integer pagesize);

    //报事故页面红包总数
    List<AccidentList> findAccidByUserid(@Param("userid")Integer userid,@Param("pagesize")Integer pagesize);

    List<WorkList> findWorkByUserId(@Param("userId")Integer userId,@Param("pagesize")Integer pagesize);

    List<WorkList> findWorkByUserIdAndType(@Param("userId")String userId,@Param("type") Integer type);

    List<WorkList> findVideoByUserid(Integer userId);

    List<AccidentRecordEntity> selectFiveAgo(Integer id);

    List<AccidentRecordEntity> selectByUserIdAndLngLat(@Param("userId")String userId, @Param("lng")Double lng, @Param("lat")Double lat);

    BigDecimal selectPayRecord(String id);

    List<AccidentReward> findAccidentReward(Integer pagesize);

    List<BigDecimal> findRedPayAmount(String id);

    List<Integer> findSeriousAcc(Integer pagesize);

    AccidentReward findSeriousMes(Integer accid);

    List<Integer> findYourself(Integer id, Integer pagesize);

    List<BigDecimal> findRedPayAmountByHim(@Param("id")String id,@Param("userId")Integer userId);
}

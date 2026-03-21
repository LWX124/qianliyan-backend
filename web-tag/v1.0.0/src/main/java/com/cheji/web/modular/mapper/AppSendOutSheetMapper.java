package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.cwork.CheckImgList;
import com.cheji.web.modular.domain.AccidentRecordEntity;
import com.cheji.web.modular.domain.AppSendOutSheetEntity;
import com.cheji.web.modular.domain.PushBillEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * web派单记录表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2021-01-11
 */
public interface AppSendOutSheetMapper extends BaseMapper<AppSendOutSheetEntity> {

    List<String> findIndex1Img(Long id);

    List<CheckImgList> selectSenceImg(Long id);

    List<Integer> findBrandMerIdList(Integer brandId);


    Integer findWaitPB(Integer userId);

    Integer findPhone(Integer userId);

    Integer findCheckTime(Integer userId);

    Integer findHaveVioce(Integer userId);

    List<AccidentRecordEntity> findAccidentList(@Param("userId") Integer userId, @Param("state")Integer state,@Param("pagesize")Integer pagesize);

    PushBillEntity findPbList(Integer id);
}

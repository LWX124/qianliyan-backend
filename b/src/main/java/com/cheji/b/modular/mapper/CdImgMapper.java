package com.cheji.b.modular.mapper;

import com.cheji.b.modular.domain.AccidentRecord;
import com.cheji.b.modular.domain.CdImgEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.b.modular.domain.PushBillEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 车店图片表
 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2022-01-12
 */
public interface CdImgMapper extends BaseMapper<CdImgEntity> {

    List<String> selectByInAdnTy(@Param("indentId") Integer indentId, @Param("i") int i);

    Integer findWaitPB(Integer userId);

    Integer findPhone(Integer userId);

    Integer findCheckTime(Integer userId);

    Integer findHaveVioce(Integer userId);

    List<AccidentRecord> findAccidentList(@Param("userId") Integer userId, @Param("state")Integer state,@Param("pagesize")Integer pagesize);

    PushBillEntity findPbList(Integer id);
}

package com.cheji.b.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.b.modular.domain.AppMessageCarEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2021-01-14
 */
public interface AppMessageCarMapper extends BaseMapper<AppMessageCarEntity> {

    List<Integer> findIsPush(String userBId, String carMid);

    List<String> selectScenePhotos(@Param("id")Long id, @Param("source") Integer source);

    Integer findAgain(Integer userId);

    Integer findAgree(Integer userId);

    Integer findRefused(Integer userId);

    Integer findFinish(Integer userId);

    List<String> selectUrlPhotos(@Param("cid")Integer cid, @Param("type")Integer type,@Param("source")Integer source);
}

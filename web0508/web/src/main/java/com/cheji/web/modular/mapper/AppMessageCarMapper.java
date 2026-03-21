package com.cheji.web.modular.mapper;

import com.cheji.web.modular.domain.AppMessageCarEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
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
}

package com.cheji.web.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.web.modular.domain.AppRepeatAccidentEntity;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2019-11-27
 */
public interface AppRepeatAccidentMapper extends BaseMapper<AppRepeatAccidentEntity> {

    List<AppRepeatAccidentEntity> selectByAccid(Integer id);

    String selectWxVideo(Integer accidentSource);

}

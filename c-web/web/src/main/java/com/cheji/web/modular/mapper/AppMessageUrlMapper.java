package com.cheji.web.modular.mapper;

import com.cheji.web.modular.domain.AppMessageUrlEntity;
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
public interface AppMessageUrlMapper extends BaseMapper<AppMessageUrlEntity> {

    List<String> selectUrlPhotos(@Param("cid") Integer cid, @Param("type")Integer type, @Param("source")Integer source);
}

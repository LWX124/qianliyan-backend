package com.cheji.b.modular.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.b.modular.domain.AppMessageUrlEntity;
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

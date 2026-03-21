package com.cheji.b.modular.mapper;

import com.cheji.b.modular.domain.CdCarEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cheji.b.modular.dto.CdCarDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2022-01-17
 */
public interface CdCarMapper extends BaseMapper<CdCarEntity> {

    List<CdCarDto> findBylevelAndPar(@Param("hierarchy")Integer hierarchy, @Param("parentId")Integer parentId);

}

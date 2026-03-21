package com.cheji.web.modular.mapper;

import com.cheji.web.modular.domain.AppSprayPaintImgEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 喷漆图片表 Mapper 接口
 * </p>
 *
 * @author Ashes
 * @since 2020-03-13
 */
public interface AppSprayPaintImgMapper extends BaseMapper<AppSprayPaintImgEntity> {

    List<AppSprayPaintImgEntity> selectInService(Integer id);
}
